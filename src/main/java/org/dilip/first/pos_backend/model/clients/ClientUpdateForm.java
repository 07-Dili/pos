package org.dilip.first.pos_backend.model.clients;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateForm {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 256,message = "Maximum 256 characters is supported for name")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "PhoneNumber cannot be blank")
    @Pattern( regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;
}
