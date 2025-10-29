package services;

import exceptions.ProductNotFoundException;
import model.Order;
import model.OrderStatus;
import model.Product;
import repositories.OrderRepo;
import repositories.ProductRepo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public ShopService(ProductRepo productRepo, OrderRepo orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();

        productIds.forEach(productId -> {
            Product product = productRepo
                    .getProductById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            products.add(product);
        });

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByOrderStatus(OrderStatus orderStatus) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.orderStatus().equals(orderStatus))
                .toList();
    }

    public Order updateOrder(String orderId, OrderStatus orderStatus) {
        Order oldOrder = orderRepo.getOrderById(orderId);
        Order newOrder = oldOrder.withOrderStatus(orderStatus);

        orderRepo.removeOrder(oldOrder.id());

        return orderRepo.addOrder(newOrder);
    }
}
