package com.example.ecommerceapplication.service;

import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.User;
import com.example.ecommerceapplication.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Orders> getOrdersByCustomer(User user) {
        return orderRepository.findOrdersByCustomer(user);
    }
    public Orders createOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    public Orders updateOrder(long id, Orders orders) {
        if (orderRepository.existsById(id)) {
            orders.setId(id);
            Orders updatedOrder = orderRepository.save(orders);
            return updatedOrder;
        }
        return null;
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }

}
