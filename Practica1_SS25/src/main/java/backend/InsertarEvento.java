/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author eleaz
 */
public class InsertarEvento {

    private Connection connection;

    public InsertarEvento(Connection connection) {
        this.connection = connection;
    }

    public void ingresarEvento(Eventos evento) {
        String sql = "INSERT INTO evento (codigo_evento, fecha_evento, tipo_evento, titulo, ubicacion, cupo_maximo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, evento.getCodigoEvento());

            // convertir LocalDate a java.sql.Date
            pstmt.setDate(2, java.sql.Date.valueOf(evento.getFechaEvento()));

            pstmt.setString(3, evento.getTipoEvento().name()); // el enum se guarda como texto
            pstmt.setString(4, evento.getTituloEvento());
            pstmt.setString(5, evento.getUbicacion());
            pstmt.setInt(6, evento.getCupoMaximo());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Evento ingresado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el evento en la base de datos.");
            e.printStackTrace();
        }
    }

    public boolean existeCodigoEvento(String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM evento WHERE codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar el código del evento.");
            e.printStackTrace();
        }
        return false;
    }

}
