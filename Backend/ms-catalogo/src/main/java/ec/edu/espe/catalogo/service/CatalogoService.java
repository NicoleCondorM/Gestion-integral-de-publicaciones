package ec.edu.espe.catalogo.service;

import ec.edu.espe.catalogo.domain.PublicacionCatalogo;
import ec.edu.espe.catalogo.repository.PublicacionCatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CatalogoService {
    
    @Autowired
    private PublicacionCatalogoRepository repository;
    
    public Page<PublicacionCatalogo> buscarPublicaciones(
            String busqueda, String tipo, String categoria, String autor, Pageable pageable) {
        
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            return repository.buscarPorTexto(busqueda.trim(), pageable);
        }
        
        if (autor != null && !autor.trim().isEmpty()) {
            return repository.findByAutoresContaining(autor.trim(), pageable);
        }
        
        if (categoria != null && !categoria.trim().isEmpty()) {
            return repository.findByCategoriasContaining(categoria.trim(), pageable);
        }
        
        if (tipo != null && !tipo.trim().isEmpty()) {
            return repository.findByEstadoAndTipo("PUBLICADO", tipo.trim(), pageable);
        }
        
        return repository.findByEstado("PUBLICADO", pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<PublicacionCatalogo> obtenerPorId(String id) {
        return repository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Page<PublicacionCatalogo> obtenerPublicadas(Pageable pageable) {
        return repository.findByEstado("PUBLICADO", pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<PublicacionCatalogo> obtenerPorTipo(String tipo, Pageable pageable) {
        return repository.findByEstadoAndTipo("PUBLICADO", tipo, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        return repository.findAllCategorias();
    }
    
    @Transactional(readOnly = true)
    public List<String> obtenerAutores() {
        return repository.findAllAutores();
    }
    
    public void actualizarPublicacion(PublicacionCatalogo publicacion) {
        publicacion.setFechaActualizacion(LocalDateTime.now());
        repository.save(publicacion);
    }
    
    public void eliminarPublicacion(String id) {
        repository.deleteById(id);
    }
}
