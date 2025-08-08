package publicaciones.enums;

public enum EstadoRevision {
    PENDIENTE("Pendiente de asignación"),
    EN_PROCESO("En proceso de revisión"),
    COMPLETADA("Revisión completada"),
    DEVUELTA("Devuelta para corrección"),
    ACEPTADA("Aceptada por el editor"),
    RECHAZADA("Rechazada");
    
    private final String descripcion;
    
    EstadoRevision(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
