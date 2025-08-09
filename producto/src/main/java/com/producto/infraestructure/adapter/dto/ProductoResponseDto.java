package com.producto.infraestructure.adapter.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoResponseDto {
    private Long id;
    private String nombre;
    private String inventario;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Integer vendidos;

}
