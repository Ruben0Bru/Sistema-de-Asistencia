package Model;

/**
 *
 * @author Nemo
 */
public class UsuarioAdmin implements Usuario {
    
    private final String id;
    private final String nombre;
    private final String correo;
    private final String contraseña;
    
    public UsuarioAdmin (String id, String nombre, String correo, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    @Override
    public String getId(){
        return id;
   }

    @Override
    public String getNombre() {
        return nombre;
   }

    @Override
    public String getTipo() {
        return "Admin";
}

    @Override
    public String getCorreo() {
        return correo;
}

    @Override
    public String getContraseña() {
        return contraseña;
}
    
}
