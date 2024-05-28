package com.example.ecommerceapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "{errors.invalid}")
    private String email;

    @NotEmpty
    private String password;

    @Column(nullable = false)
    private String role = "USER";

    @OneToMany
    private List<Orders> orders;


}


