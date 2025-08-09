package com.producto.infraestructure.adapter.persistence;

import com.producto.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface JpaProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    boolean existsByInventario(String inventario);
    boolean existsByNombreAndFechaCreacionBetween(String nombre, LocalDateTime inicio, LocalDateTime fin);

}
