package org.dilip.first.pos_backend.util.conversion;

import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.model.products.ProductForm;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProductTsvParser{
    public static List<ProductForm> parse(MultipartFile file) {

        List<ProductForm> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] cols = line.split("\t");
                ProductForm form = new ProductForm();
                form.setClientId(Long.parseLong(cols[0]));
                form.setName(cols[1]);
                form.setBarcode(cols[2]);
                form.setMrp(Double.parseDouble(cols[3]));
                list.add(form);
                if (list.size() > 5000) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Maximum 5000 rows are allowed ");
                }
            }
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return list;
    }
}
