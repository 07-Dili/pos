package org.dilip.first.pos_backend.model.clients;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientForm {

    @NotBlank(message = "Client name is required")
    @Size(max = 256,message = "Maximum 256 characters is supported for name")
    private String name;

    @NotBlank
    @Email(message = "Invalid email")
    @Size(max = 256,message = "Maximum 256 characters is supported for email")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;
}
