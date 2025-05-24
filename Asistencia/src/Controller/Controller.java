package Controller;

import View.*;
/*Eventos*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;
import javax.swing.JOptionPane;
/* Base de datos*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/*
 *
 * @author Nemo
 */
public class Controller implements ActionListener /*FocusListener*/{
    
    /*VISTAS*/
    private String correoUsuarioLogueado;
    LoginView lv = new LoginView();
    AddKeyWordView ak = new AddKeyWordView();
    AttendanceView av = new AttendanceView();
    ListView listV = new ListView();
    SingUpView suv = new SingUpView();
    
    public Controller()
    {
        /*Adding Listeners*/
        ak.getCrearClaveButton().addActionListener((ActionListener) this);
        ak.getCrearClaveButton().setActionCommand("CrearClave");
        ak.getVerListaButton().addActionListener(this);
        ak.getVerListaButton().setActionCommand("VerLista");
        
        av.getRegistrarAsistenciaButton().addActionListener((ActionListener) this);
        av.getRegistrarAsistenciaButton().setActionCommand("RegistrarAsistencia");
        
        listV.getRegresarButton().addActionListener((ActionListener) this);
        listV.getRegresarButton().setActionCommand("Regresar");
        listV.getFiltrarButton().addActionListener((ActionListener) this);
        listV.getFiltrarButton().setActionCommand("Filtrar");
        
        lv.getIngresarButton().addActionListener((ActionListener) this);
        lv.getIngresarButton().setActionCommand("Ingresar");
        lv.getRegistrarButton().addActionListener((ActionListener) this);
        lv.getRegistrarButton().setActionCommand("RegistrarUsuario");
        
        suv.getRegistrarButton().addActionListener((ActionListener) this);
        suv.getRegistrarButton().setActionCommand("Registrar");
        
        /*
        lv.getCorreoField().addFocusListener((FocusListener) this);
        lv.getCorreoField().setActionCommand("Correo");
        lv.getContraseñaField().addFocusListener((FocusListener) this);
        lv.getContraseñaField().setActionCommand("Contraseñ");
        */
    }
    
    
    public void Run()
    {
        lv.setVisible(true);
        lv.setTitle("Login");
        lv.setLocationRelativeTo(null);
        lv.setResizable(false);
    }

