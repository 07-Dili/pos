package org.dilip.first.pos_backend.model.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientForm {


    @NotBlank
    private String name;

    @Email
    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9._%+-]*@[a-zA-Z]+$\n",
            message = "Email must be in the format name@role.com"
    )
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

}
