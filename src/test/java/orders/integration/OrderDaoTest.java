package orders.integration;

import org.dilip.first.pos_backend.constants.OrderStatus;
import org.dilip.first.pos_backend.dao.OrderDao;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = org.dilip.first.pos_backend.PosBackendApplication.class)
@Transactional
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        em.createQuery("DELETE FROM OrderEntity").executeUpdate();
        em.flush();
    }

    @Test
    public void testFindByStatus_Success() {
        OrderEntity order1 = new OrderEntity();
        order1.setStatus(OrderStatus.CREATED);
        order1.setCreatedAt(OffsetDateTime.now());
        order1.setTotalAmount(100.0);
        orderDao.save(order1);

        OrderEntity order2 = new OrderEntity();
        order2.setStatus(OrderStatus.INVOICED);
        order2.setCreatedAt(OffsetDateTime.now());
        order2.setTotalAmount(100.0);
        orderDao.save(order2);

        em.flush();

        List<OrderEntity> result = orderDao.findByStatus(OrderStatus.CREATED, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(OrderStatus.CREATED, result.get(0).getStatus());
    }

    @Test
    public void testFindByStatus_NoResults() {
        List<OrderEntity> result = orderDao.findByStatus(OrderStatus.CREATED, 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByDateRange_Success() {
        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(100.0);

        orderDao.save(order);
        em.flush();

        OffsetDateTime from = OffsetDateTime.now().minusMinutes(1);
        OffsetDateTime to   = OffsetDateTime.now().plusMinutes(1);

        List<OrderEntity> result =
                orderDao.findByDateRange(from, to, 0, 10);

        assertEquals(1, result.size());
    }


    @Test
    public void testGetAll_WithPagination() {
        for (int i = 0; i < 5; i++) {
            OrderEntity order = new OrderEntity();
            order.setStatus(OrderStatus.CREATED);
            order.setCreatedAt(OffsetDateTime.now());
            order.setTotalAmount(100.0);
            orderDao.save(order);
        }
        em.flush();

        List<OrderEntity> page1 = orderDao.getAll(0, 2);
        List<OrderEntity> page2 = orderDao.getAll(1, 2);

        assertEquals(2, page1.size());
        assertEquals(2, page2.size());
        assertNotEquals(page1.get(0).getId(), page2.get(0).getId());
    }

    @Test
    public void testFindByStatus_Failure() {

        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(100.0);
        orderDao.save(order);

        em.flush();

        List<OrderEntity> result = orderDao.findByStatus(OrderStatus.INVOICED, 0, 10);

        assertEquals(0, result.size());
    }

}
