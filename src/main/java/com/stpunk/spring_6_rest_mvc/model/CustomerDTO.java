package com.stpunk.spring_6_rest_mvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class CustomerDTO {

    private UUID customerId;

    @NotNull
    @NotBlank
    private String customerName;

    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
