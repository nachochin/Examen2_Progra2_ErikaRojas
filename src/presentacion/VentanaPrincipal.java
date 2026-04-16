/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import entidades.Registro;
import entidades.Vehiculo;
import logica.ParqueoException;
import logica.ParqueoServicio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author ekaro
 */
public class VentanaPrincipal extends JFrame {

    private final ParqueoServicio servicio = new ParqueoServicio();

    private static final Color COLOR_PRIMARIO = new Color(30, 80, 160);
    private static final Color COLOR_SECUNDARIO = new Color(245, 247, 252);
    private static final Color COLOR_ACENTO = new Color(220, 53, 69);
    private static final Color COLOR_EXITO = new Color(25, 135, 84);
    private static final Color COLOR_TABLA_CAB = new Color(30, 80, 160);
    private static final Color COLOR_FILA_PAR = new Color(235, 241, 255);
    private static final Color COLOR_TEXTO = new Color(33, 37, 41);

    private JTextField txtPlaca;
    private JComboBox<Vehiculo.TipoVehiculo> cmbTipo;
    private JButton btnIngresar;

    private DefaultTableModel modeloActivos;
    private JTable tablaActivos;

    private JButton btnSalida;
    private JLabel lblResultadoSalida;

    private DefaultTableModel modeloHistorial;
    private JTable tablaHistorial;
    private JButton btnEliminarHistorial;
    private JButton btnLimpiarHistorial;

    private JLabel lblEstado;

    public VentanaPrincipal() {
        initUI();
        cargarDatos();
    }

    private void initUI() {
        setTitle("Sistema de Parqueo Público");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(COLOR_SECUNDARIO);
        setContentPane(root);

        root.add(crearHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(COLOR_SECUNDARIO);
        tabs.addTab("Vehículos en Parqueo", crearPanelActivos());
        tabs.addTab("Historial", crearPanelHistorial());
        root.add(tabs, BorderLayout.CENTER);

        root.add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Parqueo Público");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        JLabel subtitulo = new JLabel("Sistema de Administración");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(180, 200, 240));
        header.add(subtitulo, BorderLayout.EAST);

        return header;
    }

    private JPanel crearPanelActivos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_SECUNDARIO);
        panel.setBorder(new EmptyBorder(14, 16, 10, 16));

        panel.add(crearFormIngreso(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearTablaActivos(), crearPanelSalida());
        split.setResizeWeight(0.65);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setBackground(COLOR_SECUNDARIO);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFormIngreso() {
        JPanel card = crearCard("Registro de Ingreso");
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));

        card.add(etiqueta("Placa:"));
        txtPlaca = new JTextField(12);
        estilizarTextField(txtPlaca);
        card.add(txtPlaca);

        card.add(Box.createHorizontalStrut(8));

        card.add(etiqueta("Tipo:"));
        cmbTipo = new JComboBox<>(Vehiculo.TipoVehiculo.values());
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipo.setPreferredSize(new Dimension(120, 32));
        card.add(cmbTipo);

        card.add(Box.createHorizontalStrut(12));

        btnIngresar = crearBoton("  Registrar Ingreso  ", COLOR_EXITO, Color.WHITE);
        btnIngresar.addActionListener(e -> accionIngreso());
        card.add(btnIngresar);

