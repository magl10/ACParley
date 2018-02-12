/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.domain;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Developer
 */
public class MoneyPlayParticipants {
        private int rotation;
        private String name;
        private List<PlayType> playsRisk;
        private List<PlayType> playsWin;

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayType> getPlaysRisk() {
        return playsRisk;
    }

    public void setPlaysRisk(List<PlayType> playsRisk) {
        this.playsRisk = playsRisk;
    }

    public List<PlayType> getPlaysWin() {
        return playsWin;
    }

    public void setPlaysWin(List<PlayType> playsWin) {
        this.playsWin = playsWin;
    }

}
