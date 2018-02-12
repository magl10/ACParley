/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.service;

import admin.context.Compress;
import admin.controller.GameController;
import admin.controller.TicketController;
import admin.domain.MoneyPlay;
import admin.domain.SummaryOffice;
import admin.domain.SummaryTicketAgent;
import admin.domain.SummaryTickets;
import java.util.List;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

/**
 *
 * @author dev00
 */
@Stateless
@Path("/report")
public class ServiceReport {

    
    @GET
    @Compress
    @Path("/summary/")
    public List<SummaryTickets> getreportsaleAll(@Context HttpServletRequest request){
        TicketController controller = new TicketController();
        return controller.getSummaryOffice((Integer)request.getSession().getAttribute("iduser"), "","");
    }
    @GET
    @Compress
    @Path("/summary/{from}/{to}")
    public List<SummaryTickets> getreportsale(@Context HttpServletRequest request,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
        return controller.getSummaryOffice((Integer)request.getSession().getAttribute("iduser"), from, to);
    }
    @GET
    @Compress
    @Path("/summary/{cierre}/{from}/{to}")
    public List<SummaryTickets> getreportsalebyCierre(@Context HttpServletRequest request,@PathParam("cierre")Integer cierre,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
            return controller.getSummaryOffice((Integer)request.getSession().getAttribute("iduser"), from, to);
    }
    @Compress
    @Path("/summaryagent/")
    public List<SummaryTicketAgent> getreportAgentsaleAll(@Context HttpServletRequest request){
        TicketController controller = new TicketController();
        return controller.getSummaryOfficeAgent((Integer)request.getSession().getAttribute("iduser"), "","");
    }
    @GET
    @Compress
    @Path("/summaryagent/{from}/{to}")
    public List<SummaryTicketAgent> getreportAgentsale(@Context HttpServletRequest request,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
        return controller.getSummaryOfficeAgent((Integer)request.getSession().getAttribute("iduser"), from, to);
    }
    @GET
    @Compress
    @Path("/summaryagent/{cierre}/{from}/{to}")
    public List<SummaryTicketAgent> getreportAgentsalebyCierre(@Context HttpServletRequest request,@PathParam("cierre")Integer cierre,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
            return controller.getSummaryOfficeAgent((Integer)request.getSession().getAttribute("iduser"), from, to);
    }
    @GET
    @Compress
    @Path("/summarytaqdetails/{from}/{to}")
    public List<SummaryTickets> getReportAgentTaq(@Context HttpServletRequest request,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
            return controller.getSummaryTaq((Integer)request.getSession().getAttribute("iduser"), from, to,null);
    }
    @GET
    @Compress
    @Path("/summaryagentgeneral/{from}/{to}")
    public List<SummaryTicketAgent> getReportByAgent(@Context HttpServletRequest request,@PathParam("from")String from, @PathParam("to")String to){
        TicketController controller = new TicketController();
            return controller.getSummaryOfficeTaq((Integer)request.getSession().getAttribute("iduser"), from, to,null);
    }
    @GET
    @Compress
    @Path("/summaryofficegeneral")
    public SummaryOffice getGeneralStatus(@Context HttpServletRequest request){
        TicketController controller = new TicketController();
            return controller.getSummayGeneralOffice((Integer)request.getSession().getAttribute("iduser"));
    }
    @GET
    @Compress
    @Path("/summaryofficegeneralweek")
    public List<SummaryOffice> getGeneralStatusweek(@Context HttpServletRequest request){
        TicketController controller = new TicketController();
            return controller.getSummayGeneralOfficeList((Integer)request.getSession().getAttribute("iduser"));
    }
   
}
