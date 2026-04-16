/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author ekaro
 */
public class Vehiculo {

    public enum TipoVehiculo {
        CARRO, MOTO;

        @Override
        public String toString() {
            switch (this) {
                case CARRO:
                    return "Carro";
                case MOTO:
                    return "Moto";
                default:
                    return name();
            }
        }
    }

    private String placa;
    private TipoVehiculo tipo;

    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setTipo(TipoVehiculo t) {
        this.tipo = t;
    }

    @Override
    public String toString() {
        return placa + " (" + tipo + ")";
    }
}
