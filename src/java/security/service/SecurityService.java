/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security.service;

import admin.context.CommonUtils;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import security.controller.SecurityController;
import security.domain.Authentication;
import security.domain.Credential;

/**
 *
 * @author Yanny Hernandez
 */
@Stateless
@Path("/security")
public class SecurityService {
    @Context
    private HttpServletRequest request;
    private final SecurityController controller = new SecurityController();
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/login")
    public Authentication logon(Credential credential){
        Authentication resp = controller.logon(credential.getUser(), credential.getPass());
        if(resp.isSuccess()){
            HttpSession session  = request.getSession(true);
            session.setAttribute("Authorization","Basic "+CommonUtils.encodeBase64(credential.getUser()+":"+credential.getPass()));
            session.setAttribute("iduser", resp.getLogin().getOfficeid());
        }
        return resp;
    }
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/changepass/{newpass}")
    public Authentication changePassword(@PathParam("newpass")String newpass,Credential credential){
        Authentication resp = controller.changepass(credential.getUser(),credential.getPass(),newpass);
        if(resp.isSuccess()){
            HttpSession session  = request.getSession(true);
            session.setAttribute("Authorization","Basic "+CommonUtils.encodeBase64(credential.getUser()+":"+credential.getPass()));
        }
        return resp;
    }
}
