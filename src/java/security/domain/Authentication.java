/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security.domain;

import admin.domain.Office;

/**
 *
 * @author dev00
 */
public class Authentication {
    private Boolean success = false;
    private String message ="";
    private Office Login;

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Office getLogin() {
        return Login;
    }

    public void setLogin(Office Login) {
        this.Login = Login;
    }
}
