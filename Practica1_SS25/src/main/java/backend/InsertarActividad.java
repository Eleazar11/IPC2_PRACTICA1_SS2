/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 *
 * @author eleaz
 */
public class InsertarActividad {

    private Connection connection;

    public InsertarActividad(Connection connection) {
        this.connection = connection;
    }

    // Método para ingresar una actividad
    public void ingresarActividad(Actividad actividad) throws Exception {
        // 1. Validar que el evento exista
        int cupoEvento = obtenerCupoEvento(actividad.getCodigoEvento());
        if (cupoEvento < 0) {
            throw new Exception("El evento con código " + actividad.getCodigoEvento() + " no existe.");
        }

        // 2. Validar que el encargado esté inscrito y validado en ese evento
        if (!encargadoValido(actividad.getCorreoEncargado(), actividad.getCodigoEvento())) {
            throw new Exception("El encargado no está inscrito o no tiene inscripción validada en este evento.");
        }

        // 3. Validar que el cupo de la actividad no supere el cupo del evento
        if (actividad.getCupoMaximo() > cupoEvento) {
            throw new Exception("El cupo de la actividad no puede superar el cupo del evento (" + cupoEvento + ").");
        }

        // 4. Validar que no exista otra actividad en el mismo evento y rango de horas
        if (actividadTraslapada(actividad)) {
            throw new Exception("Ya existe otra actividad en el mismo evento con un horario que se traslapa.");
        }

        // 5. Insertar la actividad
        String sql = "INSERT INTO actividad (codigo_actividad, codigo_evento, tipo_actividad, titulo, correo_encargado, hora_inicio, hora_fin, cupo_maximo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, actividad.getCodigoActividad());
            pstmt.setString(2, actividad.getCodigoEvento());
            pstmt.setString(3, actividad.getTipoActividad().name()); // enum como texto
            pstmt.setString(4, actividad.getTitulo());
            pstmt.setString(5, actividad.getCorreoEncargado());
            pstmt.setTime(6, Time.valueOf(actividad.getHoraInicio()));
            pstmt.setTime(7, Time.valueOf(actividad.getHoraFin()));
            pstmt.setInt(8, actividad.getCupoMaximo());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Actividad ingresada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar la actividad en la base de datos.");
            e.printStackTrace();
            throw new Exception("Error al registrar la actividad.");
        }
    }

    // Obtener cupo máximo del evento
    private int obtenerCupoEvento(String codigoEvento) {
        String sql = "SELECT cupo_maximo FROM evento WHERE codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cupo_maximo");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cupo del evento.");
            e.printStackTrace();
        }
        return -1;
    }

    // Validar que el encargado esté inscrito y validado
    private boolean encargadoValido(String correo, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM inscripcion WHERE correo_participante = ? AND codigo_evento = ? AND inscripcion_validada = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar encargado.");
            e.printStackTrace();
        }
        return false;
    }

    // Validar solapamiento de horarios en el mismo evento
    private boolean actividadTraslapada(Actividad actividad) {
        String sql = "SELECT COUNT(*) FROM actividad " +
                     "WHERE codigo_evento = ? " +
                     "AND ( (hora_inicio < ? AND hora_fin > ?) " +
                     "   OR (hora_inicio < ? AND hora_fin > ?) " +
                     "   OR (hora_inicio >= ? AND hora_fin <= ?) )";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, actividad.getCodigoEvento());
            pstmt.setTime(2, Time.valueOf(actividad.getHoraFin()));
            pstmt.setTime(3, Time.valueOf(actividad.getHoraInicio()));
            pstmt.setTime(4, Time.valueOf(actividad.getHoraFin()));
            pstmt.setTime(5, Time.valueOf(actividad.getHoraInicio()));
            pstmt.setTime(6, Time.valueOf(actividad.getHoraInicio()));
            pstmt.setTime(7, Time.valueOf(actividad.getHoraFin()));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar solapamiento de actividad.");
            e.printStackTrace();
        }
        return false;
    }
}
