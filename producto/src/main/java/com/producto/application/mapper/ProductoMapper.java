package com.producto.application.mapper;

import com.producto.domain.model.Producto;
import com.producto.infraestructure.adapter.dto.ProductoRequestDto;
import com.producto.infraestructure.adapter.dto.ProductoResponseDto;

public class ProductoMapper {
   public static Producto toEntity(ProductoRequestDto dto){
       Producto producto = new Producto();

       producto.setNombre(dto.getNombre());
       producto.setDescripcion(dto.getDescripcion());
       producto.setPrecio(dto.getPrecio());
       producto.setStock(dto.getStock());
       producto.setVendidos(dto.getVendidos());
       producto.setInventario(dto.getInventario());

       return  producto;
   }
   public static ProductoResponseDto toDto(Producto producto){
       ProductoResponseDto dto = new ProductoResponseDto();
        dto.setId(producto.getId());
       dto.setNombre(producto.getNombre());
       dto.setDescripcion(producto.getDescripcion());
       dto.setPrecio(producto.getPrecio());
       dto.setStock(producto.getStock());
       dto.setVendidos(producto.getVendidos());
       dto.setInventario(producto.getInventario());

       return dto;
   }
}
