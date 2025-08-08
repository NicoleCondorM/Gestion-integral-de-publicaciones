package publicaciones.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import publicaciones.dto.AutorDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Autor;
import publicaciones.service.AutorService;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseDto crearAutor(@RequestBody @Valid AutorDto dto) {
        return autorService.crearAutor(dto);
    }

    @GetMapping
    public List<ResponseDto> obtenerAutores() {
        return autorService.listarAutores();
    }

    @GetMapping("/todos")
    public List<Autor> listarrAutores() {
        return autorService.autores();
    }

    @GetMapping("/{id}")
    public ResponseDto buscarPorId(@PathVariable String id) {
        return autorService.autorPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseDto actualizarAutor(@PathVariable String id, @RequestBody @Valid AutorDto dto) {
        return autorService.actualizarAutor(id, dto);
    }
}
