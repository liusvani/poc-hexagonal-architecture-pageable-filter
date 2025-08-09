package com.producto.application;


import com.producto.domain.model.Producto;
import com.producto.domain.port.ProductoRepository;
import com.producto.domain.specification.ProductoSpecification;
import com.producto.infraestructure.adapter.dto.ProductoFiltro;
import com.producto.infraestructure.adapter.handler.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }


    public Producto crearProducto(Producto producto){

        validarInventario(producto.getInventario());

        validarProductosVendidos(producto.getVendidos(), producto.getStock());

        LocalDate fechaActual = LocalDate.now();

        existNameByFechaCreacion(producto.getNombre(), fechaActual);

        return repository.save(producto);
    }
    public List<Producto> listarProductos() {
        return repository.findAll();
    }
    public Optional<Producto> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<Producto> buscarConFiltros(ProductoFiltro filtro) {
        System.out.println("Filtros recibidos: " + filtro);
        Specification<Producto> spec = ProductoSpecification.conFiltros(filtro);
        return repository.findAll(spec);
    }
    public boolean eliminarProducto(Long id) {
        Optional<Producto> producto = repository.findById(id);
        if (producto.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Page<Producto> listarConFiltros(Specification<Producto> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }
    private void validarInventario(String inventario) {
        if(repository.existByInventario(inventario)) {
            throw new BusinessException("El inventario '" + inventario + "' existe en la bd.");
        }
    }

    /*private void validarVentaProductoDiario(String nombre,
                                            LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaConvertida = fecha.format(formatter);
        if(repository.existNameByFechaCreacion(nombre,fechaConvertida)) {
            throw new BusinessException("El producto "+nombre+" ya existe registrado fecha "+fecha+" .");
        }
    }*/
    public void existNameByFechaCreacion(String nombre, LocalDate fecha) {
        LocalDateTime inicioDelDia = fecha.atStartOfDay();        // 2025-08-06T00:00:00
        LocalDateTime finDelDia = fecha.atTime(LocalTime.MAX);     // 2025-08-06T23:59:59.999999999
        boolean respuesta = repository.existsByNombreAndFechaCreacionBetween(nombre, inicioDelDia, finDelDia);
        if(respuesta){
            throw new BusinessException("EL producto ya existe registrado en la fecha actual.");
        }
    }

    private void validarProductosVendidos(Integer vendidos, Integer stock){
        try {
            int numero = Integer.parseInt(vendidos.toString());
        } catch (NumberFormatException e) {
            throw new BusinessException("Algunnos de los valores numericos.");
        }

        if(vendidos > stock && vendidos > 0 && stock > 0){
            throw new BusinessException("La cantidad de productos vendidos no pueden exceder al stock.");
        }
    }
}
