package orders.unit;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.flow.OrderFlow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderFlowTest {

    @Mock
    private OrderApi orderApi;

    @Mock
    private ProductApi productApi;

    @Mock
    private InventoryApi inventoryApi;

    @InjectMocks
    private OrderFlow orderFlow;

    private ProductEntity testProduct1;
    private ProductEntity testProduct2;
    private InventoryEntity testInventory1;
    private InventoryEntity testInventory2;
    private OrderItemEntity testOrderItem1;
    private OrderItemEntity testOrderItem2;
    private OrderEntity testOrder;

    @BeforeEach
    void setUp() {
        // Setup test product 1
        testProduct1 = new ProductEntity();
        testProduct1.setId(1L);
        testProduct1.setBarcode("BAR001");
        testProduct1.setName("Product 1");
        testProduct1.setMrp(100.0);

        // Setup test product 2
        testProduct2 = new ProductEntity();
        testProduct2.setId(2L);
        testProduct2.setBarcode("BAR002");
        testProduct2.setName("Product 2");
        testProduct2.setMrp(200.0);

        // Setup test inventory 1
        testInventory1 = new InventoryEntity();
        testInventory1.setId(1L);
        testInventory1.setProduct(testProduct1);
        testInventory1.setQuantity(50L);

        // Setup test inventory 2
        testInventory2 = new InventoryEntity();
        testInventory2.setId(2L);
        testInventory2.setProduct(testProduct2);
        testInventory2.setQuantity(30L);

        // Setup test order items
        testOrderItem1 = new OrderItemEntity();
        testOrderItem1.setBarcode("BAR001");
        testOrderItem1.setQuantity(5L);
        testOrderItem1.setSellingPrice(110.0);

        testOrderItem2 = new OrderItemEntity();
        testOrderItem2.setBarcode("BAR002");
        testOrderItem2.setQuantity(3L);
        testOrderItem2.setSellingPrice(220.0);

        // Setup test order
        testOrder = new OrderEntity();
        testOrder.setId(1L);
        testOrder.setTotalAmount(1210.0);
    }

    @Test
    void testPlaceOrder_Success() {
        // Arrange
        List<OrderItemEntity> items = Arrays.asList(testOrderItem1, testOrderItem2);
        List<String> barcodes = Arrays.asList("BAR001", "BAR002");
        List<ProductEntity> products = Arrays.asList(testProduct1, testProduct2);
        List<Long> productIds = Arrays.asList(1L, 2L);
        List<InventoryEntity> inventories = Arrays.asList(testInventory1, testInventory2);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);
        when(orderApi.createOrder(1210.0)).thenReturn(testOrder);

        // Act
        OrderEntity result = orderFlow.placeOrder(items);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1210.0, result.getTotalAmount());

        // Verify API calls
        verify(productApi, times(1)).getAllWithoutPagination(barcodes);
        verify(inventoryApi, times(1)).getAllWithoutPagination(productIds);
        verify(orderApi, times(1)).createOrder(1210.0);
        verify(inventoryApi, times(1)).updateQuantity(1L, 45L); // 50 - 5
        verify(inventoryApi, times(1)).updateQuantity(2L, 27L); // 30 - 3
        verify(orderApi, times(1)).saveOrderItem(testOrderItem1, 1L, 1L);
        verify(orderApi, times(1)).saveOrderItem(testOrderItem2, 1L, 2L);
    }

    @Test
    void testPlaceOrder_SingleItem_Success() {
        // Arrange
        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.singletonList(testInventory1);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);
        when(orderApi.createOrder(550.0)).thenReturn(testOrder); // 5 * 110

        // Act
        OrderEntity result = orderFlow.placeOrder(items);

        // Assert
        assertNotNull(result);
        verify(orderApi, times(1)).createOrder(550.0);
        verify(inventoryApi, times(1)).updateQuantity(1L, 45L);
        verify(orderApi, times(1)).saveOrderItem(testOrderItem1, 1L, 1L);
    }

    @Test
    void testPlaceOrder_ProductNotFound_ThrowsException() {
        // Arrange
        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.emptyList(); // No products found

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> orderFlow.placeOrder(items));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Product not found for barcode: BAR001"));

        verify(productApi, times(1)).getAllWithoutPagination(barcodes);
        verify(orderApi, never()).createOrder(anyDouble());
        verify(inventoryApi, never()).updateQuantity(anyLong(), anyLong());
    }

    @Test
    void testPlaceOrder_SellingPriceBelowMRP_ThrowsException() {
        // Arrange
        testOrderItem1.setSellingPrice(90.0); // Below MRP of 100.0

        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.singletonList(testInventory1);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> orderFlow.placeOrder(items));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Selling price below MRP for barcode BAR001"));

        verify(orderApi, never()).createOrder(anyDouble());
    }

    @Test
    void testPlaceOrder_InventoryNotFound_ThrowsException() {
        // Arrange
        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.emptyList(); // No inventory found

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> orderFlow.placeOrder(items));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Inventory not found for product barcode: BAR001"));

        verify(orderApi, never()).createOrder(anyDouble());
        verify(inventoryApi, never()).updateQuantity(anyLong(), anyLong());
    }

    @Test
    void testPlaceOrder_InsufficientInventory_ThrowsException() {
        // Arrange
        testOrderItem1.setQuantity(100L); // More than available (50)

        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.singletonList(testInventory1);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> orderFlow.placeOrder(items));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Insufficient inventory for product: Product 1"));

        verify(orderApi, never()).createOrder(anyDouble());
        verify(inventoryApi, never()).updateQuantity(anyLong(), anyLong());
    }

    @Test
    void testPlaceOrder_ExactInventoryQuantity_Success() {
        // Arrange
        testOrderItem1.setQuantity(50L); // Exact inventory quantity

        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.singletonList(testInventory1);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);
        when(orderApi.createOrder(5500.0)).thenReturn(testOrder); // 50 * 110

        // Act
        OrderEntity result = orderFlow.placeOrder(items);

        // Assert
        assertNotNull(result);
        verify(inventoryApi, times(1)).updateQuantity(1L, 0L); // 50 - 50 = 0
        verify(orderApi, times(1)).saveOrderItem(testOrderItem1, 1L, 1L);
    }

    @Test
    void testPlaceOrder_SellingPriceEqualsToMRP_Success() {
        // Arrange
        testOrderItem1.setSellingPrice(100.0); // Equal to MRP

        List<OrderItemEntity> items = Collections.singletonList(testOrderItem1);
        List<String> barcodes = Collections.singletonList("BAR001");
        List<ProductEntity> products = Collections.singletonList(testProduct1);
        List<Long> productIds = Collections.singletonList(1L);
        List<InventoryEntity> inventories = Collections.singletonList(testInventory1);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);
        when(orderApi.createOrder(500.0)).thenReturn(testOrder); // 5 * 100

        // Act
        OrderEntity result = orderFlow.placeOrder(items);

        // Assert
        assertNotNull(result);
        verify(orderApi, times(1)).createOrder(500.0);
    }

    @Test
    void testPlaceOrder_MultipleItemsOneInvalid_ThrowsException() {
        // Arrange
        testOrderItem2.setSellingPrice(150.0); // Below MRP of 200.0

        List<OrderItemEntity> items = Arrays.asList(testOrderItem1, testOrderItem2);
        List<String> barcodes = Arrays.asList("BAR001", "BAR002");
        List<ProductEntity> products = Arrays.asList(testProduct1, testProduct2);
        List<Long> productIds = Arrays.asList(1L, 2L);
        List<InventoryEntity> inventories = Arrays.asList(testInventory1, testInventory2);

        when(productApi.getAllWithoutPagination(barcodes)).thenReturn(products);
        when(inventoryApi.getAllWithoutPagination(productIds)).thenReturn(inventories);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> orderFlow.placeOrder(items));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Selling price below MRP for barcode BAR002"));

        // Ensure no order was created or inventory updated
        verify(orderApi, never()).createOrder(anyDouble());
        verify(inventoryApi, never()).updateQuantity(anyLong(), anyLong());
    }

    @Test
    void testGetItemsByOrderId_Success() {
        // Arrange
        Long orderId = 1L;
        List<OrderItemEntity> orderItems = Arrays.asList(testOrderItem1, testOrderItem2);

        when(orderApi.findOrderItemsByOrderId(orderId)).thenReturn(orderItems);

        // Act
        List<OrderItemEntity> result = orderFlow.getItemsByOrderId(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderApi, times(1)).findOrderItemsByOrderId(orderId);
    }

    @Test
    void testGetItemsByOrderId_EmptyList() {
        // Arrange
        Long orderId = 999L;
        when(orderApi.findOrderItemsByOrderId(orderId)).thenReturn(Collections.emptyList());

        // Act
        List<OrderItemEntity> result = orderFlow.getItemsByOrderId(orderId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderApi, times(1)).findOrderItemsByOrderId(orderId);
    }
}
