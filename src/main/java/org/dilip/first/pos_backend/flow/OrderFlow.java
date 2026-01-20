package org.dilip.first.pos_backend.flow;

import org.dilip.first.pos_backend.api.InventoryApi;
import org.dilip.first.pos_backend.api.OrderApi;
import org.dilip.first.pos_backend.api.ProductApi;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.OrderItemEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class OrderFlow {

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;



    public OrderEntity placeOrder(List<OrderItemEntity> items) {

        List<String> barcodes = items.stream().map(OrderItemEntity::getBarcode).collect(Collectors.toList());
        List<ProductEntity> products = productApi.getAllWithoutPagination(barcodes);

        List<Long> productIds = products.stream().map(ProductEntity::getId).collect(Collectors.toList());
        List<InventoryEntity> inventories = inventoryApi.getAllWithoutPagination(productIds);

        Map<String, ProductEntity> productMap = products.stream()
                .collect(Collectors.toMap(ProductEntity::getBarcode, p -> p));

        Map<Long, InventoryEntity> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(inv -> inv.getProduct().getId(), inv -> inv));

        double total = 0;

        for (OrderItemEntity item : items) {

            ProductEntity product = productMap.get(item.getBarcode());
            if (product == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Product not found for barcode: " + item.getBarcode());
            }

            if (item.getSellingPrice() < product.getMrp()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Selling price below MRP for barcode " + product.getBarcode());
            }

            InventoryEntity inventory = inventoryMap.get(product.getId());
            if (inventory == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Inventory not found for product barcode: " + item.getBarcode());
            }

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Insufficient inventory for product: " + product.getName());
            }

            total += item.getQuantity() * item.getSellingPrice();
        }

        OrderEntity order = orderApi.createOrder(total);

        for (OrderItemEntity item : items) {

            ProductEntity product = productMap.get(item.getBarcode());
            InventoryEntity inventory = inventoryMap.get(product.getId());
            inventoryApi.updateQuantity(product.getId(),inventory.getQuantity() - item.getQuantity());
            orderApi.saveOrderItem(item, order.getId(), product.getId());
        }

        return order;
    }


    public List<OrderItemEntity> getItemsByOrderId(Long orderId) {
        return orderApi.findOrderItemsByOrderId(orderId);
    }
}
