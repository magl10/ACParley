/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.service;

import admin.controller.AgenteController;
import admin.domain.Agente;
import admin.domain.StateAgencia;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
/**
 *
 * @author Yanny Hernandez
 */
@Stateless 
@Path("/agent")
public class ServiceAgent {
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/list")
    public List<Agente> getAgentByOffice(@Context HttpServletRequest request){
         AgenteController controller = new AgenteController();
         return controller.getListAgentbyOffice((Integer)request.getSession().getAttribute("iduser")); 
    } 
    
    @POST
    @Path("/edit/")
    public String edit(Agente agent){
        AgenteController controller = new AgenteController();
        return controller.edit(agent);
    }
    @POST
    @Path("/create/{pass}")
    public String create(@Context HttpServletRequest request,@PathParam("pass")String pass, Agente agent){
        AgenteController controller = new AgenteController();
        agent.setOffice((Integer)request.getSession().getAttribute("iduser"));
        if(controller.create(agent, pass)==null){
            return ":( hubo un Error Creando la Taquilla";
        }else{
            return ":) Correcta Creacion de Usuario";
        }
    }
}
