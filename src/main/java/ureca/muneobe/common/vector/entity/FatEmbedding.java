package ureca.muneobe.common.vector.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fat_embedding")
@Getter
@Setter
public class FatEmbedding {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "fat_id")
    private Long fatId;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private float[] embedding;
}
