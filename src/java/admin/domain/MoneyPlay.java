/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;

import java.util.Date;

/**
 *
 * @author Developer
 */
public class MoneyPlay {
    private int gameid;
    private int sport;
    private Date date;
    private String time;
    private MoneyPlayParticipants visitor;
    private MoneyPlayParticipants home;

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MoneyPlayParticipants getVisitor() {
        return visitor;
    }

    public void setVisitor(MoneyPlayParticipants visitor) {
        this.visitor = visitor;
    }

    public MoneyPlayParticipants getHome() {
        return home;
    }

    public void setHome(MoneyPlayParticipants home) {
        this.home = home;
    }
    
}
