package ureca.muneobe.common.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table
@Getter
public class Slang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    protected Slang() {}

    public Slang(String word) {
        this.word = word;
    }

    public void setWord(String word) { this.word = word; }
}
