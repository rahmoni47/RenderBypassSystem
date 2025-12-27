package com.h80.demo.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EditRequestDTO {
    @NotBlank(message = "The email attribute can't be empty")
    @Email(message = "email format is wrong")
    private String email;
}
