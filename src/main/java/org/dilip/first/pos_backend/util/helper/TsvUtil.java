package org.dilip.first.pos_backend.util.helper;

import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TsvUtil {

    public static List<String[]> parse(MultipartFile file, int maxRows) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));

            List<String[]> rows = new ArrayList<>();
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                rows.add(line.split("\t"));

                if (rows.size() > maxRows) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Maximum 5000 rows are allowed");
                }
            }

            return rows;

        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
