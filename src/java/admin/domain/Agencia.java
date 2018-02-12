/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;

import java.util.Date;


/**
 *
 * @author Yanny Hernadez
 */
public class Agencia {
    private Integer idagencia;
    private Date datecreation;
    private String username;
    private String nameagencia;
    private String pass;
    private Float limitderecho;
    private Float limitparlay;
    private Float limitcombina;
    private Integer cierre;
    private Integer typeprint;
    private Boolean istaquilla;
    private Boolean isprepaid;
    private Boolean isprint;
    private Float partipacion;
    private Float porcentajeganancia;
    private Float comisionderecho;
    private Float comisionparlay;
    private String nameagente;
    private Float creditlimit;
    private Float balance;
    private Float atrisk;
    private Float maxbet;
    private Date lastbet;
    private Float limitsaleday;
    private Boolean isactiva;
    private int proporcion;
    private String responsable;
    private int idOffice;
    private boolean  pardos;
    private String phone;
    private int idAgent;

    public int getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }

    public boolean isPardos() {
        return pardos;
    }

    public void setPardos(boolean pardos) {
        this.pardos = pardos;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private String email;
    public int getIdOffice() {
        return idOffice;
    }

    public void setIdOffice(int idOffice) {
        this.idOffice = idOffice;
    }
    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public int getProporcion() {
        return proporcion;
    }

    public void setProporcion(int proporcion) {
        this.proporcion = proporcion;
    }
     public Agencia(){
     }

    public Integer getIdagencia() {
        return idagencia;
    }

    public void setIdagencia(Integer idagencia) {
        this.idagencia = idagencia;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameagencia() {
        return nameagencia;
    }

    public void setNameagencia(String nameagencia) {
        this.nameagencia = nameagencia;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Float getLimitderecho() {
        return limitderecho;
    }

    public void setLimitderecho(Float limitderecho) {
        this.limitderecho = limitderecho;
    }

    public Float getLimitparlay() {
        return limitparlay;
    }

    public void setLimitparlay(Float limiyparlay) {
        this.limitparlay = limiyparlay;
    }

    public Float getLimitcombina() {
        return limitcombina;
    }

    public void setLimitcombina(Float limitcombina) {
        this.limitcombina = limitcombina;
    }

    public Integer getCierre() {
        return cierre;
    }

    public void setCierre(Integer cierre) {
        this.cierre = cierre;
    }

    public Integer getTypeprint() {
        return typeprint;
    }

    public void setTypeprint(Integer typeprint) {
        this.typeprint = typeprint;
    }

    public Boolean isIstaquilla() {
        return istaquilla;
    }

    public void setIstaquilla(Boolean istaquilla) {
        this.istaquilla = istaquilla;
    }

    public Boolean isIsprepaid() {
        return isprepaid;
    }

    public void setIsprepaid(Boolean isprepaid) {
        this.isprepaid = isprepaid;
    }

    public Boolean isIsprint() {
        return isprint;
    }

    public void setIsprint(Boolean isprint) {
        this.isprint = isprint;
    }

    public Float getPartipacion() {
        return partipacion;
    }

    public void setPartipacion(Float partipacion) {
        this.partipacion = partipacion;
    }

    public Float getPorcentajeganancia() {
        return porcentajeganancia;
    }

    public void setPorcentajeganancia(Float porcentajeganancia) {
        this.porcentajeganancia = porcentajeganancia;
    }

    public Float getComisionderecho() {
        return comisionderecho;
    }

    public void setComisionderecho(Float comisionderecho) {
        this.comisionderecho = comisionderecho;
    }

    public Float getComisionparlay() {
        return comisionparlay;
    }

    public void setComisionparlay(Float comisionparlay) {
        this.comisionparlay = comisionparlay;
    }

    public String getNameagente() {
        return nameagente;
    }

    public void setNameagente(String nameagente) {
        this.nameagente = nameagente;
    }

    public Float getCreditlimit() {
        return creditlimit;
    }

    public void setCreditlimit(Float creditlimit) {
        this.creditlimit = creditlimit;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getAtrisk() {
        return atrisk;
    }

    public void setAtrisk(Float atrisk) {
        this.atrisk = atrisk;
    }

    public Float getMaxbet() {
        return maxbet;
    }

    public void setMaxbet(Float maxbet) {
        this.maxbet = maxbet;
    }

    public Date getLastbet() {
        return lastbet;
    }

    public void setLastbet(Date lastbet) {
        this.lastbet = lastbet;
    }

    public Float getLimitsaleday() {
        return limitsaleday;
    }

    public void setLimitsaleday(Float limitsaleday) {
        this.limitsaleday = limitsaleday;
    }

    public Boolean isIsactiva() {
        return isactiva;
    }

    public void setIsactiva(Boolean isactiva) {
        this.isactiva = isactiva;
    }
    
}
