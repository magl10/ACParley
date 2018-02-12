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
public class PlayType {
    public PlayType(){
    }
    public PlayType(String type,int value){
        this.type = type;
        this.value = value;
    }
    private String type;   
    private int value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
