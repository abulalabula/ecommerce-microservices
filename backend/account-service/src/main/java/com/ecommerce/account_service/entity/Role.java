package com.ecommerce.account_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private UserRole name;  // Uses Enum instead of String

    public Role() {}

    public Role(UserRole name) {
        this.name = name;
    }

    // Getter
    public UserRole getName() { return name; }  // 🔹 THIS IS WHERE `role.getName()` COMES FROM
}
