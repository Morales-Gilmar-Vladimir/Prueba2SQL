import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BuscarCodigo {
     JPanel rootPanel;
    private JTextField codigoField;
    private JButton BUSCARButton;

    private static final String DB_URL = "jdbc:mysql://localhost/Personas";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";

    public BuscarCodigo() {
        BUSCARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = codigoField.getText();
                buscarPorCodigo(codigo);
            }
        });
    }

    private void buscarPorCodigo(String codigo) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM persona WHERE Codigo = ?")) {

            pstmt.setInt(1, Integer.parseInt(codigo));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String retrievedCodigo = rs.getString("Codigo");
                    String cedula = rs.getString("cedula");
                    String nombre = rs.getString("Nombre");
                    String fechaNacimiento = rs.getString("FechaNacimiento");
                    String signoZodiaco = rs.getString("SignoZodiaco");


                    System.out.println("Código: " + retrievedCodigo);
                    System.out.println("Cédula: " + cedula);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Fecha de Nacimiento: " + fechaNacimiento);
                    System.out.println("Signo Zodiaco: " + signoZodiaco);
                    System.out.println("-------------------------------");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}