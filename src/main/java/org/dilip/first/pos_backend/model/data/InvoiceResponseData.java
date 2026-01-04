package org.dilip.first.pos_backend.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseData {

    private String base64Pdf;
}
