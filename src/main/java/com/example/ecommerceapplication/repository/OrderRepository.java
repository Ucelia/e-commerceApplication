package com.example.ecommerceapplication.repository;

import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findOrdersByCustomer(User customer);
}
