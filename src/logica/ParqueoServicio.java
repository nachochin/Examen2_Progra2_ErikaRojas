/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import accesodatos.RegistroDAO;
import entidades.Registro;
import entidades.Vehiculo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ekaro
 */
public class ParqueoServicio {

    private static final double TARIFA_POR_HORA = 800.0;
    private final RegistroDAO dao;

    public ParqueoServicio() {
        this.dao = new RegistroDAO();
    }

    public Registro registrarIngreso(String placa, Vehiculo.TipoVehiculo tipo)
            throws ParqueoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new ParqueoException("La placa no puede estar vacía.");
        }
        if (tipo == null) {
            throw new ParqueoException("Debe seleccionar el tipo de vehículo.");
        }

        placa = placa.trim().toUpperCase();

        try {
            List<Registro> activos = dao.leerActivos();
            for (Registro r : activos) {
                if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                    throw new ParqueoException(
                            "La placa \"" + placa + "\" ya está registrada en el parqueo.");
                }
            }

            int nuevoId = dao.siguienteId();
            Vehiculo v = new Vehiculo(placa, tipo);
            Registro r = new Registro(nuevoId, v, LocalDateTime.now());

            activos.add(r);
            dao.guardarActivos(activos);
            return r;

        } catch (IOException e) {
            throw new ParqueoException("Error al acceder a los datos: " + e.getMessage());
        }
    }

    public Registro registrarSalida(int registroId) throws ParqueoException {
        try {
            List<Registro> activos = dao.leerActivos();
            Registro encontrado = null;

            for (Registro r : activos) {
                if (r.getId() == registroId) {
                    encontrado = r;
                    break;
                }
            }

            if (encontrado == null) {
                throw new ParqueoException(
                        "No se encontró un registro activo con el ID indicado.");
            }

            LocalDateTime salida = LocalDateTime.now();
            encontrado.setHoraSalida(salida);
            encontrado.setMontoTotal(
                    calcularMonto(encontrado.getHoraEntrada(), salida));

            activos.remove(encontrado);
            dao.guardarActivos(activos);

            List<Registro> historial = dao.leerHistorial();
            historial.add(encontrado);
            dao.guardarHistorial(historial);

            return encontrado;

        } catch (IOException e) {
            throw new ParqueoException("Error al acceder a los datos: " + e.getMessage());
        }
    }

    public List<Registro> obtenerActivos() throws ParqueoException {
        try {
            return dao.leerActivos();
        } catch (IOException e) {
            throw new ParqueoException("Error al leer vehículos activos: " + e.getMessage());
        }
    }

    public List<Registro> obtenerHistorial() throws ParqueoException {
        try {
            return dao.leerHistorial();
        } catch (IOException e) {
            throw new ParqueoException("Error al leer historial: " + e.getMessage());
        }
    }

    public void eliminarRegistroHistorial(int registroId) throws ParqueoException {
        try {
            List<Registro> historial = dao.leerHistorial();
            boolean eliminado = historial.removeIf(r -> r.getId() == registroId);
            if (!eliminado) {
                throw new ParqueoException("No se encontró el registro en el historial.");
            }
            dao.guardarHistorial(historial);
        } catch (IOException e) {
            throw new ParqueoException("Error al eliminar registro: " + e.getMessage());
        }
    }

    public void limpiarHistorial() throws ParqueoException {
        try {
            dao.guardarHistorial(new ArrayList<>());
        } catch (IOException e) {
            throw new ParqueoException("Error al limpiar historial: " + e.getMessage());
        }
    }

    public double calcularMonto(LocalDateTime entrada, LocalDateTime salida) {
        long minutos = ChronoUnit.MINUTES.between(entrada, salida);
        if (minutos < 0) {
            minutos = 0;
        }
        long horas = (minutos + 59) / 60;
        if (horas == 0) {
            horas = 1;
        }
        return horas * TARIFA_POR_HORA;
    }

    public double getTarifaPorHora() {
        return TARIFA_POR_HORA;
    }

    public Registro registrarSalidaSimulada(int registroId, int horas, int minutos)
            throws ParqueoException {
        try {
            List<Registro> activos = dao.leerActivos();
            Registro encontrado = null;

            for (Registro r : activos) {
                if (r.getId() == registroId) {
                    encontrado = r;
                    break;
                }
            }

            if (encontrado == null) {
                throw new ParqueoException("No se encontró un registro activo con el ID indicado.");
            }

            // Salida simulada = entrada + tiempo indicado
            LocalDateTime salidaSimulada = encontrado.getHoraEntrada()
                    .plusHours(horas)
                    .plusMinutes(minutos);

            encontrado.setHoraSalida(salidaSimulada);
            encontrado.setMontoTotal(calcularMonto(encontrado.getHoraEntrada(), salidaSimulada));

            activos.remove(encontrado);
            dao.guardarActivos(activos);

            List<Registro> historial = dao.leerHistorial();
            historial.add(encontrado);
            dao.guardarHistorial(historial);

            return encontrado;

        } catch (IOException e) {
            throw new ParqueoException("Error al acceder a los datos: " + e.getMessage());
        }
    }
}
