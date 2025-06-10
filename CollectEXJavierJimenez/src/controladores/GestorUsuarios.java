package controladores;

import daos.UsuarioDAO;
import modelos.Usuario;

public class GestorUsuarios {
    private final UsuarioDAO dao = new UsuarioDAO();

    public boolean registrar(String nombre, String correo, String contrasenaHash) {
        if (nombre == null || nombre.isBlank()) return false;
        if (correo == null || correo.isBlank()) return false;
        if (contrasenaHash == null || contrasenaHash.isBlank()) return false;
        if (dao.obtenerPorCorreo(correo) != null) return false;
        return dao.insertarUsuario(new Usuario(0, nombre, correo, contrasenaHash));
    }

    public boolean login(String entrada, String hash) {
        return dao.autenticarPorNombreOCorreo(entrada, hash);
    }

    public boolean solicitarRecuperacion(String correo, String nuevoHash) {
        Usuario u = dao.obtenerPorCorreo(correo);
        if (u == null) return false;
        return dao.cambiarContrasena(u.getIdUsuario(), nuevoHash);
    }

    public boolean eliminarCuenta(int idUsuario) {
        if (idUsuario <= 0) return false;
        return dao.eliminarUsuario(idUsuario);
    }

    public Usuario obtenerPorCorreo(String correo) {
        return dao.obtenerPorCorreo(correo);
    }

    public Usuario obtenerPorEntrada(String entrada) {
        Usuario u = dao.obtenerPorCorreo(entrada);
        if (u != null) return u;
        return dao.obtenerPorNombre(entrada);
    }
    
    public Usuario obtenerPorId(int id) {
        return dao.obtenerPorId(id);         
    }

}
