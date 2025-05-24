/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Nemo
 */
public abstract class UsuarioFactory {
    
    
    public abstract Usuario crearUsuario(String id, String nombre, String correo, String contraseña, String tipo);
}
