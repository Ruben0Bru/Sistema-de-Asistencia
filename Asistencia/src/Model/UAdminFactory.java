package Model;

/**
 *
 * @author Nemo
 */
public class UAdminFactory extends UsuarioFactory{

    @Override
    public Usuario crearUsuario(String id, String nombre, String correo, String contraseña, String tipo) {
        return new UsuarioAdmin(id, nombre, correo, contraseña);
    }
    
}
