package com.producto.infraestructure.adapter.persistence;

import com.producto.domain.model.Producto;
import com.producto.domain.port.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ProductoRepositoryAdapter implements ProductoRepository {
    private final JpaProductoRepository jpaRepository;

    public ProductoRepositoryAdapter(JpaProductoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Producto> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Producto> findAll(Specification<Producto> spec) {
        return jpaRepository.findAll(spec);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jpaRepository.findById(id);
    }
    @Override
    public Producto save(Producto producto) {
        return jpaRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Producto> findAll(Specification<Producto> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existByInventario(String inventario) {
        return jpaRepository.existsByInventario(inventario);
    }

    @Override
    public boolean existsByNombreAndFechaCreacionBetween(String nombre, LocalDateTime inicio, LocalDateTime fin) {
        return jpaRepository.existsByNombreAndFechaCreacionBetween(nombre, inicio, fin);
    }




}
