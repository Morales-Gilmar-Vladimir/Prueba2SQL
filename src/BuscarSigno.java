import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BuscarSigno {
    private JComboBox<String> comboBox1;
    private JButton BUSCARButton;
    JPanel rootPanel;

    private static final String DB_URL = "jdbc:mysql://localhost/Personas";
    private static final String USER = "root";
    private static final String PASS = "root_bas3";

    public BuscarSigno() {

        String[] signoptions = {
                "Piscis", "Acuario", "Leo", "Capricornio", "Libra",
                "Escorpio", "Aries", "Tauro", "Geminis", "Cancer", "Virgo", "Sagitario"
        };
        comboBox1.setModel(new DefaultComboBoxModel<>(signoptions));

        BUSCARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSigno = (String) comboBox1.getSelectedItem();
                buscarPorSigno(selectedSigno);
            }
        });
    }

    private void buscarPorSigno(String signo) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM persona WHERE SignoZodiaco = ?")) {

            pstmt.setString(1, signo);


            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String codigo = rs.getString("Codigo");
                    String cedula = rs.getString("cedula");
                    String retrievedNombre = rs.getString("Nombre");
                    String fechaNacimiento = rs.getString("FechaNacimiento");
                    String signoZodiaco = rs.getString("SignoZodiaco");

                    System.out.println("Código: " + codigo);
                    System.out.println("Cédula: " + cedula);
                    System.out.println("Nombre: " + retrievedNombre);
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