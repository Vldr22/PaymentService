package org.resume.paymentservice.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.model.enums.UserStatus;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @Column(length = 50)
    private String midname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(nullable = false)
    private boolean passwordChangeRequired;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Staff(String name, String surname, String midname,
                 String email, String password, Roles role,
                 boolean passwordChangeRequired) {
        this.name = name;
        this.surname = surname;
        this.midname = midname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.passwordChangeRequired = passwordChangeRequired;
    }

}
