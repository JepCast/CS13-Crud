package edu.umg.domain.user;

public class UsuarioDTO {
    private String nombreUsuario;
    private String contraseña;

    // Constructor por defecto
    public UsuarioDTO() {
    }

    // Constructor con parámetros
    public UsuarioDTO(String nombreUsuario, String contraseña) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
    }

    // Métodos getter y setter
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    // Método toString para representación textual del objeto
    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", contraseña='" + contraseña + '\'' +
                '}';
    }
}

