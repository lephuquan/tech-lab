package com.techlab.esproductsearch.web;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IndexProductRequest(
        String id,
        @NotBlank(message = "name khong duoc de trong") String name,
        @NotBlank(message = "description khong duoc de trong") String description,
        @NotBlank(message = "category khong duoc de trong") String category,
        @NotEmpty(message = "tags can it nhat 1 gia tri") List<String> tags,
        @NotNull(message = "price khong duoc null")
        @DecimalMin(value = "0.0", inclusive = false, message = "price phai lon hon 0")
        BigDecimal price,
        boolean inStock
) {
}
