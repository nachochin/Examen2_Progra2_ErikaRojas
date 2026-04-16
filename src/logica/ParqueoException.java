/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author ekaro
 */
public class ParqueoException extends Exception {

    public ParqueoException(String mensaje) {
        super(mensaje);
    }

    public ParqueoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
