package orders.integration;

import org.dilip.first.pos_backend.dto.ClientDto;
import org.dilip.first.pos_backend.dto.InventoryDto;
import org.dilip.first.pos_backend.dto.ProductDto;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.clients.ClientForm;
import org.dilip.first.pos_backend.model.inventory.InventoryCreateForm;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.dilip.first.pos_backend.model.orders.OrderForm;
import org.dilip.first.pos_backend.model.orders.OrderItemForm;
import org.dilip.first.pos_backend.model.orders.OrderData;
import org.dilip.first.pos_backend.constants.OrderStatus;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = org.dilip.first.pos_backend.PosBackendApplication.class)
@Transactional
public class OrderDtoTest {

    @Autowired
    private org.dilip.first.pos_backend.dto.OrderDto orderDto;

    @Autowired
    private ClientDto clientDto;

    @Autowired
    private ProductDto productDto;

    @Autowired
    private InventoryDto inventoryDto;

    private Long clientId;
    private Long productId;
    private String barcode;

    @BeforeEach
    public void setUp() {

        // GIVEN: a client exists in the system
        ClientForm clientForm = new ClientForm();
        clientForm.setName("Test Client");
        clientForm.setEmail("test@test.com");
        clientForm.setPhone("9999999999");
        clientId = clientDto.create(clientForm).getId();

        // GIVEN: a product exists for the client
        ProductForm productForm = new ProductForm();
        productForm.setClientId(clientId);
        productForm.setName("Test Product");
        productForm.setBarcode("barcode123");
        productForm.setMrp(100.00);
        productId = productDto.create(productForm).getId();

        // GIVEN: inventory exists for the product
        barcode = productForm.getBarcode();
        InventoryCreateForm inventoryForm = new InventoryCreateForm();
        inventoryForm.setProductId(productId);
        inventoryForm.setQuantity(100L);
        inventoryDto.create(inventoryForm);
    }

    @Test
    public void testCreateOrder_Success() {

        // GIVEN: a valid order form with one order item
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(5L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        // WHEN: the order is created
        OrderData data = orderDto.create(form);

        // THEN: the order should be created successfully
        assertNotNull(data, "OrderData should not be null");
        assertNotNull(data.getId(), "Order ID should be generated");
        assertEquals(OrderStatus.CREATED, data.getStatus(), "Order status should be CREATED");
    }

    @Test
    public void testGetOrderById_Success() {

        // GIVEN: an order already exists
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(2L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        OrderData created = orderDto.create(form);

        // WHEN: the order is fetched by its ID
        OrderData fetched = orderDto.getById(created.getId());

        // THEN: the correct order details should be returned
        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
        assertNotNull(fetched.getItems());
        assertEquals(1, fetched.getItems().size());
        assertEquals(barcode.toLowerCase(), fetched.getItems().get(0).getBarcode());
    }

    @Test
    public void testGetAllOrders_Success() {

        // GIVEN: multiple orders exist in the system
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(1L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        orderDto.create(form);
        orderDto.create(form);

        // WHEN: all orders are fetched
        List<OrderData> orders = orderDto.getAll(0, 10);

        // THEN: a list of orders should be returned
        assertNotNull(orders);
        assertTrue(orders.size() >= 2);
    }

    @Test
    public void testGetOrdersByStatus_Success() {

        // GIVEN: an order exists with CREATED status
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(1L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        orderDto.create(form);

        // WHEN: orders are fetched by status
        List<OrderData> orders = orderDto.getByStatus(OrderStatus.CREATED, 0, 10);

        // THEN: only orders with the given status should be returned
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(OrderStatus.CREATED, orders.get(0).getStatus());
    }

    @Test
    public void testGetOrdersByDateRange_success() {

        // GIVEN: an order exists within the date range
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(1L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        orderDto.create(form);

        // WHEN: orders are fetched within a date range
        List<OrderData> orders = orderDto.getByDateRange(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                0,
                10
        );

        // THEN: orders within the date range should be returned
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    public void testCreateOrderInsuffcientQuantity_fail() {
        OrderItemForm item = new OrderItemForm();
        item.setBarcode(barcode);
        item.setQuantity(102L);
        item.setSellingPrice(100.00);

        OrderForm form = new OrderForm();
        form.setItems(List.of(item));

        assertThrows(ApiException.class, () -> {
            orderDto.create(form);
        });
    }

    @Test
    public void testGetOrderById_fail() {
        long invalidOrderId=2000;
        assertThrows(ApiException.class, () -> {
            orderDto.getById(invalidOrderId);
        });
    }
}
