/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.service;

import admin.context.Compress;
import admin.controller.PlayerController;
import admin.domain.Agencia;
import admin.domain.EditAgencia;
import admin.domain.StateAgencia;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import security.domain.Permission;

/**
 *
 * @author Yanny Hernandez
 */
@Stateless
@Path("/player")
public class ServicePlayer {
    @Context
    SecurityContext securityContext;
    

    @GET
    @Compress
    @Path("/state/{idagente}")
    @Permission(service = "Player",description = "Monitor de Ventas",method = "getStateAgencia")
    public List<StateAgencia> getStateAgencia(@PathParam("idagente")Integer idagente){
        PlayerController controller = new PlayerController();
        return controller.getStateAgencia(idagente);
    }
    @GET
    @Compress
    @Path("/")
    @Permission(service = "Player", description = "Obtener Taquilla o Agencia",method = "getAgencia")
    public List<Agencia> getAgencias(@Context HttpServletRequest request){
      PlayerController controller = new PlayerController();
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
        return controller.getAgenciaByOffice(iduser);
      else
          return  new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/searchbyuser/{username}")
    public Agencia getAgenciabyusername(@PathParam("username")String username){
         PlayerController controller = new PlayerController();
        return controller.getAgenciaByUserName(username);
    }
    @GET
    @Compress
    @Path("/searchbyid/{idagencia}")
    public Agencia getAgenciabyid(@PathParam("idagencia")Integer idagencia){
         PlayerController controller = new PlayerController();
        return controller.getAgenciaByUserName(idagencia);
    }
    @POST
    @Compress
    @Path("/free/{idagencia}")
    public boolean freeAgencia(@PathParam("idagencia")Integer idagencia){
        PlayerController controller = new PlayerController();
        return controller.freeAgencia(idagencia);
    }
    @POST
    @Compress
    @Path("/changestat/{idagencia}")
    public boolean changeStatAgencia(@PathParam("idagencia")Integer idagencia){
        PlayerController controller = new PlayerController();
        return controller.changeStat(idagencia);
    }
    @POST
    @Compress
    @Path("/resetpass/{idagencia}")
    public boolean resetPass(@PathParam("idagencia")Integer idagencia){
        PlayerController controller = new PlayerController();
        return controller.resetPass(idagencia);
    }
    @POST
    @Compress
    @Consumes("application/json")
    @Path("/update")
    public Agencia changeStatAgencia(Agencia edit){
        PlayerController controller = new PlayerController();
        return controller.edit(edit);
    }
     @POST
    @Compress
    @Consumes("application/json")
    @Path("/create")
    public Agencia create(@Context HttpServletRequest request,Agencia player){
        PlayerController controller = new PlayerController();
        player.setIdOffice((Integer)request.getSession().getAttribute("iduser"));
        return controller.CreateAgencia(player);
    }
}
