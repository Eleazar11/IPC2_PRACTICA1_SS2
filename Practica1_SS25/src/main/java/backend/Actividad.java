/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backend;

import java.time.LocalTime;

/**
 *
 * @author eleaz
 */
public class Actividad {

    private String codigoActividad;
    private String codigoEvento;
    private String titulo;
    private TipoActividad tipoActividad;
    private String correoEncargado;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cupoMaximo;

    public Actividad(String codigoActividad, String codigoEvento, String titulo, TipoActividad tipoActividad, String correoEncargado, LocalTime horaInicio, LocalTime horaFin, int cupoMaximo) {
        this.codigoActividad = codigoActividad;
        this.codigoEvento = codigoEvento;
        this.titulo = titulo;
        this.tipoActividad = tipoActividad;
        this.correoEncargado = correoEncargado;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cupoMaximo = cupoMaximo;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(TipoActividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getCorreoEncargado() {
        return correoEncargado;
    }

    public void setCorreoEncargado(String correoEncargado) {
        this.correoEncargado = correoEncargado;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    
    

}
