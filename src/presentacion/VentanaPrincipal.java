package presentacion;

import entidades.Registro;
import entidades.Vehiculo;
import logica.ParqueoException;
import logica.ParqueoServicio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class VentanaPrincipal extends JFrame {

    private final ParqueoServicio servicio = new ParqueoServicio();
    private JTextField txtPlaca;
    private JComboBox<Vehiculo.TipoVehiculo> cmbTipo;
    private DefaultTableModel modeloActivos;
    private JTable tablaActivos;
    private JLabel lblEstado;

    public VentanaPrincipal() {
        initUI();
        cargarDatos();
    }

    private void initUI() {
        setTitle("Sistema de Parqueo Publico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelIngreso = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelIngreso.setBorder(BorderFactory.createTitledBorder("Registro de ingreso"));

        panelIngreso.add(new JLabel("Placa:"));
        txtPlaca = new JTextField(12);
        panelIngreso.add(txtPlaca);

        panelIngreso.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(Vehiculo.TipoVehiculo.values());
        panelIngreso.add(cmbTipo);

        JButton btnIngresar = new JButton("Registrar ingreso");
        btnIngresar.addActionListener(e -> accionIngreso());
        panelIngreso.add(btnIngresar);

        add(panelIngreso, BorderLayout.NORTH);

        modeloActivos = new DefaultTableModel(new String[]{"ID", "Placa", "Tipo", "Hora de entrada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaActivos = new JTable(modeloActivos);
        add(new JScrollPane(tablaActivos), BorderLayout.CENTER);

        lblEstado = new JLabel("Sistema listo. Tarifa: CRC 800 por hora o fraccion.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(lblEstado, BorderLayout.SOUTH);
    }

    private void accionIngreso() {
        try {
            Registro registro = servicio.registrarIngreso(
                    txtPlaca.getText(),
                    (Vehiculo.TipoVehiculo) cmbTipo.getSelectedItem());
            txtPlaca.setText("");
            cargarDatos();
            lblEstado.setText("Ingreso registrado: " + registro.getVehiculo().getPlaca());
        } catch (ParqueoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        modeloActivos.setRowCount(0);
        try {
            List<Registro> activos = servicio.obtenerActivos();
            for (Registro registro : activos) {
                modeloActivos.addRow(new Object[]{
                    registro.getId(),
                    registro.getVehiculo().getPlaca(),
                    registro.getVehiculo().getTipo(),
                    registro.getHoraEntradaStr()
                });
            }
        } catch (ParqueoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
