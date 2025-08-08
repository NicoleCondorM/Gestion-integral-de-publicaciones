package ec.edu.espe.auth.model;

public enum Role {
    ROLE_AUTOR,      // Crea y actualiza borradores, responde solicitudes de cambio
    ROLE_REVISOR,    // Evalúa, comenta y emite recomendaciones (aceptar, solicitar cambios, rechazar)
    ROLE_EDITOR,     // Decide aprobación final, fuerza estados especiales
    ROLE_ADMIN,      // Administrador del sistema con todos los permisos
    ROLE_LECTOR      // Accede al catálogo publicado y consulta metadatos
}
