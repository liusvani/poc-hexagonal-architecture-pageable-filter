package com.producto.infraestructure.adapter.web;

import com.producto.application.ProductoService;
import com.producto.application.mapper.ProductoMapper;
import com.producto.domain.model.Producto;
import com.producto.infraestructure.adapter.dto.ProductoFiltro;
import com.producto.infraestructure.adapter.dto.ProductoRequestDto;
import com.producto.infraestructure.adapter.dto.ProductoResponseDto;
import com.producto.infraestructure.adapter.dto.ProductoSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequestDto dto) {
        try {

            Map<String, Object> response = new HashMap<>();
            Producto producto = ProductoMapper.toEntity(dto);
            Producto creado = (Producto) service.crearProducto(producto);
            response.put("data", ProductoMapper.toDto(creado));

            response.put("message", "Producto creado exitosamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el producto", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            boolean eliminado = service.eliminarProducto(id);

            if (eliminado) {
                return ResponseEntity.ok(Map.of("message", "Producto eliminado exitosamente."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Producto no encontrado con ID: " + id));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar el producto", "details", e.getMessage()));
        }
    }


    /*@GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDto>> buscarProductos(@ModelAttribute ProductoFiltro filtro) {
        List<Producto> productos = service.buscarConFiltros(filtro);
        List<ProductoResponseDto> dtos = productos.stream()
                .map(ProductoMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }*/

    @GetMapping
    public ResponseEntity<?> listarProductos(@ModelAttribute ProductoFiltro filtro) {
        Sort sort = Sort.by(Sort.Direction.fromString(filtro.getDirection()), filtro.getSortBy());
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), sort);

        Specification<Producto> spec = ProductoSpecificationBuilder.build(filtro);

        Page<Producto> pagina = service.listarConFiltros(spec, pageable);

        List<ProductoResponseDto> productos = pagina.getContent().stream()
                .map(ProductoMapper::toDto)
                .toList();

        Map<String, Object> response = Map.of(
                "data", productos,
                "totalElements", pagina.getTotalElements(),
                "totalPages", pagina.getTotalPages(),
                "currentPage", pagina.getNumber()
        );
    //GET /api/productos?nombre=mouse&precioMin=10&precioMax=100&sortBy=precio&direction=desc&page=0&size=5
        return ResponseEntity.ok(response);
    }


    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProductos(@ModelAttribute ProductoFiltro filtro) {
        try {
            List<Producto> productos = service.buscarConFiltros(filtro);

            if (productos.isEmpty()) {
                Map<String, String> mensaje = new HashMap<>();
                mensaje.put("mensaje", "No se encontraron productos con los filtros proporcionados.");
                return ResponseEntity.ok(mensaje);
            }

            List<ProductoResponseDto> dtos = productos.stream()
                    .map(ProductoMapper::toDto)
                    .toList();

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al buscar los productos.");
            error.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



}
