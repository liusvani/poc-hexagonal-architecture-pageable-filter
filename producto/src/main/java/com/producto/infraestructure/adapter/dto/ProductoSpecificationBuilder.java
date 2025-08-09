package com.producto.infraestructure.adapter.dto;


import com.producto.domain.model.Producto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;


public class ProductoSpecificationBuilder {
    public static Specification<Producto> build(ProductoFiltro filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNombre() != null) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + filtro.getNombre().toLowerCase() + "%"));
            }
            if (filtro.getInventario() != null) {
                predicates.add(cb.equal(root.get("inventario"), filtro.getInventario()));
            }
            if (filtro.getPrecioMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), filtro.getPrecioMin()));
            }
            if (filtro.getPrecioMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), filtro.getPrecioMax()));
            }
            if (filtro.getStockMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), filtro.getStockMin()));
            }
            if (filtro.getStockMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stock"), filtro.getStockMax()));
            }
            if (filtro.getFechaCreacionDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaCreacion"), filtro.getFechaCreacionDesde().atStartOfDay()));
            }

            if (filtro.getFechaCreacionHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaCreacion"), filtro.getFechaCreacionHasta().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

