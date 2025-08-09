package com.producto.domain.specification;

import com.producto.domain.model.Producto;
import com.producto.infraestructure.adapter.dto.ProductoFiltro;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
@Component
public class ProductoSpecification {

    public static Specification<Producto> conFiltros(ProductoFiltro filtro) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filtro.getNombre() != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("nombre")), "%" + filtro.getNombre().toLowerCase() + "%"));
            }

            if (filtro.getInventario() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("inventario"), filtro.getInventario()));
            }

            if (filtro.getPrecioMin() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("precio"), filtro.getPrecioMin()));
            }

            if (filtro.getPrecioMax() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("precio"), filtro.getPrecioMax()));
            }

            if (filtro.getStockMin() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("stock"), filtro.getStockMin()));
            }

            if (filtro.getStockMax() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("stock"), filtro.getStockMax()));
            }

            if (filtro.getFechaCreacionDesde() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fechaCreacion"), filtro.getFechaCreacionDesde().atStartOfDay()));
            }

            if (filtro.getFechaCreacionHasta() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fechaCreacion"), filtro.getFechaCreacionHasta().atTime(LocalTime.MAX)));
            }
            System.out.println("Filtros recibidos: " + filtro);
            return predicate;
        };
    }
}

