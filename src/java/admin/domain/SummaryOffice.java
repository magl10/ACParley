/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;


/**
 *
 * @author Developer
 */
public class SummaryOffice {
    private int idOffice;
    private String date;
    private int tickets;
    private Float sale;
    private Float winPaid;
    private int playersPlay;
    private int ticketsD;
    private int ticketsP;
    private Float saleDerecho;
    private Float saleParlay;
    public int getIdOffice() {
        return idOffice;
    }

    public void setIdOffice(int idOffice) {
        this.idOffice = idOffice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public Float getSale() {
        return sale;
    }

    public void setSale(Float sale) {
        this.sale = sale;
    }

    public Float getWinPaid() {
        return winPaid;
    }

    public void setWinPaid(Float winPaid) {
        this.winPaid = winPaid;
    }

    public int getPlayersPlay() {
        return playersPlay;
    }

    public void setPlayersPlay(int playersPlay) {
        this.playersPlay = playersPlay;
    }

    public int getTicketsD() {
        return ticketsD;
    }

    public void setTicketsD(int ticketsD) {
        this.ticketsD = ticketsD;
    }

    public int getTicketsP() {
        return ticketsP;
    }

    public void setTicketsP(int ticketsP) {
        this.ticketsP = ticketsP;
    }

    public Float getSaleDerecho() {
        return saleDerecho;
    }

    public void setSaleDerecho(Float saleDerecho) {
        this.saleDerecho = saleDerecho;
    }

    public Float getSaleParlay() {
        return saleParlay;
    }

    public void setSaleParlay(Float saleParlay) {
        this.saleParlay = saleParlay;
    }
    
}
