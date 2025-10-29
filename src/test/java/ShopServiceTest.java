import exceptions.ProductNotFoundException;
import model.Order;
import model.OrderStatus;
import model.Product;
import org.junit.jupiter.api.Test;
import repositories.OrderMapRepo;
import repositories.OrderRepo;
import repositories.ProductRepo;
import services.ShopService;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo());
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_throwProductNotFoundException() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo());
        List<String> productsIds = List.of("1", "2");

        //THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void getOrdersByOrderStatus_returnsOnlyOrdersWithRequestedStatus() {
        OrderRepo orderRepo = new OrderMapRepo();
        orderRepo.addOrder(new Order("1", List.of(), OrderStatus.PROCESSING, Instant.MIN));
        orderRepo.addOrder(new Order("2", List.of(), OrderStatus.IN_DELIVERY, Instant.MIN));
        orderRepo.addOrder(new Order("3", List.of(), OrderStatus.PROCESSING, Instant.MIN));

        List<Order> expected = List.of(
                new Order("1", List.of(), OrderStatus.PROCESSING, Instant.MIN),
                new Order("3", List.of(), OrderStatus.PROCESSING, Instant.MIN)
        );

        ShopService shopService = new ShopService(new ProductRepo(), orderRepo);

        List<Order> result = shopService.getOrdersByOrderStatus(OrderStatus.PROCESSING);

        assertEquals(expected, result);
    }

    @Test
    void updateOrder() {
        OrderRepo orderRepo = new OrderMapRepo();
        orderRepo.addOrder(new Order("1", List.of(), OrderStatus.PROCESSING, Instant.MIN));
        orderRepo.addOrder(new Order("2", List.of(), OrderStatus.IN_DELIVERY, Instant.MIN));
        orderRepo.addOrder(new Order("3", List.of(), OrderStatus.PROCESSING, Instant.MIN));
        ShopService shopService = new ShopService(new ProductRepo(), orderRepo);

        Order expected = new Order("2", List.of(), OrderStatus.COMPLETED, Instant.MIN);
        Order actual = shopService.updateOrder("2", OrderStatus.COMPLETED);

        assertEquals(expected, actual);
    }
}
