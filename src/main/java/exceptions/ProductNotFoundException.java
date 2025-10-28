package exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Produkt id=" + id + " existiert nicht!");
    }
}
