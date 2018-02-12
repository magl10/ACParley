/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;

import java.util.List;
/**
 * @author Yanny Hernandez
*/
public class Ticket {
    private Integer idticket=-1;
    private String dateCreation;
    private String status;
    private String descripcion;
    private Integer amount;
    private Integer winamount;
    private Integer riskamount;
    private String typebet;
    private String datePaid;
    private Boolean isPaid;
    private String agencia;
    private String descGame;

    public Boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getDescGame() {
        return descGame;
    }

    public void setDescGame(String descGame) {
        this.descGame = descGame;
    }
    public Integer getIdticket() {
        return idticket;
    }

    public void setIdticket(Integer idticket) {
        this.idticket = idticket;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getWinamount() {
        return winamount;
    }

    public void setWinamount(Integer winamount) {
        this.winamount = winamount;
    }

    public Integer getRiskamount() {
        return riskamount;
    }

    public void setRiskamount(Integer riskamount) {
        this.riskamount = riskamount;
    }

    public String getTypebet() {
        return typebet;
    }

    public void setTypebet(String typebet) {
        this.typebet = typebet;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setStatuspaid(Boolean statuspaid) {
        this.isPaid = statuspaid;
    }



    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
  
}   
