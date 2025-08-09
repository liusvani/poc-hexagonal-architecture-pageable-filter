package com.producto.infraestructure.adapter.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class ProductoFiltro {
    private String nombre;
    private String inventario;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private Integer stockMin;
    private Integer stockMax;
    private LocalDate fechaCreacionDesde;
    private LocalDate fechaCreacionHasta;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String direction = "asc";
}

