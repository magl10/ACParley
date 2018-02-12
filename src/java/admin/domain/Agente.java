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
public class Agente {
    private Integer idagente;
    private String nameagente;
    private String username;
    private String grouping;
    private String address;
    private String telf;
    private String email;
    private Float alquiler;
    private Float derecho;
    private Float parlay;
    private Float comision;
    private Integer Office;
    private Integer idprofile;
    private Integer cierre;
    private Float participacion;
    private Float ganancia;

    public Float getParticipacion() {
        return participacion;
    }

    public void setParticipacion(Float participacion) {
        this.participacion = participacion;
    }

    public Float getGanancia() {
        return ganancia;
    }

    public void setGanancia(Float ganancia) {
        this.ganancia = ganancia;
    }
    
    public Integer getCierre() {
        return cierre;
    }

    public void setCierre(Integer cierre) {
        this.cierre = cierre;
    }
    public Agente() {
    }

    public Agente(Integer idagente, String nameagente, String pass, String grouping, String address, String telf, String email,  Float alquiler, Float comision) {
        this.idagente = idagente;
        this.nameagente = nameagente;
        this.grouping = grouping;
        this.address = address;
        this.telf = telf;
        this.email = email;
        this.alquiler = alquiler;
        this.comision = comision;
    }

    public Integer getIdagente() {
        return idagente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIdagente(Integer idagente) {
        this.idagente = idagente;
    }

    public String getNameagente() {
        return nameagente;
    }

    public void setNameagente(String nameagente) {
        this.nameagente = nameagente;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelf() {
        return telf;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Float getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(Float alquiler) {
        this.alquiler = alquiler;
    }

    public Float getComision() {
        return comision;
    }

    public void setComision(Float comision) {
        this.comision = comision;
    }

    public Integer getOffice() {
        return Office;
    }

    public void setOffice(Integer Office) {
        this.Office = Office;
    }


    public Integer getIdprofile() {
        return idprofile;
    }

    public void setIdprofile(Integer idprofile) {
        this.idprofile = idprofile;
    }

    public Float getDerecho() {
        return derecho;
    }

    public void setDerecho(Float derecho) {
        this.derecho = derecho;
    }

    public Float getParlay() {
        return parlay;
    }

    public void setParlay(Float parlay) {
        this.parlay = parlay;
    }
    
}
