package com.auth.Dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO extends RepresentationModel<LoginResponseDTO> {
    private boolean success;
    private String message;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