    /*Configurando eventos de los botones*/
    @Override 
    public void actionPerformed(ActionEvent e) { 
        /* Se agregaron los casos para cada una de las funciones, 
        solamente faltaria crear la base de datos, agregar un admin y organizar el codigo.*/

        switch (e.getActionCommand()) {

            case "Ingresar":
                String correo = lv.getCorreoField().getText();
                String contraseña = new String(lv.getContraseñaField().getPassword());

                try (Connection con = ConexionDB.getConnection()) {
                    String query = "SELECT rol FROM usuarios WHERE correo = ? AND contraseña = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, correo);
                    ps.setString(2, contraseña);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        this.correoUsuarioLogueado = correo;
                        String rol = rs.getString("rol");
                        JOptionPane.showMessageDialog(null, "¡Sesión iniciada!");
                        lv.dispose();

                        if ("admin".equals(rol)) {
                            ak.setVisible(true);
                            ak.setLocationRelativeTo(null);
                        } else {
                            av.setVisible(true);
                            av.setLocationRelativeTo(null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
                }
                break;
            case "RegistrarUsuario":
                lv.dispose();
                suv.setVisible(true);
                suv.setLocationRelativeTo(null);
                break;
            case "Registrar":
                String nombre = suv.getNombreField().getText();
                correo = suv.getCorreoField().getText();
                contraseña = new String(suv.getContraseñaField().getPassword());
                String rol = "normal";

                if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos");
                    return;
                }

                try (Connection con = ConexionDB.getConnection()) {
                    String query = "INSERT INTO usuarios (nombre, correo, contraseña, rol) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, nombre);
                    ps.setString(2, correo);
                    ps.setString(3, contraseña);
                    ps.setString(4, rol);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Registro exitoso");
                    suv.dispose();
                    lv.setVisible(true);
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1062) {
                        JOptionPane.showMessageDialog(null, "El correo ya está registrado");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                }
                break;
            case "CrearClave":
                String nombreEvento = ak.getNombreEventoField().getText();
                String claveEvento = ak.getClaveField().getText();

                if (nombreEvento.isEmpty() || claveEvento.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Rellena ambos campos");
                    return;
                }

                try (Connection con = ConexionDB.getConnection()) {
                    // Verificar clave única
                    String checkQuery = "SELECT clave_evento FROM eventos WHERE clave_evento = ?";
                    PreparedStatement checkPs = con.prepareStatement(checkQuery);
                    checkPs.setString(1, claveEvento);
                    ResultSet rs = checkPs.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "La clave ya está en uso");
                    } else {
                        // Insertar evento
                        String insertQuery = "INSERT INTO eventos (nombre_evento, clave_evento) VALUES (?, ?)";
                        PreparedStatement insertPs = con.prepareStatement(insertQuery);
                        insertPs.setString(1, nombreEvento);
                        insertPs.setString(2, claveEvento);
                        insertPs.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Evento creado");
                        ak.dispose();
                        listV.setVisible(true);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
                break;
            case "RegistrarAsistencia":
                if (this.correoUsuarioLogueado == null) {
                    JOptionPane.showMessageDialog(null, "❌ No hay un usuario logueado");
                    return;
                }
                String eventoSeleccionado = av.getEventoComboBox().getSelectedItem().toString();
                String claveIngresada = av.getClaveField().getText();

                try (Connection con = ConexionDB.getConnection()) {
                    // Verificar clave del evento
                    String queryClave = "SELECT clave_evento FROM eventos WHERE nombre_evento = ?";
                    PreparedStatement psClave = con.prepareStatement(queryClave);
                    psClave.setString(1, eventoSeleccionado);
                    ResultSet rsClave = psClave.executeQuery();

                    if (rsClave.next()) {
                        String claveCorrecta = rsClave.getString("clave_evento");

                        if (claveIngresada.equals(claveCorrecta)) {
                            // Registrar asistencia usando la variable de instancia
                            String insertQuery = "INSERT INTO asistencias (correo_usuario, clave_evento) VALUES (?, ?)";
                            PreparedStatement insertPs = con.prepareStatement(insertQuery);
                            insertPs.setString(1, this.correoUsuarioLogueado); // Usar la variable de instancia
                            insertPs.setString(2, claveCorrecta);
                            insertPs.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Asistencia registrada");
                            //av.dispose();
                            System.exit(0);
                        } else {
                            JOptionPane.showMessageDialog(null, "Clave incorrecta");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
                break;
                
            case "Regresar":
                listV.dispose();
                ak.setVisible(true);
                ak.setLocationRelativeTo(null);
                break; 
                
            case "VerLista":
                ak.dispose();
                listV.setVisible(true);
                listV.setLocationRelativeTo(null);
                break;
                
            case "Filtrar":
                String criterio = listV.getNombreField().getText();
                String tipoFiltro = listV.getEventoComboBox().getSelectedItem().toString();
                DefaultTableModel model = (DefaultTableModel) listV.getAsistentesTable().getModel();
                model.setRowCount(0);

                try (Connection con = ConexionDB.getConnection()) {
                    String query = tipoFiltro.equals("Nombre")
                            ? "SELECT u.nombre, e.nombre_evento, u.rol FROM asistencias a "
                            + "JOIN usuarios u ON a.correo_usuario = u.correo "
                            + "JOIN eventos e ON a.clave_evento = e.clave_evento "
                            + "WHERE u.nombre LIKE ?"
                            : "SELECT u.nombre, e.nombre_evento, u.rol FROM asistencias a "
                            + "JOIN usuarios u ON a.correo_usuario = u.correo "
                            + "JOIN eventos e ON a.clave_evento = e.clave_evento "
                            + "WHERE e.nombre_evento LIKE ?";

                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, "%" + criterio + "%");
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("nombre"),
                            rs.getString("nombre_evento"),
                            rs.getString("rol")
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al filtrar: " + ex.getMessage());
                }
                break;

            default:
                throw new AssertionError();
        }

    }
    /*
        @Override
    public void focusGained(FocusEvent e) {
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        
    }
    */
    
}
