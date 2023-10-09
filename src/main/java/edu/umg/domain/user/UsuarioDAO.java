package edu.umg.domain.user;

import edu.umg.datos.Conexion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Sentencias SQL
    private static final String SQL_INSERT = "INSERT INTO usuario(username, password) VALUES(?, ?)";
    private static final String SQL_SELECT_BY_USER_PASS = "SELECT * FROM usuario WHERE username = ? AND password = ?";
    private static final String SQL_SELECT_BY_USER = "SELECT * FROM usuario WHERE username = ?";

    // Mensaje de notificación para la conexión exitosa
    private static final String SUCCESSFUL_CONNECTION_MESSAGE = "Conexión exitosa a la base de datos.";

    private Connection conexionTransaccional;

    public UsuarioDAO() {
    }

    // Constructor para utilizar una conexión transaccional
    public UsuarioDAO(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    // Método para insertar un usuario en la base de datos
    public boolean insertUsuario(UsuarioDTO usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, encriptarContraseña(usuario.getContraseña()));

            int rows = stmt.executeUpdate();
            return rows > 0;
        } finally {
            Conexion.close(stmt);
            cerrarConexion(conn);
        }
    }


    // Método para validar las credenciales de un usuario
    public boolean validarUsuario(String username, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean usuarioValido = false;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_USER_PASS);
            stmt.setString(1, username);
            stmt.setString(2, encriptarContraseña(password));
            rs = stmt.executeQuery();

            // Si se encuentra un usuario con las credenciales proporcionadas, es válido
            usuarioValido = rs.next();
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            cerrarConexion(conn);
        }

        return usuarioValido;
    }

    // Método para obtener un usuario por su nombre de usuario
    public UsuarioDTO obtenerUsuario(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsuarioDTO usuario = null;

        try {
            conn = obtenerConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_USER);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            // Si se encuentra un usuario, se crea un objeto UsuarioDTO con los datos
            if (rs.next()) {
                usuario = new UsuarioDTO();
                usuario.setNombreUsuario(rs.getString("username"));
                usuario.setContraseña(rs.getString("password"));
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            cerrarConexion(conn);
        }

        return usuario;
    }

    // Función para encriptar la contraseña de forma segura
    private String encriptarContraseña(String contraseña) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contraseña.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña.", e);
        }
    }

    // Método privado para obtener la conexión a la base de datos
    private Connection obtenerConexion() throws SQLException {
        Connection conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
        System.out.println(SUCCESSFUL_CONNECTION_MESSAGE); // Mensaje de notificación
        return conn;    }

    // Método privado para cerrar la conexión, solo si no se está utilizando una conexión transaccional
    private void cerrarConexion(Connection conn) throws SQLException {
        if (this.conexionTransaccional == null && conn != null) {
            conn.close();
        }
    }
}