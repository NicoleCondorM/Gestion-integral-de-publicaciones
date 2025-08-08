package publicaciones.enums;

/**
 * Enumeración que define los estados del ciclo de vida de una publicación
 * BORRADOR → EN_REVISION → CAMBIOS_SOLICITADOS → APROBADO → PUBLICADO → RETIRADO
 */
public enum EstadoPublicacion {
    BORRADOR("Borrador"),
    EN_REVISION("En Revisión"),
    CAMBIOS_SOLICITADOS("Cambios Solicitados"),
    APROBADO("Aprobado"),
    PUBLICADO("Publicado"),
    RETIRADO("Retirado");

    private final String descripcion;

    EstadoPublicacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Valida si es posible transicionar de un estado a otro
     */
    public boolean puedeTransicionarA(EstadoPublicacion nuevoEstado) {
        return switch (this) {
            case BORRADOR -> nuevoEstado == EN_REVISION;
            case EN_REVISION -> nuevoEstado == CAMBIOS_SOLICITADOS || nuevoEstado == APROBADO;
            case CAMBIOS_SOLICITADOS -> nuevoEstado == EN_REVISION;
            case APROBADO -> nuevoEstado == PUBLICADO;
            case PUBLICADO -> nuevoEstado == RETIRADO;
            case RETIRADO -> false; // Estado final
        };
    }
}
