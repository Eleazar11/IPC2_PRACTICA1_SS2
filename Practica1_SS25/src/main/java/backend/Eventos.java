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
public class Eventos {
    private String codigoEvento;
    private LocalDate fechaEvento;
    private TipoEvento tipoEvento;
    private String ubicacion;  // <= 150 caracteres
    private String tituloEvento;
    private int cupoMaximo;

    public Eventos(String codigoEvento, LocalDate fechaEvento, TipoEvento tipoEvento, String ubicacion, String tituloEvento, int cupoMaximo) {
        this.codigoEvento = codigoEvento;
        this.fechaEvento = fechaEvento;
        this.tipoEvento = tipoEvento;
        this.ubicacion = ubicacion;
        this.tituloEvento = tituloEvento;
        this.cupoMaximo = cupoMaximo;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    
    
    
}