        return card;
    }

    private JScrollPane crearTablaActivos() {
        String[] columnas = {"ID", "Placa", "Tipo", "Hora de Entrada"};
        modeloActivos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaActivos = estilizarTabla(new JTable(modeloActivos));

        JScrollPane scroll = new JScrollPane(tablaActivos);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 240)),
                "Vehículos actualmente en el parqueo",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12), COLOR_PRIMARIO));
        return scroll;
    }

    private JPanel crearPanelSalida() {
        JPanel card = crearCard("Registrar Salida");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel instruccion = new JLabel(
                "<html><center>Seleccione un vehículo<br>"
                + "en la tabla y presione<br>el botón de salida.</center></html>");
        instruccion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instruccion.setForeground(new Color(90, 100, 120));
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(instruccion);
        card.add(Box.createVerticalStrut(16));

        btnSalida = crearBoton("  Registrar Salida  ", COLOR_ACENTO, Color.WHITE);
        btnSalida.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalida.addActionListener(e -> accionSalida());
        card.add(btnSalida);
        card.add(Box.createVerticalStrut(16));

        lblResultadoSalida = new JLabel(" ");
        lblResultadoSalida.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResultadoSalida.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblResultadoSalida);

        return card;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_SECUNDARIO);
        panel.setBorder(new EmptyBorder(14, 16, 10, 16));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        barra.setBackground(COLOR_SECUNDARIO);

        btnEliminarHistorial = crearBoton("Eliminar seleccionado", COLOR_ACENTO, Color.WHITE);
        btnEliminarHistorial.addActionListener(e -> accionEliminarHistorial());

        btnLimpiarHistorial = crearBoton("Limpiar todo el historial",
                new Color(108, 117, 125), Color.WHITE);
        btnLimpiarHistorial.addActionListener(e -> accionLimpiarHistorial());

        JButton btnReimprimir = crearBoton("Reimprimir Recibo", COLOR_PRIMARIO, Color.WHITE);
        btnReimprimir.addActionListener(e -> accionReimprimirRecibo());

        barra.add(btnEliminarHistorial);
        barra.add(btnLimpiarHistorial);
        barra.add(btnReimprimir);
        panel.add(barra, BorderLayout.NORTH);

        String[] cols = {"ID", "Placa", "Tipo", "Entrada", "Salida", "Monto (₡)"};
        modeloHistorial = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaHistorial = estilizarTabla(new JTable(modeloHistorial));

        // Doble clic también abre el recibo
        tablaHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    accionReimprimirRecibo();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 240)),
                "Historial de vehículos — doble clic para reimprimir recibo",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12), COLOR_PRIMARIO));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_PRIMARIO);
        barra.setBorder(new EmptyBorder(4, 14, 4, 14));

        lblEstado = new JLabel("Sistema listo.");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(200, 220, 255));
        barra.add(lblEstado, BorderLayout.WEST);

        NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));
        JLabel tarifa = new JLabel(
                "Tarifa: " + fmt.format(servicio.getTarifaPorHora()) + " / hora o fracción");
        tarifa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tarifa.setForeground(new Color(200, 220, 255));
        barra.add(tarifa, BorderLayout.EAST);

        return barra;
    }

    // ── Acciones ─────────────────────────────────────────────────────
    private void accionReimprimirRecibo() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un registro del historial para reimprimir el recibo.");
            return;
        }

        try {
            List<Registro> historial = servicio.obtenerHistorial();
            int id = (int) modeloHistorial.getValueAt(fila, 0);
            Registro r = null;
            for (Registro reg : historial) {
                if (reg.getId() == id) {
                    r = reg;
                    break;
                }
            }
            if (r == null) {
                mostrarError("No se encontró el registro.");
                return;
            }
            mostrarRecibo(r);
        } catch (ParqueoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionIngreso() {
        String placa = txtPlaca.getText();
        Vehiculo.TipoVehiculo tipo = (Vehiculo.TipoVehiculo) cmbTipo.getSelectedItem();
        try {
            Registro r = servicio.registrarIngreso(placa, tipo);
            txtPlaca.setText("");
            cargarDatos();
            mostrarEstado("Ingreso registrado: " + r.getVehiculo().getPlaca()
                    + " — " + r.getHoraEntradaStr(), COLOR_EXITO);
        } catch (ParqueoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionSalida() {
        int fila = tablaActivos.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un vehículo de la tabla para registrar su salida.");
            return;
        }
        int id = (int) modeloActivos.getValueAt(fila, 0);
        try {
            Registro r = servicio.registrarSalida(id);
            NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));
            lblResultadoSalida.setForeground(COLOR_EXITO);
            lblResultadoSalida.setText("<html><center>Salida registrada<br>"
                    + r.getVehiculo().getPlaca() + "<br>Monto: "
                    + fmt.format(r.getMontoTotal()) + "</center></html>");
            cargarDatos();
            mostrarEstado("Salida: " + r.getVehiculo().getPlaca()
                    + " — " + fmt.format(r.getMontoTotal()), COLOR_EXITO);
            mostrarRecibo(r);
        } catch (ParqueoException ex) {
            mostrarError(ex.getMessage());
        }
    }

   private void mostrarRecibo(Registro r) {
    NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

    long minutos = java.time.temporal.ChronoUnit.MINUTES.between(
            r.getHoraEntrada(), r.getHoraSalida());
    long horas = minutos / 60;
    long mins  = minutos % 60;
    String tiempo = horas + " h " + mins + " min";

    // ── Diálogo ──────────────────────────────────────────────────────
    JDialog dialogo = new JDialog(this, "Recibo de Pago", true);
    dialogo.setSize(420, 540);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new BorderLayout());
    dialogo.getContentPane().setBackground(Color.WHITE);

    // ── Panel principal ──────────────────────────────────────────────
    JPanel contenido = new JPanel();
    contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
    contenido.setBackground(Color.WHITE);
    contenido.setBorder(new EmptyBorder(0, 0, 10, 0));

    // -- Encabezado azul ---------------------------------------------
    JPanel encabezado = new JPanel();
    encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
    encabezado.setBackground(COLOR_PRIMARIO);
    encabezado.setBorder(new EmptyBorder(22, 20, 22, 20));

    

    JLabel lblEmpresa = new JLabel("PARQUEO PÚBLICO");
    lblEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblEmpresa.setForeground(Color.WHITE);
    lblEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lblRecibo = new JLabel("RECIBO DE PAGO");
    lblRecibo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    lblRecibo.setForeground(new Color(180, 210, 255));
    lblRecibo.setAlignmentX(Component.CENTER_ALIGNMENT);

    encabezado.add(lblEmpresa);
    encabezado.add(Box.createVerticalStrut(2));
    encabezado.add(lblRecibo);
    contenido.add(encabezado);

    // -- Cuerpo blanco -----------------------------------------------
    JPanel cuerpo = new JPanel(new GridBagLayout());
    cuerpo.setBackground(Color.WHITE);
    cuerpo.setBorder(new EmptyBorder(18, 28, 10, 28));

    GridBagConstraints izq = new GridBagConstraints();
    izq.anchor = GridBagConstraints.WEST;
    izq.insets = new Insets(5, 0, 5, 16);
    izq.gridx  = 0; izq.gridy = 0;

    GridBagConstraints der = new GridBagConstraints();
    der.anchor = GridBagConstraints.WEST;
    der.insets = new Insets(5, 0, 5, 0);
    der.gridx  = 1; der.gridy = 0;
    der.weightx = 1.0; der.fill = GridBagConstraints.HORIZONTAL;

    Font fuenteEtiq = new Font("Segoe UI", Font.BOLD, 13);
    Font fuenteValor = new Font("Segoe UI", Font.PLAIN, 13);
    Color gris = new Color(100, 110, 130);

    Object[][] filas = {
        {"N° Registro",  String.valueOf(r.getId())},
        {"Placa",        r.getVehiculo().getPlaca()},
        {"Tipo",         r.getVehiculo().getTipo().toString()},
        {null, null}, // separador
        {"Entrada",      r.getHoraEntradaStr()},
        {"Salida",       r.getHoraSalidaStr()},
        {"Tiempo",       tiempo},
        {null, null}, // separador
        {"Tarifa",       "₡800 / hora o fracción"},
    };

    int fila = 0;
    for (Object[] par : filas) {
        if (par[0] == null) {
            // línea separadora
            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(220, 228, 245));
            GridBagConstraints sepC = new GridBagConstraints();
            sepC.gridx = 0; sepC.gridy = fila;
            sepC.gridwidth = 2;
            sepC.fill = GridBagConstraints.HORIZONTAL;
            sepC.insets = new Insets(4, 0, 4, 0);
            cuerpo.add(sep, sepC);
        } else {
            JLabel etiq = new JLabel((String) par[0]);
            etiq.setFont(fuenteEtiq);
            etiq.setForeground(gris);
            izq.gridy = fila;
            cuerpo.add(etiq, izq);

            JLabel val = new JLabel((String) par[1]);
            val.setFont(fuenteValor);
            val.setForeground(COLOR_TEXTO);
            der.gridy = fila;
            cuerpo.add(val, der);
        }
        fila++;
    }
    contenido.add(cuerpo);

    // -- Total destacado ---------------------------------------------
    JPanel panelTotal = new JPanel(new BorderLayout());
    panelTotal.setBackground(new Color(235, 241, 255));
    panelTotal.setBorder(new EmptyBorder(14, 28, 14, 28));

    JLabel lblTotalTxt = new JLabel("TOTAL A PAGAR");
    lblTotalTxt.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblTotalTxt.setForeground(COLOR_PRIMARIO);

    JLabel lblTotalVal = new JLabel(fmt.format(r.getMontoTotal()));
    lblTotalVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblTotalVal.setForeground(COLOR_EXITO);
    lblTotalVal.setHorizontalAlignment(SwingConstants.RIGHT);

    panelTotal.add(lblTotalTxt, BorderLayout.WEST);
    panelTotal.add(lblTotalVal, BorderLayout.EAST);
    contenido.add(panelTotal);

    // -- Pie de página -----------------------------------------------
    JLabel pie = new JLabel("¡Gracias por su visita!");
    pie.setFont(new Font("Segoe UI", Font.ITALIC, 12));
    pie.setForeground(new Color(150, 160, 180));
    pie.setAlignmentX(Component.CENTER_ALIGNMENT);
    pie.setBorder(new EmptyBorder(12, 0, 4, 0));
    contenido.add(pie);

    dialogo.add(contenido, BorderLayout.CENTER);

    // ── Botones ──────────────────────────────────────────────────────
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
    panelBotones.setBackground(Color.WHITE);
    panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
            new Color(220, 228, 245)));

    JButton btnImprimir = crearBoton("🖨  Imprimir", COLOR_PRIMARIO, Color.WHITE);
    btnImprimir.addActionListener(e -> {
        try {
            // Panel temporal para imprimir
            JTextArea temp = new JTextArea(generarTextoRecibo(r));
            temp.setFont(new Font("Monospaced", Font.PLAIN, 12));
            temp.print();
        } catch (java.awt.print.PrinterException ex) {
            mostrarError("Error al imprimir: " + ex.getMessage());
        }
    });

    JButton btnCerrar = crearBoton("Cerrar", new Color(108, 117, 125), Color.WHITE);
    btnCerrar.addActionListener(e -> dialogo.dispose());

    panelBotones.add(btnImprimir);
    panelBotones.add(btnCerrar);
    dialogo.add(panelBotones, BorderLayout.SOUTH);

    dialogo.setVisible(true);
}

