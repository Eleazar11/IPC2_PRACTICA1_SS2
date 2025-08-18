/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author eleaz
 */
public class InsertarParticipante {

    private Connection connection;

    public InsertarParticipante(Connection connection) {
        this.connection = connection;
    }

    // Método para insertar un participante en la base de datos
    public void ingresarParticipante(Participante participante) {
        String sql = "INSERT INTO participante (correo, nombre_completo, tipo_participante, institucion, participante_validado) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, participante.getCorreoElectronico());
            pstmt.setString(2, participante.getNombre());
            pstmt.setString(3, participante.getTipoParticipante().name()); // Enum como texto
            pstmt.setString(4, participante.getInstitucion());
            pstmt.setBoolean(5, false); // al registrarse no está validado todavía

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Participante ingresado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el participante en la base de datos.");
            e.printStackTrace();
        }
    }

    // Validación de correo único
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM participante WHERE correo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar el correo del participante.");
            e.printStackTrace();
        }
        return false;
    }
}
