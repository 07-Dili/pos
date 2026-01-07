package org.dilip.first.pos_backend.util.helper;

import org.dilip.first.pos_backend.model.form.InvoiceRequestForm;
import org.dilip.first.pos_backend.model.data.InvoiceResponseData;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class InvoiceServiceUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${invoice.app.url}")
    private String invoiceAppUrl;

    @Value("${invoice.pdf.storage.path}")
    private String storagePath;

    public String generateAndSavePdf(InvoiceRequestForm request) {
        try {
            String base64Pdf = callInvoiceApp(request);

            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);

            return savePdfToFile(request.getOrderId(), pdfBytes);

        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to generate and save PDF: " + e.getMessage());
        }
    }

    private String callInvoiceApp(InvoiceRequestForm request) {
        try {
            String url = invoiceAppUrl + "/api/invoice/generate";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<InvoiceRequestForm> entity = new HttpEntity<>(request, headers);

            ResponseEntity<InvoiceResponseData> response = restTemplate.exchange( url, HttpMethod.POST, entity, InvoiceResponseData.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getBase64Pdf();
            } else {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid response from invoice app");
            }

        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to call invoice app: " + e.getMessage());
        }
    }

    private String savePdfToFile(Long orderId, byte[] pdfBytes) throws IOException {
        Path dirPath = Paths.get(storagePath);
        Files.createDirectories(dirPath);

        String fileName = "invoice-" + orderId + ".pdf";
        Path filePath = dirPath.resolve(fileName);

        Files.write(filePath, pdfBytes);

        return filePath.toString();
    }
}
