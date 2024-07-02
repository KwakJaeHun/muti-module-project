package com.jhkwak.userservice.entity.user;

import com.jhkwak.userservice.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "user")
@NoArgsConstructor
public class User extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 36)
    private String emailVerificationToken;

    @Column(nullable = false)
    private LocalDateTime emailVerificationExpiresAt = LocalDateTime.now().plusHours(24);

    @Column(nullable = false)
    private Boolean emailVerifiedStatus = false;

    public User(
        String name,
        String email,
        String password,
        String phone,
        String address,
        String emailVerificationToken,
        Boolean emailVerifiedStatus
    )
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.emailVerificationToken = emailVerificationToken;
        this.emailVerifiedStatus = emailVerifiedStatus;
    }
}
