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
public class InsertarAsistencia {

    private Connection connection;

    public InsertarAsistencia(Connection connection) {
        this.connection = connection;
    }

    public void ingresarAsistencia(Asistencia asistencia) throws Exception {
        // Validamos que el correo esté inscrito en el evento correspondiente a la actividad
        if (!existeInscripcion(asistencia.getCorreoParticipante(), asistencia.getCodigoActividad())) {
            throw new Exception("El participante no está inscrito en el evento correspondiente a esta actividad.");
        }

        // Validamos que la inscripción esté validada
        if (!inscripcionValidada(asistencia.getCorreoParticipante(), asistencia.getCodigoActividad())) {
            throw new Exception("La inscripción del participante no está validada.");
        }

        // Validamos que no exista asistencia duplicada
        if (asistenciaRegistrada(asistencia.getCorreoParticipante(), asistencia.getCodigoActividad())) {
            throw new Exception("El participante ya tiene asistencia registrada para esta actividad.");
        }

        // Validamos cupo disponible
        if (!hayCupoDisponible(asistencia.getCodigoActividad())) {
            throw new Exception("No hay cupo disponible en esta actividad.");
        }

        // Validamos la asistencia
        String sql = "INSERT INTO asistencia (codigo_actividad, correo_participante) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, asistencia.getCodigoActividad());
            pstmt.setString(2, asistencia.getCorreoParticipante());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Asistencia registrada exitosamente.");
            }
        } catch (SQLException e) {
            throw new Exception("Error al registrar la asistencia: " + e.getMessage(), e);
        }
    }

    // Verificmos si el participante está inscrito en el evento correspondiente a la actividad
    private boolean existeInscripcion(String correo, String codigoActividad) {
        String sql = "SELECT COUNT(*) FROM inscripcion i "
                + "JOIN actividad a ON i.codigo_evento = a.codigo_evento "
                + "WHERE i.correo_participante = ? AND a.codigo_actividad = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoActividad);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validamos si la inscripción del participante está validada
    private boolean inscripcionValidada(String correo, String codigoActividad) {
        String sql = "SELECT COUNT(*) FROM inscripcion i "
                + "JOIN actividad a ON i.codigo_evento = a.codigo_evento "
                + "WHERE i.correo_participante = ? "
                + "AND a.codigo_actividad = ? "
                + "AND i.inscripcion_validada = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoActividad);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validamos si el participante ya tiene asistencia registrada en la actividad
    private boolean asistenciaRegistrada(String correo, String codigoActividad) {
        String sql = "SELECT COUNT(*) FROM asistencia "
                + "WHERE correo_participante = ? AND codigo_actividad = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoActividad);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Validamos si la actividad aún tiene cupo disponible
    private boolean hayCupoDisponible(String codigoActividad) {
        String sql = "SELECT a.cupo_maximo, COUNT(asist.id_asistencia) AS total_asistencias "
                + "FROM actividad a "
                + "LEFT JOIN asistencia asist ON a.codigo_actividad = asist.codigo_actividad "
                + "WHERE a.codigo_actividad = ? "
                + "GROUP BY a.cupo_maximo";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoActividad);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cupoMaximo = rs.getInt("cupo_maximo");
                int totalAsistencias = rs.getInt("total_asistencias");
                return totalAsistencias < cupoMaximo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // si hay error, asumimos que no hay cupo
    }

}
