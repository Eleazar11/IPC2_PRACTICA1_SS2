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
public class InsertarPago {

    private Connection connection;

    public InsertarPago(Connection connection) {
        this.connection = connection;
    }

    public void ingresarPago(Pago pago) throws Exception {
        // 1. Validar que exista la inscripción
        if (!existeInscripcion(pago.getCorreoParticipante(), pago.getCodigoEvento())) {
            throw new Exception("No existe una inscripción para este participante en el evento indicado.");
        }

        // 2. Validar que el monto sea >= precio_evento
        double precioEvento = obtenerPrecioEvento(pago.getCodigoEvento());
        if (precioEvento < 0) {
            throw new Exception("No se encontró el evento asociado al código.");
        }

        if (pago.getMonto() < precioEvento) {
            throw new Exception("El monto ingresado no puede ser menor al precio del evento. Precio: " + precioEvento);
        }

        // 3. Validar que no exista ya un pago
        if (existePago(pago.getCorreoParticipante(), pago.getCodigoEvento())) {
            throw new Exception("Ya existe un pago registrado para este participante en este evento.");
        }

        // 4. Insertar el pago
        String sql = "INSERT INTO pago (correo_participante, codigo_evento, metodo_pago, monto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pago.getCorreoParticipante());
            pstmt.setString(2, pago.getCodigoEvento());
            pstmt.setString(3, pago.getTipoPago().name());
            pstmt.setDouble(4, pago.getMonto());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pago registrado exitosamente.");

                // Actualizar participante_validado
                actualizarParticipante(pago.getCorreoParticipante());

                // Actualizar inscripcion_validada
                actualizarInscripcion(pago.getCorreoParticipante(), pago.getCodigoEvento());
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el pago en la base de datos.");
            e.printStackTrace();
            throw new Exception("Error al registrar el pago.");
        }
    }

    // Validar existencia de inscripción
    private boolean existeInscripcion(String correoParticipante, String codigoEvento) {
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

    // Obtener precio del evento
    private double obtenerPrecioEvento(String codigoEvento) {
        String sql = "SELECT precio_evento FROM evento WHERE codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio_evento");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el precio del evento.");
            e.printStackTrace();
        }
        return -1; // Indica que no se encontró
    }

    // Validar existencia de pago
    private boolean existePago(String correoParticipante, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM pago WHERE correo_participante = ? AND codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correoParticipante);
            pstmt.setString(2, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar si ya existe un pago.");
            e.printStackTrace();
        }
        return false;
    }

    // Marcar al participante como validado
    private void actualizarParticipante(String correo) {
        String sql = "UPDATE participante SET participante_validado = 1 WHERE correo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar participante_validado.");
            e.printStackTrace();
        }
    }

// Marcar la inscripción como validada
    private void actualizarInscripcion(String correo, String codigoEvento) {
        String sql = "UPDATE inscripcion SET inscripcion_validada = 1 WHERE correo_participante = ? AND codigo_evento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoEvento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar inscripcion_validada.");
            e.printStackTrace();
        }
    }

}
