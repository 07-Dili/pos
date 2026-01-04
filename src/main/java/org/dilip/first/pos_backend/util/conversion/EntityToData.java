package org.dilip.first.pos_backend.util.conversion;

import org.dilip.first.pos_backend.entity.ClientEntity;
import org.dilip.first.pos_backend.entity.InventoryEntity;
import org.dilip.first.pos_backend.entity.OrderEntity;
import org.dilip.first.pos_backend.entity.ProductEntity;
import org.dilip.first.pos_backend.model.data.*;

public class EntityToData {
    public static ClientData convertClientEntityToData(ClientEntity entity) {
        ClientData data = new ClientData();
        data.setId(entity.getId());
        data.setName(entity.getName());
        data.setEmail(entity.getEmail());
        data.setPhone(entity.getPhone());
        return data;
    }
    public static OrderData convertOrderEntityToOrderData(OrderEntity order) {

        OrderData data = new OrderData();
        data.setId(order.getId());
        data.setStatus(order.getStatus());
        data.setTotalAmount(order.getTotalAmount());
        data.setCreatedAt(order.getCreatedAt());
        return data;
    }
    public static ProductInventoryData convertInventoryEntityToCompleteProductData(InventoryEntity inventory) {

        ProductInventoryData data = new ProductInventoryData();
        data.setProductId(inventory.getProduct().getId());
        data.setClientId(inventory.getProduct().getClientId());
        data.setProductName(inventory.getProduct().getName());
        data.setBarcode(inventory.getProduct().getBarcode());
        data.setMrp(inventory.getProduct().getMrp());
        data.setQuantity(inventory.getQuantity());
        return data;
    }

    public static ProductData convertProductEntityToData(ProductEntity entity) {
        ProductData data = new ProductData();
        data.setId(entity.getId());
        data.setClientId(entity.getClientId());
        data.setName(entity.getName());
        data.setBarcode(entity.getBarcode());
        data.setMrp(entity.getMrp());
        return data;
    }

    public static InventoryData convertInventoryEntityToData(InventoryEntity entity) {
        InventoryData data = new InventoryData();
        data.setProductId(entity.getProduct().getId());
        data.setQuantity(entity.getQuantity());
        return data;
    }
}
