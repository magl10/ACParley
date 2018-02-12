/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.service;

import admin.context.CommonUtils;
import admin.context.Compress;
import admin.controller.GameController;
import admin.domain.Game;
import admin.domain.MoneyPlay;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

/**
 *
 * @author Yanny Hernandez
 */
@Stateless
@Path("/game")
public class ServiceGame {
    private final GameController controller = new GameController();
    @GET
    @Compress
    @Path("/list/{from}")
    public List<Game> getGamesbyDate(@Context HttpServletRequest request, @PathParam("from") String from){
           Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null){
          if(from ==null) from = null;
         return controller.getGamebydate(iduser,CommonUtils.convertStrToShortDate(from));
      }else{
          return new ArrayList<>();
      }
    }
    @GET
    @Compress
    @Path("/list")
    public List<Game> getGames(@Context HttpServletRequest request){
           Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null){
         return controller.getGamebydate(iduser,CommonUtils.convertStrToShortDate(""));
      }else{
          return new ArrayList<>();
      }
    }
      @GET
    @Compress
    @Path("/moneyreport")
    public  List<MoneyPlay> getMoneyReport(@Context HttpServletRequest request){
        GameController controller = new GameController();
        return controller.getPlaysMoney((Integer)request.getSession().getAttribute("iduser"));
    }
}
