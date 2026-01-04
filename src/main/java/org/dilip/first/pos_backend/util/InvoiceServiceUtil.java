package org.dilip.first.pos_backend.util;

import org.dilip.first.pos_backend.dto.InvoiceRequestDto;
import org.dilip.first.pos_backend.dto.InvoiceResponseDto;
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

    public String generateAndSavePdf(InvoiceRequestDto request) {
        try {
            String base64Pdf = callInvoiceApp(request);

            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);

            return savePdfToFile(request.getOrderId(), pdfBytes);

        } catch (Exception e) {
            throw new ApiException(500,"Failed to generate and save PDF: " + e.getMessage());
        }
    }

    private String callInvoiceApp(InvoiceRequestDto request) {
        try {
            String url = invoiceAppUrl + "/api/invoice/generate";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<InvoiceRequestDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<InvoiceResponseDto> response = restTemplate.exchange( url, HttpMethod.POST, entity, InvoiceResponseDto.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getBase64Pdf();
            } else {
                throw new ApiException(500,"Invalid response from invoice app");
            }

        } catch (Exception e) {
            throw new ApiException(500,"Failed to call invoice app: " + e.getMessage());
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
