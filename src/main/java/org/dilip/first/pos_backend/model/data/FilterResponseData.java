package org.dilip.first.pos_backend.model.data;

public interface FilterResponseData {

    Long getProductId();
    Long getClientId();
    String getProductName();
    String getBarcode();
    Double getMrp();
    Long getQuantity();
}
