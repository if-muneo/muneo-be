package ureca.muneobe.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ureca.muneobe.entity.chat.Chat;
import ureca.muneobe.entity.common.BaseEntity;
import ureca.muneobe.entity.review.Review;
import ureca.muneobe.entity.subscription.Subscription;

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