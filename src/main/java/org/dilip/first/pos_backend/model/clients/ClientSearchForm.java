package org.dilip.first.pos_backend.model.clients;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientSearchForm {

    private Long id;
    private String name;
    private String email;
    private Integer page=0;
    private Integer size=10;
}
