package publicaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import publicaciones.dto.AutorDto;
import publicaciones.dto.ResponseDto;
import publicaciones.model.Autor;
import publicaciones.producer.NotificacionProducer;
import publicaciones.repository.AutorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private NotificacionProducer notificacionProducer;

    //create
    public ResponseDto crearAutor(AutorDto dto) {
        Autor autor = new Autor();
        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setEmail(dto.getEmail());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setInstitucion(dto.getInstitucion());
        autor.setOrcid(dto.getOrcid());

        Autor savedAutor = autorRepository.save(autor);

        notificacionProducer.enviarNotificacion("Se registro a: "+dto.getNombre()+ " "+dto.getApellido(),
                "NUEVO AUTOR");

        return ResponseDto.builder()
                .mensaje("Autor registrado exitosamente")
                .codigo("200")
                .dato(savedAutor)
                .build();
    }

    public List<ResponseDto> listarAutores() {
        return autorRepository.findAll().stream()
                .map(autor -> ResponseDto.builder()
                        .mensaje("Autor: " + autor.getApellido())
                        .codigo("200")
                        .dato(autor)
                        .build())
                .collect(Collectors.toList());
    }

    public List<Autor> autores() {
        return autorRepository.findAll();
    }

    public ResponseDto autorPorId(String id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el autor con id: " + id));
        return ResponseDto.builder()
                .mensaje("Autor con id " + autor.getId())
                .codigo("200")
                .dato(autor)
                .build();
    }

    public ResponseDto actualizarAutor(String id, AutorDto dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el autor con id: " + id));

        autor.setNombre(dto.getNombre());
        autor.setApellido(dto.getApellido());
        autor.setEmail(dto.getEmail());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setInstitucion(dto.getInstitucion());
        autor.setOrcid(dto.getOrcid());

        return ResponseDto.builder()
                .mensaje("Autor actualizado exitosamente")
                .codigo("200")
                .dato(autorRepository.save(autor))
                .build();
    }


}
