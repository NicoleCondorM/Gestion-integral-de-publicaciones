package publicaciones.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Imports específicos para los DTOs
import publicaciones.dto.LibroDto;
import publicaciones.dto.ResponseDto;
import publicaciones.dto.PublicacionResponseDto;
import publicaciones.enums.EstadoPublicacion;
import publicaciones.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<ResponseDto> crearLibro(@RequestBody LibroDto libroDto) {
        try {
            ResponseDto response = libroService.crearLibro(libroDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .mensaje("Error al crear libro: " + e.getMessage())
                            .codigo("400")
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<List<PublicacionResponseDto>> obtenerTodos() {
        List<PublicacionResponseDto> libros = libroService.obtenerTodos();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponseDto> obtenerPorId(@PathVariable String id) {
        try {
            PublicacionResponseDto libro = libroService.obtenerPorId(id);
            return ResponseEntity.ok(libro);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> actualizarLibro(@PathVariable String id, @RequestBody LibroDto libroDto) {
        try {
            ResponseDto response = libroService.actualizarLibro(id, libroDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .mensaje("Error al actualizar libro: " + e.getMessage())
                            .codigo("400")
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> eliminarLibro(@PathVariable String id) {
        try {
            ResponseDto response = libroService.eliminarLibro(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .mensaje("Error al eliminar libro: " + e.getMessage())
                            .codigo("400")
                            .build());
        }
    }

    @GetMapping("/buscar/titulo/{titulo}")
    public ResponseEntity<List<PublicacionResponseDto>> buscarPorTitulo(@PathVariable String titulo) {
        List<PublicacionResponseDto> libros = libroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/buscar/autor/{autorId}")
    public ResponseEntity<List<PublicacionResponseDto>> buscarPorAutor(@PathVariable String autorId) {
        List<PublicacionResponseDto> libros = libroService.buscarPorAutor(autorId);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/buscar/genero/{genero}")
    public ResponseEntity<List<PublicacionResponseDto>> buscarPorGenero(@PathVariable String genero) {
        List<PublicacionResponseDto> libros = libroService.buscarPorGenero(genero);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<List<PublicacionResponseDto>> buscarPorEstado(@PathVariable String estado) {
        try {
            EstadoPublicacion estadoEnum = EstadoPublicacion.valueOf(estado.toUpperCase());
            List<PublicacionResponseDto> libros = libroService.buscarPorEstado(estadoEnum);
            return ResponseEntity.ok(libros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Libros API is running");
    }
}
