package ureca.muneobe.common.vector.embedding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class EmbeddingSentence {
    @Value("${spring.ai.openai.api-key}")
    private String modelApiKey;
    @Value("${spring.ai.openai.models.embedding.model}")
    private String embeddingModel;
    // OpenAI에서 공식적으로 제공하는 임베딩 API의 고정 엔드포인트
    @Value("https://api.openai.com/v1/embeddings")
    private String apiUrl;

    private final ObjectMapper mapper;

    public float[] requestEmbeddingFromOpenAI(String text) throws IOException, InterruptedException {
        ObjectNode root = mapper.createObjectNode();
        root.withArray("input").add(text);
        root.put("model", embeddingModel);
        root.put("dimensions", 1536);

        String json = mapper.writeValueAsString(root);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + modelApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode dataNode = mapper.readTree(response.body()).get("data");
        if (dataNode == null || !dataNode.elements().hasNext()) {
            throw new RuntimeException("Could not get embedding model" + response.body());
        }

        JsonNode embeddingNode = dataNode.elements().next().get("embedding");
        if (embeddingNode == null || !embeddingNode.isArray()) {
            throw new RuntimeException("Could not get embedding model" + response.body());
        }

        float[] result = new float[embeddingNode.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (float) embeddingNode.get(i).asDouble();
        }

        return result;
    }
}