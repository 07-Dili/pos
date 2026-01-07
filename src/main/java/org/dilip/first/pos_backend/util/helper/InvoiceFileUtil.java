package org.dilip.first.pos_backend.util.helper;

import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InvoiceFileUtil {

    private static final String BASE_DIR =
            System.getProperty("user.home") + "/pos-invoices";

    public static String generateInvoiceFile(Long orderId) {

        try {
            Path dirPath = Paths.get(BASE_DIR);
            Files.createDirectories(dirPath);

            String fileName = "invoice-" + orderId + ".txt";
            Path filePath = dirPath.resolve(fileName);

            String content = "INVOICE\n\nOrder ID: " + orderId;
            Files.write(filePath, content.getBytes());

            return filePath.toString();

        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR ," Failed to generate invoice file");
        }
    }

    public static byte[] readInvoice(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to read invoice file");
        }
    }
}
