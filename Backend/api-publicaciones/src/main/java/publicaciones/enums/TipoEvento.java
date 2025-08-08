package publicaciones.enums;

/**
 * Enumeración para tipos de eventos de dominio en publicaciones
 */
public enum TipoEvento {
    PUBLICATION_SUBMITTED("Publicación enviada para revisión"),
    REVIEW_REQUESTED("Revisión solicitada"),
    PUBLICATION_APPROVED("Publicación aprobada"),
    PUBLICATION_PUBLISHED("Publicación publicada"),
    PUBLICATION_READY_FOR_CATALOG("Publicación lista para catálogo"),
    CHANGES_REQUESTED("Cambios solicitados"),
    PUBLICATION_WITHDRAWN("Publicación retirada");

    private final String descripcion;

    TipoEvento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
