package com.producto.domain.port;

import com.producto.domain.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    Producto save(Producto producto);
    Optional<Producto> findById(Long id);
    List<Producto> findAll();
    List<Producto> findAll(Specification<Producto> spec);
    Page<Producto> findAll(Specification<Producto> spec, Pageable pageable); // Paginacion y filtro de busqueda
    void deleteById(Long id);
    boolean existByInventario(String inventario);
    boolean existsByNombreAndFechaCreacionBetween(String nombre, LocalDateTime inicio, LocalDateTime fin);
}
