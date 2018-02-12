/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.domain;

/**
 *
 * @author dev00
 */
public class StateAgencia {
    private int paid;
    private int ticket;
    private int nopaid;
    private Agencia agencia;

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public int getNopaid() {
        return nopaid;
    }

    public void setNopaid(int nopaid) {
        this.nopaid = nopaid;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

}
