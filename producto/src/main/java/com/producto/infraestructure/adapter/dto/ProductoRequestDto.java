package com.producto.infraestructure.adapter.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El inventario no puede estar vacío.")
    @Size(max = 100, message = "El inventario no puede tener más de 100 caracteres.")
    private String inventario;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres.")
    private String descripcion;


    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0.")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener hasta 10 dígitos enteros y 2 decimales.")
    private BigDecimal precio;


    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "La cantidad vendida es obligatoria.")
    @Min(value = 0, message = "La cantidad vendida no puede ser negativa.")
    private Integer vendidos;

    /*
    @AssertTrue(message = "La cantidad vendida no puede exceder el stock.")
    public boolean isVendidosMenorOIgualStock() {
        if (vendidos == null || stock == null) return true; // Evita doble validación
        return vendidos <= stock;
    }*/

}

