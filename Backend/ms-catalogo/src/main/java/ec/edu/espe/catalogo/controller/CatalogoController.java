package ec.edu.espe.catalogo.controller;

import ec.edu.espe.catalogo.domain.PublicacionCatalogo;
import ec.edu.espe.catalogo.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
@CrossOrigin(origins = "*")
public class CatalogoController {
    
    @Autowired
    private CatalogoService catalogoService;
    
    @GetMapping("/publicaciones")
    public ResponseEntity<Page<PublicacionCatalogo>> buscarPublicaciones(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String autor,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<PublicacionCatalogo> resultado = catalogoService.buscarPublicaciones(
            busqueda, tipo, categoria, autor, pageable);
        
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/publicaciones/{id}")
    public ResponseEntity<PublicacionCatalogo> obtenerPublicacion(@PathVariable String id) {
        return catalogoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/publicaciones/tipo/{tipo}")
    public ResponseEntity<Page<PublicacionCatalogo>> obtenerPorTipo(
            @PathVariable String tipo,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<PublicacionCatalogo> resultado = catalogoService.obtenerPorTipo(tipo, pageable);
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> obtenerCategorias() {
        List<String> categorias = catalogoService.obtenerCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/autores")
    public ResponseEntity<List<String>> obtenerAutores() {
        List<String> autores = catalogoService.obtenerAutores();
        return ResponseEntity.ok(autores);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Catálogo Service is running");
    }
}
