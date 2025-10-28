import model.Order;
import model.OrderStatus;
import model.Product;
import org.junit.jupiter.api.Test;
import repositories.OrderMapRepo;
import repositories.OrderRepo;
import repositories.ProductRepo;
import services.ShopService;

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
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo());
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }

    @Test
    void getOrdersByOrderStatus_returnsOnlyOrdersWithRequestedStatus() {
        OrderRepo orderRepo = new OrderMapRepo();
        orderRepo.addOrder(new Order("1", List.of(), OrderStatus.PROCESSING));
        orderRepo.addOrder(new Order("2", List.of(), OrderStatus.IN_DELIVERY));
        orderRepo.addOrder(new Order("3", List.of(), OrderStatus.PROCESSING));

        List<Order> expected = List.of(
                new Order("1", List.of(), OrderStatus.PROCESSING),
                new Order("3", List.of(), OrderStatus.PROCESSING)
        );

        ShopService shopService = new ShopService(new ProductRepo(), orderRepo);

        System.out.println(orderRepo.getOrders().stream()
                .filter(order -> order.orderStatus().equals(OrderStatus.PROCESSING))
                .toList());

        List<Order> result = shopService.getOrdersByOrderStatus(OrderStatus.PROCESSING);
        System.out.println(result);

        assertEquals(expected, result);
    }
}
