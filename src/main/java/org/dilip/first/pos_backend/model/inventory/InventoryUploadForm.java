package org.dilip.first.pos_backend.model.inventory;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class InventoryUploadForm {
    private MultipartFile file;
}
