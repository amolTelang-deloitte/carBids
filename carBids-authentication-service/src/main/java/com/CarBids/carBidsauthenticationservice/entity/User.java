package com.CarBids.carBidsauthenticationservice.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    private String username;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Email(message = "Please provide a valid email address")
    private String email;
    @NonNull
    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    private String phoneNumber;
    @NonNull
    private String password;
}
