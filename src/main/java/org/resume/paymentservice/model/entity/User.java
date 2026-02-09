package org.resume.paymentservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.model.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @Column(length = 50)
    private String midname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column()
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}