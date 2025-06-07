package ureca.muneobe.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sangyunpark.muneo_entity.entity.chat.Chat;
import org.sangyunpark.muneo_entity.entity.common.BaseEntity;
import org.sangyunpark.muneo_entity.entity.review.Review;
import org.sangyunpark.muneo_entity.entity.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int old;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private String name;

    private Boolean activeYn;

    @Enumerated(EnumType.STRING)
    private Job job;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions = new ArrayList<>();
}