/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Nemo
 */
public class FactoryProducer{
    
    
    public static UsuarioFactory getFactory(String tipo) 
    {
        if(tipo == null){
            throw new IllegalArgumentException("El tipo de usuario no puede ser nulo");
        }
        
        switch (tipo.toLowerCase()){
            case "admin":
                return new UAdminFactory();
            case "normal":
                return new UNormalFactory();
            default:
                throw new IllegalArgumentException("Tipo de usuario no valido: " + tipo);
        }
    }
}
