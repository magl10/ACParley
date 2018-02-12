/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.service;

import admin.context.Compress;
import admin.controller.TicketController;
import admin.domain.SummarySale;
import admin.domain.Ticket;
import admin.domain.TicketDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

/**
 *
 * @author Yanny Hernandez
 */
@Stateless
@Path("/ticket")
public class ServiceTicket {

    private final TicketController controller = new TicketController();
    @GET
    @Compress
    @Path("/list/{from}/{to}/{idagencia}")
    public List<Ticket> getTicketbyAgencia(@Context HttpServletRequest request, @PathParam("from") String from, @PathParam("to") String to,@PathParam("idagencia") Integer idagencia){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getTicketOffice(iduser, from, to, idagencia);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/list/{from}/{to}")
    public List<Ticket> getTicket(@Context HttpServletRequest request, @PathParam("from") String from, @PathParam("to") String to){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getTicketOffice(iduser, from, to, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/list/{from}")
    public List<Ticket> getTicketfrom(@Context HttpServletRequest request, @PathParam("from") String from){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getTicketOffice(iduser, from, null, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/list")
    public List<Ticket> getTicketMonth(@Context HttpServletRequest request){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getTicketOffice(iduser, null, null, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/find/{numero}")
    public List<Ticket> getTicketMonth(@Context HttpServletRequest request,@PathParam("numero")Integer numero){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
           return controller.getTicket(iduser, numero);
      else
          return new ArrayList<>();
    }
     @GET
    @Path("/listdeleted/{from}/{to}/{idagencia}")
    public List<Ticket> getTicketbydeletedAgencia(@Context HttpServletRequest request, @PathParam("from") String from, @PathParam("to") String to,@PathParam("idagencia") Integer idagencia){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getListTicketbydeleted(iduser, from, to, idagencia);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/win/{from}/{to}/{user}/{cierre}")
    public List<Ticket> getTicketWinner(@Context HttpServletRequest request, @PathParam("from") String from, @PathParam("to") String to,@PathParam("user") String user,@PathParam("cierre") int cierre){
        Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
         return   controller.getTicketPaid(user, from, to, cierre);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/listdeleted/{from}/{to}")
    public List<Ticket> getTicketdeleted(@Context HttpServletRequest request, @PathParam("from") String from, @PathParam("to") String to){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getListTicketbydeleted(iduser, from, to, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/listdeleted/{from}")
    public List<Ticket> getTicketfromdeleted(@Context HttpServletRequest request, @PathParam("from") String from){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getListTicketbydeleted(iduser, from, null, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/listdeleted")
    public List<Ticket> getTicketMonthdeleted(@Context HttpServletRequest request){
      Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getListTicketbydeleted(iduser, null, null, null);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/details/{id}")
    public List<TicketDetalle> getDetailsTickets(@PathParam("id") Integer id){

      if(id!=null)
            return   controller.getDetailsbyid(id);
      else
          return new ArrayList<>();
    }
    @GET
    @Path("/detailsdeleted/{id}")
    public List<TicketDetalle> getDetailsTicketsDeleted(@PathParam("id") Integer id){

      if(id!=null)
     return   controller.getDetailsDeletedbyid(id);
      else
          return new ArrayList<>();
    }
    @GET
    @Compress
    @Path("/ticketgame/{gameid}")
    public List<Ticket> getTicketsGame(@Context HttpServletRequest request,@PathParam("gameid") Integer gameid){
    Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
     return   controller.getListTicketbyGame(gameid,iduser);
      else
          return new ArrayList<>();
    }
    @DELETE
    @Compress
    @Path("/delete/{ticket}")
    public boolean deletedTicket(@Context HttpServletRequest request,@PathParam("ticket") Integer ticket){
    Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
            return   controller.deleteTicket(iduser,ticket);
      else
          return false;
    }
    @POST
    @Compress
    @Path("/paid/{ticket}")
    public boolean paidTicket(@Context HttpServletRequest request,@PathParam("ticket") Integer ticket){
    Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
      if(iduser!=null)
            return   controller.paidTicket(+iduser,ticket);
      else
          return false;
    }
    @GET
    @Compress
    @Path("/hourlastsale")
    public List<SummarySale> getHourLastSale(@Context HttpServletRequest request){
        TicketController controllers = new TicketController();
        Integer iduser  = (Integer)request.getSession().getAttribute("iduser");
          if(iduser!=null)
                return   controllers.getLastSale(iduser);
          else
              return new ArrayList<>();
    }
}
