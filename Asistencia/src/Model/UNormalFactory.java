package Model;

/**
 *
 * @author Nemo
 */
public class UNormalFactory extends UsuarioFactory{

    @Override
    public Usuario crearUsuario(String id, String nombre, String correo, String contraseña, String tipo) {
        return new UsuarioNormal(id, nombre, correo, contraseña);
   }
    
}
