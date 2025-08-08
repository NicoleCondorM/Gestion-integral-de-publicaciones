package publicaciones.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorDto {

    @NotBlank(message = "El nombre del autor es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido del autor es obligatorio")
    private String apellido;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La nacionalidad es campo obligatorio")
    private String nacionalidad;

    @NotBlank(message = "La institución es campo obligatorio")
    private String institucion;

    @NotBlank(message = "La biografia es campo obligatorio")
    private String biografia;

    @NotBlank(message = "El orcid es campo obligatorio")
    private String orcid;
}
