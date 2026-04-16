package logica;

import accesodatos.RegistroDAO;
import entidades.Registro;
import entidades.Vehiculo;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ParqueoServicio {

    private static final double TARIFA_POR_HORA = 800.0;
    private final RegistroDAO dao;

    public ParqueoServicio() {
        this.dao = new RegistroDAO();
    }

    public Registro registrarIngreso(String placa, Vehiculo.TipoVehiculo tipo)
            throws ParqueoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new ParqueoException("La placa no puede estar vacia.");
        }
        if (tipo == null) {
            throw new ParqueoException("Debe seleccionar el tipo de vehiculo.");
        }

        placa = placa.trim().toUpperCase();

        try {
            List<Registro> activos = dao.leerActivos();
            for (Registro r : activos) {
                if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                    throw new ParqueoException(
                            "La placa \"" + placa + "\" ya esta registrada en el parqueo.");
                }
            }

            int nuevoId = dao.siguienteId();
            Vehiculo vehiculo = new Vehiculo(placa, tipo);
            Registro registro = new Registro(nuevoId, vehiculo, LocalDateTime.now());
            activos.add(registro);
            dao.guardarActivos(activos);
            return registro;
        } catch (IOException e) {
            throw new ParqueoException("Error al acceder a los datos: " + e.getMessage());
        }
    }

    public List<Registro> obtenerActivos() throws ParqueoException {
        try {
            return dao.leerActivos();
        } catch (IOException e) {
            throw new ParqueoException("Error al leer vehiculos activos: " + e.getMessage());
        }
    }

    public double getTarifaPorHora() {
        return TARIFA_POR_HORA;
    }
}
