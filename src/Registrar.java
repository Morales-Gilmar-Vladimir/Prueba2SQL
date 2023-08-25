import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Registrar {
    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField cedulaField;
    private JTextField fechaField;
    private JButton botonBuscarPorCodigoButton;
    private JButton botonBuscarPorNombreButton;
    private JButton botonBuscarPorSignoButton;
    private JComboBox comboBox1;
    private JButton botonBorrarElPresenteButton;
    private JButton botonActualizarElPresenteButton;
    private JButton botonIngresarElPresenteButton;
    private JButton limpiarFormularioButton;
    private JPanel rootPanel;
    private List<String> codigos;
    private List<String> cedulas;
    private List<String> nombres;
    private List<String> fechas;
    private List<String> signo;

    private static final String DB_URL = "jdbc:mysql://localhost/Personas";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";
    private static final String QUERY = "SELECT * FROM persona";
    public Registrar() {
        codigos = new ArrayList<>();
        cedulas = new ArrayList<>();
        nombres = new ArrayList<>();
        fechas = new ArrayList<>();
        signo = new ArrayList<>();

        cargarUsuariosDesdeBaseDeDatos();

        botonIngresarElPresenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText();
                String nombre = nombreField.getText();
                String cedula = cedulaField.getText();
                String fecha = fechaField.getText();
                String selectedSigno = (String) comboBox1.getSelectedItem();

                guardarRegistroEnBaseDeDatos(codigo, nombre, cedula, fecha, selectedSigno);
            }
        });

        botonBuscarPorCodigoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Buscar por codigo");
                frame.setContentPane(new BuscarCodigo().rootPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        botonBuscarPorSignoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Buscar por signo");
                frame.setContentPane(new BuscarSigno().rootPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        botonBuscarPorNombreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Buscar por nombre");
                frame.setContentPane(new BuscarNombre().rootPanel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        botonBorrarElPresenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText();

                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String deleteQuery = "DELETE FROM persona WHERE Codigo=" + codigo;

                    Statement statement = connection.createStatement();
                    int rowsDeleted = statement.executeUpdate(deleteQuery);

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(rootPanel, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarUsuariosDesdeBaseDeDatos();
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, "No se encontró ningún registro con el código ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        botonActualizarElPresenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText();
                String cedula = cedulaField.getText();
                String nombre = nombreField.getText();
                String nacimiento = fechaField.getText();
                String signo = (String) comboBox1.getSelectedItem();

                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String updateQuery = "UPDATE persona SET cedula='" + cedula + "', Nombre='" + nombre + "', FechaNacimiento='" + nacimiento + "', SignoZodiaco='" + signo + "' WHERE Codigo=" + codigo;

                    Statement statement = connection.createStatement();
                    int rowsUpdated = statement.executeUpdate(updateQuery);

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(rootPanel, "Registro actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarUsuariosDesdeBaseDeDatos(); // Refresh the displayed records after update
                    } else {
                        JOptionPane.showMessageDialog(rootPanel, "No se encontró ningún registro con el código ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        limpiarFormularioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codigoField.setText("");
                cedulaField.setText("");
                nombreField.setText("");
                fechaField.setText("año-mes-dia");
                comboBox1.setSelectedItem("Acuario");
            }
        });

    }

    private void cargarUsuariosDesdeBaseDeDatos() {
        codigos.clear();
        cedulas.clear();
        nombres.clear();
        fechas.clear();
        signo.clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY)) {

            while (rs.next()) {
                codigos.add(rs.getString("Codigo"));
                cedulas.add(rs.getString("cedula"));
                nombres.add(rs.getString("Nombre"));
                fechas.add(rs.getString("FechaNacimiento"));
                signo.add(rs.getString("SignoZodiaco"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void guardarRegistroEnBaseDeDatos(String codigo, String nombre, String cedula, String fecha, String signoValue) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO persona (Codigo, cedula, Nombre, FechaNacimiento, SignoZodiaco) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setInt(1, Integer.parseInt(codigo));
            pstmt.setInt(2, Integer.parseInt(cedula));
            pstmt.setString(3, nombre);
            pstmt.setDate(4, java.sql.Date.valueOf(fecha));
            pstmt.setString(5, signoValue);

            pstmt.executeUpdate();

            codigos.add(codigo);
            cedulas.add(cedula);
            nombres.add(nombre);
            fechas.add(fecha);
            signo.add(signoValue);

            JOptionPane.showMessageDialog(null, "Registro guardado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el registro.");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registrar");
        frame.setContentPane(new Registrar().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

