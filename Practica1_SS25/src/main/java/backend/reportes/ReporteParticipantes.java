/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend.reportes;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author eleaz
 */
public class ReporteParticipantes {

    private Connection connection;

    public ReporteParticipantes(Connection connection) {
        this.connection = connection;
    }

    public void generar(String codigoEvento) throws Exception {
        // Crear carpeta reportes/participantes si no existe
        File carpeta = new File("reportes/participantes");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Nombre del archivo HTML
        String nombreArchivo = "reportes/participantes/reporte_" + codigoEvento + ".html";

        // Consulta a la BD
        String sql = "SELECT correo, tipo_participante, nombre_completo, institucion, participante_validado " +
                     "FROM participante " +
                     "WHERE correo IN (SELECT correo_participante FROM asistencia a " +
                     "JOIN actividad act ON a.codigo_actividad = act.codigo_actividad " +
                     "WHERE act.codigo_evento = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoEvento);
            ResultSet rs = pstmt.executeQuery();

            try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
                pw.println("<!DOCTYPE html>");
                pw.println("<html lang='es'>");
                pw.println("<head><meta charset='UTF-8'><title>Reporte de Participantes</title></head>");
                pw.println("<body style='font-family:Arial, sans-serif;'>");
                pw.println("<h1>Reporte de Participantes - Evento: " + codigoEvento + "</h1>");
                pw.println("<table border='1' cellpadding='5' cellspacing='0'>");
                pw.println("<tr><th>Correo</th><th>Tipo</th><th>Nombre Completo</th><th>Institución</th><th>Validado</th></tr>");

                while (rs.next()) {
                    pw.println("<tr>");
                    pw.println("<td>" + rs.getString("correo") + "</td>");
                    pw.println("<td>" + rs.getString("tipo_participante") + "</td>");
                    pw.println("<td>" + rs.getString("nombre_completo") + "</td>");
                    pw.println("<td>" + rs.getString("institucion") + "</td>");
                    pw.println("<td>" + (rs.getBoolean("participante_validado") ? "Sí" : "No") + "</td>");
                    pw.println("</tr>");
                }

                pw.println("</table>");
                pw.println("</body></html>");
            }
        }

        System.out.println("Reporte de participantes generado correctamente: " + nombreArchivo);
    }
}
