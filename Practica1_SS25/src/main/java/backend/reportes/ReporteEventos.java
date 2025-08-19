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
public class ReporteEventos {

    private Connection conexion;

    public ReporteEventos(Connection conexion) {
        this.conexion = conexion;
    }

    public void generarReporte(String codigoEvento, String tipoEvento) {
        String sql = "SELECT e.codigo_evento, e.fecha_evento, e.titulo, e.tipo_evento, "
                + "e.ubicacion, e.cupo_maximo, p.correo, p.nombre_completo, p.tipo_participante "
                + "FROM evento e "
                + "INNER JOIN asistencia a ON e.codigo_evento = a.codigo_actividad "
                + "INNER JOIN participante p ON a.correo_participante = p.correo "
                + "WHERE e.codigo_evento = ? AND e.tipo_evento = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            ps.setString(2, tipoEvento);

            ResultSet rs = ps.executeQuery();

            // Crear carpeta reportes/eventos si no existe
            File carpeta = new File("reportes/eventos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String nombreArchivo = "reportes/eventos/reporte_" + codigoEvento + ".html";

            StringBuilder html = new StringBuilder();
            html.append("<html><body style='font-family:Arial, sans-serif;'>");
            html.append("<h2>REPORTE DE EVENTO</h2>");

            html.append("<table border='0' cellspacing='0' cellpadding='5'>");
            html.append("<tr><td><b>CÓDIGO DE EVENTO:</b></td><td></td></tr>");
            html.append("<tr><td><b>FECHA DE EVENTO:</b></td><td></td></tr>");
            html.append("<tr><td><b>TÍTULO DEL EVENTO:</b></td><td></td></tr>");
            html.append("<tr><td><b>TIPO DE EVENTO:</b></td><td></td></tr>");
            html.append("<tr><td><b>UBICACIÓN:</b></td><td></td></tr>");
            html.append("<tr><td><b>CUPO MÁXIMO:</b></td><td></td></tr>");
            html.append("</table><br>");

            html.append("<table border='1' cellspacing='0' cellpadding='5'>");
            html.append("<tr>")
                    .append("<th>CORREO DEL PARTICIPANTE</th>")
                    .append("<th>NOMBRE DEL PARTICIPANTE</th>")
                    .append("<th>TIPO DE PARTICIPANTE</th>")
                    .append("<th>MÉTODO DE PAGO</th>")
                    .append("<th>MONTO PAGADO</th>")
                    .append("</tr>");

            html.append("</table><br>");

            html.append("<b>MONTO TOTAL:</b> Q.<br>");
            html.append("<b>PARTICIPANTES VALIDADOS:</b> <br>");
            html.append("<b>PARTICIPANTES NO VALIDADOS:</b> <br>");

            html.append("</body></html>");

            try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
                pw.println(html.toString());
            }

            System.out.println("Reporte de evento generado correctamente: " + nombreArchivo);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
