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
public class InsertarInscripcion {

    private Connection connection;

    public InsertarInscripcion(Connection connection) {
        this.connection = connection;
    }

    public void ingresarInscripcion(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripcion (correo_participante, codigo_evento, tipo_inscripcion, inscripcion_validada) "
                   + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, inscripcion.getCorreoParticipante());
            pstmt.setString(2, inscripcion.getCodigoEvento());
            pstmt.setString(3, inscripcion.getTipoInscripcion().name()); // Enum como texto
            pstmt.setBoolean(4, false); // al registrarse no está validada todavía

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inscripción ingresada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar la inscripción en la base de datos.");
            e.printStackTrace();
        }
    }

    // Validamos si existe una inscripción de este participante a este evento
    public boolean existeInscripcion(String correoParticipante, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM inscripcion WHERE correo_participante = ? AND codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correoParticipante);
            pstmt.setString(2, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la inscripción.");
            e.printStackTrace();
        }
        return false;
    }
}
