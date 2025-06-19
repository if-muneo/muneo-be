package ureca.muneobe.common.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.muneobe.common.auth.entity.enums.Category;
import ureca.muneobe.common.auth.entity.enums.Gender;
import ureca.muneobe.common.auth.entity.enums.Role;
import ureca.muneobe.global.common.BaseEntity;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(name = "old")
    private Integer old;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "active_yn")
    private Boolean activeYn;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "use_amount")
    private Integer useAmount;

    @Builder
    private Member(String name, String password, String phoneNumber,
                   String email, Integer old, Gender gender, Category category) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.old = old;
        this.gender = gender;
        this.category = category;
        this.activeYn = true;  // 기본값
        this.role = Role.USER; // 기본값
    }
}