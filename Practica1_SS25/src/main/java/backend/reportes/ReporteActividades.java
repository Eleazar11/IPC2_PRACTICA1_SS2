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
public class ReporteActividades {

    private Connection connection;

    public ReporteActividades(Connection connection) {
        this.connection = connection;
    }

    public void generar(String codigoEvento) throws Exception {
        // Cramos la carpeta reportes/actividades si no existe
        File carpeta = new File("reportes/actividades");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // aca definimos del archivo HTML
        String nombreArchivo = "reportes/actividades/reporte_" + codigoEvento + ".html";

        // Consulta a la DB
        String sql = "SELECT act.codigo_actividad, act.codigo_evento, act.titulo, act.correo_encargado, " +
                     "act.hora_inicio, act.cupo_maximo, COUNT(a.correo_participante) AS cantidad_participantes " +
                     "FROM actividad act " +
                     "LEFT JOIN asistencia a ON act.codigo_actividad = a.codigo_actividad " +
                     "WHERE act.codigo_evento = ? " +
                     "GROUP BY act.codigo_actividad, act.codigo_evento, act.titulo, act.correo_encargado, act.hora_inicio, act.cupo_maximo";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoEvento);
            ResultSet rs = pstmt.executeQuery();

            try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
                pw.println("<!DOCTYPE html>");
                pw.println("<html lang='es'>");
                pw.println("<head><meta charset='UTF-8'><title>Reporte de Actividades</title></head>");
                pw.println("<body style='font-family:Arial, sans-serif;'>");
                pw.println("<h1>Reporte de Actividades - Evento: " + codigoEvento + "</h1>");
                pw.println("<table border='1' cellpadding='5' cellspacing='0'>");
                pw.println("<tr><th>Código Actividad</th><th>Código Evento</th><th>Título</th><th>Encargado</th><th>Hora Inicio</th><th>Cupo Máximo</th><th>Cantidad de Participantes</th></tr>");

                while (rs.next()) {
                    pw.println("<tr>");
                    pw.println("<td>" + rs.getString("codigo_actividad") + "</td>");
                    pw.println("<td>" + rs.getString("codigo_evento") + "</td>");
                    pw.println("<td>" + rs.getString("titulo") + "</td>");
                    pw.println("<td>" + rs.getString("correo_encargado") + "</td>");
                    pw.println("<td>" + rs.getTime("hora_inicio") + "</td>");
                    pw.println("<td>" + rs.getInt("cupo_maximo") + "</td>");
                    pw.println("<td>" + rs.getInt("cantidad_participantes") + "</td>");
                    pw.println("</tr>");
                }

                pw.println("</table>");
                pw.println("</body></html>");
            }
        }

        System.out.println("Reporte de actividades generado correctamente: " + nombreArchivo);
    }
}

