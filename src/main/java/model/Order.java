package model;

import lombok.With;

import java.time.Instant;
import java.util.List;

public record Order(
        String id,
        List<Product> products,
        @With OrderStatus orderStatus,
        Instant orderDate
) {
}
