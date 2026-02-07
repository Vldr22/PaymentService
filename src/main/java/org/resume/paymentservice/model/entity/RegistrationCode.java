package org.resume.paymentservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.resume.paymentservice.model.enums.Roles;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registration_codes")
public class RegistrationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
}
