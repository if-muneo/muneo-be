package ureca.muneobe.common.openai.dto.router;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "router",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RdbResponse.class, name = "RDB"),
        @JsonSubTypes.Type(value = VectorResponse.class, name = "VECTOR"),
        @JsonSubTypes.Type(value = InappropriateResponse.class, name = "INAPPROPRIATE"),
        @JsonSubTypes.Type(value = DailyResponse.class, name = "DAILY")
})
public abstract class FirstPromptResponse {
    protected String router;
}
