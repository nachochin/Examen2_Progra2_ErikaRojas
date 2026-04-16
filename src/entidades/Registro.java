/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ekaro
 */
public class Registro {

    public static final DateTimeFormatter FORMATO
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int id;
    private Vehiculo vehiculo;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private double montoTotal;

    public Registro(int id, Vehiculo vehiculo, LocalDateTime horaEntrada) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = null;
        this.montoTotal = 0.0;
    }

    public int getId() {
        return id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVehiculo(Vehiculo v) {
        this.vehiculo = v;
    }

    public void setHoraEntrada(LocalDateTime h) {
        this.horaEntrada = h;
    }

    public void setHoraSalida(LocalDateTime h) {
        this.horaSalida = h;
    }

    public void setMontoTotal(double m) {
        this.montoTotal = m;
    }

    public boolean estaActivo() {
        return horaSalida == null;
    }

    public String getHoraEntradaStr() {
        return horaEntrada != null ? horaEntrada.format(FORMATO) : "";
    }

    public String getHoraSalidaStr() {
        return horaSalida != null ? horaSalida.format(FORMATO) : "—";
    }
}
