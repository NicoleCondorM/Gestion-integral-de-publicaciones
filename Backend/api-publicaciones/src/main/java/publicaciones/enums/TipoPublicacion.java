package publicaciones.enums;

public enum TipoPublicacion {
    ARTICULO("Artículo científico"),
    LIBRO("Libro");
    
    private final String descripcion;
    
    TipoPublicacion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
