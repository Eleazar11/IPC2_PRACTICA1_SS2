/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author eleaz
 */
public class GeneradorCertificado {

    private Connection connection;

    public GeneradorCertificado(Connection connection) {
        this.connection = connection;
    }

    public void generar(Certificado certificado) throws Exception {

        // Validar asistencia
        if (!tieneAsistencia(certificado.getCorreoParticipante(), certificado.getCodigoEvento())) {
            throw new Exception("El participante no ha asistido a ninguna actividad válida del evento.");
        }

        // Crear carpeta "reportes/certificados" si no existe
        File carpeta = new File("reportes/certificados");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Formatear la fecha actual
        String fechaFormateada = certificado.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Generar archivo HTML
        String nombreArchivo = "reportes/certificados/certificado_" + certificado.getCorreoParticipante() + "_"
                + certificado.getCodigoEvento() + ".html";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang='es'>");
            pw.println("<head><meta charset='UTF-8'><title>Certificado de Participación</title></head>");
            pw.println("<body style='text-align:center; font-family:Arial, sans-serif;'>");
            pw.println("<h1>Certificado de Participación HYRULE EVENTS</h1>");
            pw.println("<p>Se otorga a <b>" + certificado.getNombreParticipante() + "</b></p>");
            pw.println("<p>Con la direccion de correo electrónico: <b>" + certificado.getCorreoParticipante() + "</b></p>");
            pw.println("<p>Por su participación en el evento <b>" + certificado.getCodigoEvento() + "</b></p>");
            pw.println("<p>Emitido el <b>" + certificado.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b></p>");
            pw.println("<p>¡Gracias por su participación!</p>");
            pw.println("</body></html>");
        }

        // Guardar en la base de datos
        String sqlInsert = "INSERT INTO certificado (correo_participante, codigo_evento, fecha_emision) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
            pstmt.setString(1, certificado.getCorreoParticipante());
            pstmt.setString(2, certificado.getCodigoEvento());
            pstmt.setDate(3, new Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
        }

        System.out.println("Certificado generado y registrado correctamente: " + nombreArchivo);
    }

    private boolean tieneAsistencia(String correo, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM asistencia a "
                + "JOIN actividad act ON a.codigo_actividad = act.codigo_actividad "
                + "WHERE a.correo_participante = ? AND act.codigo_evento = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, codigoEvento);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
