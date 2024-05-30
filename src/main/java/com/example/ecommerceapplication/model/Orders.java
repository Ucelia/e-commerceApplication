package com.example.ecommerceapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User customer;
    private LocalDate orderDate = LocalDate.now();
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
    private String orderStatus;
    private Double totalPrice;
    private String address;
    private String phoneNumber;
    @ManyToMany
    private List<Product> products;

    public String setAddress() {
        return address;
    }
}
