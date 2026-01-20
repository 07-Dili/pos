package orders.unit;

import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.dao.OrderItemDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderApiTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @InjectMocks
    private OrderApi orderApi;

    private OrderEntity testOrder;
    private OrderItemEntity testOrderItem;

    @BeforeEach
    void setUp() {
        testOrder = new OrderEntity();
        testOrder.setId(1L);
        testOrder.setStatus(OrderStatus.CREATED);
        testOrder.setTotalAmount(1000.0);

        testOrderItem = new OrderItemEntity();
        testOrderItem.setId(1L);
        testOrderItem.setOrderId(1L);
        testOrderItem.setProductId(1L);
    }

    @Test
    void testGetById_Success() {
        // Arrange
        when(orderDao.findById(OrderEntity.class, 1L)).thenReturn(testOrder);

        // Act
        OrderEntity result = orderApi.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OrderStatus.CREATED, result.getStatus());
        verify(orderDao, times(1)).findById(OrderEntity.class, 1L);
    }

    @Test
    void testGetById_NotFound_ThrowsException() {
        // Arrange
        when(orderDao.findById(OrderEntity.class, 999L)).thenReturn(null);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class,
                () -> orderApi.getById(999L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Order not found with id: 999"));
        verify(orderDao, times(1)).findById(OrderEntity.class, 999L);
    }

    @Test
    void testGetAll_Success() {
        // Arrange
        List<OrderEntity> orders = Arrays.asList(testOrder, new OrderEntity());
        when(orderDao.getAll(0, 10)).thenReturn(orders);

        // Act
        List<OrderEntity> result = orderApi.getAll(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderDao, times(1)).getAll(0, 10);
    }

    @Test
    void testGetByStatus_Success() {
        // Arrange
        List<OrderEntity> orders = Arrays.asList(testOrder);
        when(orderDao.findByStatus(OrderStatus.CREATED, 0, 10)).thenReturn(orders);

        // Act
        List<OrderEntity> result = orderApi.getByStatus(OrderStatus.CREATED, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.CREATED, result.get(0).getStatus());
        verify(orderDao, times(1)).findByStatus(OrderStatus.CREATED, 0, 10);
    }

    @Test
    void testGetByDateRange_Success() {
        // Arrange
        OffsetDateTime from = OffsetDateTime.now().minusDays(7);
        OffsetDateTime to = OffsetDateTime.now();
        List<OrderEntity> orders = Arrays.asList(testOrder);
        when(orderDao.findByDateRange(from, to, 0, 10)).thenReturn(orders);

        // Act
        List<OrderEntity> result = orderApi.getByDateRange(from, to, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderDao, times(1)).findByDateRange(from, to, 0, 10);
    }

    @Test
    void testGetByDateRange_InvalidRange_ThrowsException() {
        // Arrange
        OffsetDateTime from = OffsetDateTime.now();
        OffsetDateTime to = OffsetDateTime.now().minusDays(1);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class,
                () -> orderApi.getByDateRange(from, to, 0, 10));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid date range", exception.getMessage());
        verify(orderDao, never()).findByDateRange(any(), any(), anyInt(), anyInt());
    }

    @Test
    void testCreateOrder_Success() {
        // Arrange
        Double totalAmount = 1500.0;
        when(orderDao.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity order = invocation.getArgument(0);
            order.setId(2L);
            return order;
        });

        // Act
        OrderEntity result = orderApi.createOrder(totalAmount);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CREATED, result.getStatus());
        assertEquals(totalAmount, result.getTotalAmount());
        verify(orderDao, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testChangeOrderStatusToInvoice_Success() {
        // Arrange
        when(orderDao.save(testOrder)).thenReturn(testOrder);

        // Act
        orderApi.changeOrderStatusToInvoice(testOrder);

        // Assert
        assertEquals(OrderStatus.INVOICED, testOrder.getStatus());
        verify(orderDao, times(1)).save(testOrder);
    }

    @Test
    void testSaveOrderItem_Success() {
        // Arrange
        Long orderId = 1L;
        Long productId = 100L;
        when(orderItemDao.save(testOrderItem)).thenReturn(testOrderItem);

        // Act
        orderApi.saveOrderItem(testOrderItem, orderId, productId);

        // Assert
        assertEquals(orderId, testOrderItem.getOrderId());
        assertEquals(productId, testOrderItem.getProductId());
        verify(orderItemDao, times(1)).save(testOrderItem);
    }

    @Test
    void testFindOrderItemsByOrderId_Success() {
        // Arrange
        List<OrderItemEntity> orderItems = Arrays.asList(testOrderItem);
        when(orderItemDao.findByOrderId(1L)).thenReturn(orderItems);

        // Act
        List<OrderItemEntity> result = orderApi.findOrderItemsByOrderId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderId());
        verify(orderItemDao, times(1)).findByOrderId(1L);
    }

    @Test
    void testFindOrderItemsByOrderId_EmptyList() {
        // Arrange
        when(orderItemDao.findByOrderId(999L)).thenReturn(Arrays.asList());

        // Act
        List<OrderItemEntity> result = orderApi.findOrderItemsByOrderId(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderItemDao, times(1)).findByOrderId(999L);
    }
}