private String generarTextoRecibo(Registro r) {
    NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));
    long minutos = java.time.temporal.ChronoUnit.MINUTES.between(
            r.getHoraEntrada(), r.getHoraSalida());
    long horas = minutos / 60;
    long mins  = minutos % 60;
    return  "========================================\n"
          + "          PARQUEO PÚBLICO\n"
          + "          RECIBO DE PAGO\n"
          + "========================================\n"
          + "  N° Registro : " + r.getId() + "\n"
          + "  Placa       : " + r.getVehiculo().getPlaca() + "\n"
          + "  Tipo        : " + r.getVehiculo().getTipo() + "\n"
          + "----------------------------------------\n"
          + "  Entrada : " + r.getHoraEntradaStr() + "\n"
          + "  Salida  : " + r.getHoraSalidaStr() + "\n"
          + "  Tiempo  : " + horas + " h " + mins + " min\n"
          + "----------------------------------------\n"
          + "  Tarifa  : ₡800 / hora o fracción\n"
          + "  TOTAL   : " + fmt.format(r.getMontoTotal()) + "\n"
          + "========================================\n"
          + "       ¡Gracias por su visita!\n"
          + "========================================\n";
}

    private void accionEliminarHistorial() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un registro del historial para eliminar.");
            return;
        }
        int id = (int) modeloHistorial.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el registro ID " + id + " del historial?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            servicio.eliminarRegistroHistorial(id);
            cargarDatos();
            mostrarEstado("Registro " + id + " eliminado.", COLOR_PRIMARIO);
        } catch (ParqueoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionLimpiarHistorial() {
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar TODO el historial?",
                "Confirmar limpieza", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            servicio.limpiarHistorial();
            cargarDatos();
            mostrarEstado("Historial limpiado.", COLOR_PRIMARIO);
        } catch (ParqueoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // ── Carga de tablas ───────────────────────────────────────────────
    private void cargarDatos() {
        modeloActivos.setRowCount(0);
        try {
            for (Registro r : servicio.obtenerActivos()) {
                modeloActivos.addRow(new Object[]{
                    r.getId(), r.getVehiculo().getPlaca(),
                    r.getVehiculo().getTipo(), r.getHoraEntradaStr()
                });
            }
        } catch (ParqueoException ex) {
            mostrarError("Error al cargar activos: " + ex.getMessage());
        }

        modeloHistorial.setRowCount(0);
        try {
            NumberFormat fmt = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));
            for (Registro r : servicio.obtenerHistorial()) {
                modeloHistorial.addRow(new Object[]{
                    r.getId(), r.getVehiculo().getPlaca(),
                    r.getVehiculo().getTipo(), r.getHoraEntradaStr(),
                    r.getHoraSalidaStr(), fmt.format(r.getMontoTotal())
                });
            }
        } catch (ParqueoException ex) {
            mostrarError("Error al cargar historial: " + ex.getMessage());
        }
    }

    // ── Helpers UI ────────────────────────────────────────────────────
    private void mostrarEstado(String msg, Color color) {
        lblEstado.setText(msg);
        lblEstado.setForeground(color == COLOR_PRIMARIO
                ? new Color(200, 220, 255) : Color.WHITE);
    }

    private void mostrarError(String msg) {
        //lblEstado.setText("⚠  " + msg);
        lblEstado.setForeground(new Color(255, 180, 140));
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel crearCard(String titulo) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(190, 210, 240)),
                        titulo,
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12), COLOR_PRIMARIO),
                new EmptyBorder(6, 10, 8, 10)));
        return card;
    }

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }

    private JButton crearBoton(String texto, Color fondo, Color textColor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(fondo.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(fondo);
            }
        });
        return btn;
    }

    private void estilizarTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 240)),
                new EmptyBorder(4, 8, 4, 8)));
    }

    private JTable estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(28);
        tabla.setSelectionBackground(new Color(173, 204, 255));
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(new Color(220, 228, 245));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_TABLA_CAB);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : COLOR_FILA_PAR);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        return tabla;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
