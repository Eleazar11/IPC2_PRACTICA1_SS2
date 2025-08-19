/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.time.LocalDate;

/**
 *
 * @author eleaz
 */
public class Certificado {

    private String correoParticipante;
    private String nombreParticipante;
    private String codigoEvento;
    private LocalDate fechaEmision;

    public Certificado(String correoParticipante, String nombreParticipante, String codigoEvento, LocalDate fechaEmision) {
        this.correoParticipante = correoParticipante;
        this.nombreParticipante = nombreParticipante;
        this.codigoEvento = codigoEvento;
        this.fechaEmision = fechaEmision;
    }

    public String getCorreoParticipante() {
        return correoParticipante;
    }

    public void setCorreoParticipante(String correoParticipante) {
        this.correoParticipante = correoParticipante;
    }

    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    

}
