/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package accesodatos;

import entidades.Registro;
import entidades.Vehiculo;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ekaro
 */
public class RegistroDAO {

    private static final String SEP = "|";
    private static final String ACTIVOS_FILE = "data/activos.txt";
    private static final String HISTORIAL_FILE = "data/historial.txt";

    public List<Registro> leerActivos() throws IOException {
        return leerArchivo(ACTIVOS_FILE);
    }

    public void guardarActivos(List<Registro> lista) throws IOException {
        guardarArchivo(ACTIVOS_FILE, lista);
    }

    public List<Registro> leerHistorial() throws IOException {
        return leerArchivo(HISTORIAL_FILE);
    }

    public void guardarHistorial(List<Registro> lista) throws IOException {
        guardarArchivo(HISTORIAL_FILE, lista);
    }

    public int siguienteId() throws IOException {
        int max = 0;
        for (Registro r : leerActivos()) {
            if (r.getId() > max) {
                max = r.getId();
            }
        }
        for (Registro r : leerHistorial()) {
            if (r.getId() > max) {
                max = r.getId();
            }
        }
        return max + 1;
    }

    private List<Registro> leerArchivo(String ruta) throws IOException {
        List<Registro> lista = new ArrayList<>();
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            return lista;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }
                Registro r = parsearLinea(linea);
                if (r != null) {
                    lista.add(r);
                }
            }
        }
        return lista;
    }

    private void guardarArchivo(String ruta, List<Registro> lista) throws IOException {
        Path path = Paths.get(ruta);
        Files.createDirectories(path.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Registro r : lista) {
                bw.write(serializarRegistro(r));
                bw.newLine();
            }
        }
    }

    private String serializarRegistro(Registro r) {
        String salida = r.getHoraSalida() != null
                ? r.getHoraSalida().format(Registro.FORMATO) : "";
        return r.getId()
                + SEP + r.getVehiculo().getPlaca()
                + SEP + r.getVehiculo().getTipo().name()
                + SEP + r.getHoraEntrada().format(Registro.FORMATO)
                + SEP + salida
                + SEP + r.getMontoTotal();
    }

    private Registro parsearLinea(String linea) {
        try {
            String[] p = linea.split("\\|", -1);
            if (p.length < 6) {
                return null;
            }

            int id = Integer.parseInt(p[0].trim());
            String placa = p[1].trim();
            Vehiculo.TipoVehiculo tipo
                    = Vehiculo.TipoVehiculo.valueOf(p[2].trim());
            LocalDateTime entrada
                    = LocalDateTime.parse(p[3].trim(), Registro.FORMATO);
            LocalDateTime salida = p[4].trim().isEmpty() ? null
                    : LocalDateTime.parse(p[4].trim(), Registro.FORMATO);
            double monto = Double.parseDouble(p[5].trim());

            Vehiculo v = new Vehiculo(placa, tipo);
            Registro r = new Registro(id, v, entrada);
            r.setHoraSalida(salida);
            r.setMontoTotal(monto);
            return r;
        } catch (Exception e) {
            return null;
        }
    }
}
