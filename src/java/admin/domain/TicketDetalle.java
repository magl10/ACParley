/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;
/**
 *
 * @author Yanny Hernandez
 */
public class TicketDetalle {
    private String status;
    private String descripcion;
    private Integer odd;
    private Float cant;
    private String dategame;
    private Integer idgame;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOdd() {
        return odd;
    }

    public void setOdd(Integer odd) {
        this.odd = odd;
    }

    public Float getCant() {
        return cant;
    }

    public void setCant(Float cant) {
        this.cant = cant;
    }

    public String getDategame() {
        return dategame;
    }

    public void setDategame(String dategame) {
        this.dategame = dategame;
    }

    public Integer getIdgame() {
        return idgame;
    }

    public void setIdgame(Integer idgame) {
        this.idgame = idgame;
    }
    
}
