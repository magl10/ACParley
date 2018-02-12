/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.controller;

import admin.context.CommonUtils;
import admin.domain.Agencia;
import admin.domain.StateAgencia;
import admin.domain.SummaryOffice;
import admin.domain.SummarySale;
import admin.domain.SummaryTicketAgent;
import admin.domain.SummaryTickets;
import admin.domain.Ticket;
import admin.domain.TicketDetalle;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Yanny Hernandez
 */
public class TicketController {
      
      public TicketController(){
      
      }
       public StateAgencia getTicketStateAgencia(Agencia agencia){
           return new StateAgencia();
      }
      public List<Ticket> getTicketPaid(String user,String from, String to,int cierre){
          List<Ticket> lista = new ArrayList<>();
          
          try {
              Date datef = CommonUtils.convertStrToShortDate(from);
              Date datet = CommonUtils.convertStrToShortDateEndHour(to);
              Connection cnn = CommonUtils.getConnection();
              String query= "SELECT * from vwTicketSummary";
              if(cierre==1)
                  query += " where  stat = 2 and placeddate between ? and ? and username = ?";
              else
                  query += " where stat = 2 and paiddate   between ? and ? and username = ?";
              PreparedStatement statement  = cnn.prepareStatement(query);
              statement.setDate(1,new java.sql.Date(datef.getTime()));
              statement.setTimestamp(2,new java.sql.Timestamp(datet.getTime()));
              statement.setString(3, user);
              ResultSet resp = statement.executeQuery();
              while (resp.next()) {
                    lista.add(createTicket(resp));
            }
                 resp.close();
        cnn.close();
          }catch(SQLException ex){
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
          return lista;
      }
      public List<SummarySale> getLastSale(Integer idOffice){
        List<SummarySale> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwSummarySaleHour where officeid =?  and placeddate >= dateadd(hour,-6,getdate())  order by placeddate,hora,minutos";
        try {
            PreparedStatement statement = cnn.prepareStatement(query);
            statement.setInt(1, idOffice);
               
            ResultSet resp = statement.executeQuery();
            List<TicketDetalle> detalles = new ArrayList<>();
            while (resp.next()) {
                SummarySale sale = new SummarySale();
                Calendar calendar = Calendar.getInstance();
                Date date =resp.getDate("placeddate");
                calendar.setTime(date);
                sale.setHour(resp.getInt("hora"));
                sale.setMin(resp.getInt("minutos"));
                sale.setSale(resp.getFloat("venta"));
                lista.add(sale);
            }
               resp.close();
        cnn.close();
             } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
      }
      public List<Ticket> getTicketGame(Integer idagente,Integer idgame){
        List<Ticket> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where agentid =?  and gameid = ? order by 1 ";
        try {
            
            PreparedStatement statement = cnn.prepareStatement(query);
            
            statement.setInt(1, idagente);
            statement.setInt(2,idgame);
               
            ResultSet resp = statement.executeQuery();
            List<TicketDetalle> detalles = new ArrayList<>();
            while (resp.next()) {
                    lista.add(createTicket(resp));
            }
               resp.close();
        cnn.close();
             } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
      }
      public List<SummaryTickets> getSummaryAgentbyCierre(Integer idAgente,String pfrom, String pto,Integer idagencia,Integer cierre){
          List<SummaryTickets> lista = new ArrayList<>();
           Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where agentid =? and (placeddate between ? and ? or paiddate between ?  and ?)";
        PreparedStatement cmd;
          try {
              if(idagencia==null)
              cmd = cnn.prepareStatement(query + " order by username");
              else{
                  cmd = cnn.prepareStatement(query + " and playerid = ? order by username");
                  cmd.setInt(4, idagencia);
              }
        
        cmd.setInt(1, idAgente);
        cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
        cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
        cmd.setTimestamp(4, new Timestamp(dateFrom.getTime()));
        cmd.setTimestamp(5, new Timestamp(dateTo.getTime()));
        ResultSet resp = cmd.executeQuery();
        float derecho=0,parlay=0,participacion=0,ganancia=0;
        SummaryTickets temp = new SummaryTickets();
        while (resp.next()) {
              if(!resp.getString("username").toLowerCase().equals(temp.getUsername().toLowerCase())){
                derecho = resp.getBigDecimal("derecho")==null?0:resp.getBigDecimal("derecho").floatValue();
                parlay = resp.getBigDecimal("parlay")==null?0:resp.getBigDecimal("parlay").floatValue();
                participacion = resp.getBigDecimal("participacion")==null?0:resp.getBigDecimal("participacion").floatValue();
                ganancia = resp.getBigDecimal("ganancia")==null?0:resp.getBigDecimal("ganancia").floatValue();
                calculaSum(temp,derecho,parlay,participacion,ganancia);
                temp = new  SummaryTickets();
                temp.setAgencia(resp.getString("agencia"));
                temp.setUsername(resp.getString("username"));
                temp.setCanttickets(0);
                lista.add(temp);
              }
                  temp.setCanttickets(temp.getCanttickets()+1);
                  Date date=resp.getTimestamp("placeddate");
                  Date datePaid=resp.getTimestamp("paiddate");
                  if(date.getTime()>=dateFrom.getTime()&&date.getTime()<=dateTo.getTime()){
                    if(datePaid!=null)
                    acumValue(temp, resp,cierre==1||datePaid.getTime()>=dateFrom.getTime()&&datePaid.getTime()<=dateTo.getTime());
                    else
                    acumValue(temp, resp,false);
                  }else{
                    acumValuePremio(temp, resp);
                  }
        }
        calculaSum(temp,derecho,parlay,participacion,ganancia);
           resp.close();
        cnn.close();
          } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
    
        return lista;
      }
       public List<SummaryTickets> getSummaryOffice(Integer idOffice,String pfrom, String pto){
          List<SummaryTickets> lista = new ArrayList<>();
           Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where officeid =? and placeddate between ? and ?";
        PreparedStatement cmd;
          try {
            cmd = cnn.prepareStatement(query + " order by username");
            cmd.setInt(1, idOffice);
            cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
            cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
            ResultSet resp = cmd.executeQuery();
            float derecho=0,parlay=0,participacion=0,ganancia=0;
            SummaryTickets temp = new SummaryTickets();
            while (resp.next()) {
                if(!resp.getString("username").toLowerCase().equals(temp.getUsername().toLowerCase())){
                    calculaSum(temp,derecho,parlay,participacion,ganancia);
                    derecho = resp.getBigDecimal("derecho")==null?0:resp.getBigDecimal("derecho").floatValue();
                    parlay = resp.getBigDecimal("parlay")==null?0:resp.getBigDecimal("parlay").floatValue();
                    participacion = resp.getBigDecimal("participacion")==null?0:resp.getBigDecimal("participacion").floatValue();
                    ganancia = resp.getBigDecimal("ganancia")==null?0:resp.getBigDecimal("ganancia").floatValue();
                    temp = new  SummaryTickets();
                    temp.setAgencia(resp.getString("agencia"));
                    temp.setUsername(resp.getString("username"));
                    temp.setAgente(resp.getString("agent"));
                    temp.setCanttickets(0);
                    lista.add(temp);
                }
                temp.setCanttickets(temp.getCanttickets()+1);
                acumValue(temp, resp);
            }
            calculaSum(temp,derecho,parlay,participacion,ganancia);
               resp.close();
        cnn.close();
          }catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
      }
      public List<SummaryTicketAgent> getSummaryOfficeAgent(Integer idOffice,String pfrom, String pto){
          List<SummaryTicketAgent> lista = new ArrayList<>();
           Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where officeid =? and placeddate between ? and ? ";
        PreparedStatement cmd;
          try {
            cmd = cnn.prepareStatement(query + " order by agent");
            cmd.setInt(1, idOffice);
            cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
            cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
            ResultSet resp = cmd.executeQuery();
            float derecho=0,parlay=0,participacion=0,ganancia=0;
            SummaryTicketAgent temp = new SummaryTicketAgent();
            while (resp.next()) {
                if(!resp.getString("agent").toLowerCase().equals(temp.getAgente().toLowerCase())){
                    calculaSumAgent(temp,derecho,parlay,participacion,ganancia);
                    derecho = resp.getBigDecimal("derechoagent")==null?0:resp.getBigDecimal("derechoagent").floatValue();
                    parlay = resp.getBigDecimal("parleyagent")==null?0:resp.getBigDecimal("parleyagent").floatValue();
                    participacion = resp.getBigDecimal("COMMPCT")==null?0:resp.getBigDecimal("COMMPCT").floatValue();
                    ganancia = resp.getBigDecimal("comagente")==null?0:resp.getBigDecimal("comagente").floatValue();
                    temp = new  SummaryTicketAgent();
                    temp.setAgente(resp.getString("agent"));
                    temp.setIdAgent(resp.getInt("agentid"));
                    temp.setCanttickets(0);
                    lista.add(temp);
                }
                temp.setCanttickets(temp.getCanttickets()+1);
                acumValueAgent(temp, resp);
            }
            calculaSumAgent(temp,derecho,parlay,participacion,ganancia);
               resp.close();
        cnn.close();
          }catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
      }
      public List<SummaryTickets> getSummaryAgent(Integer idAgente,String pfrom, String pto,Integer idagencia){
        List<SummaryTickets> lista = new ArrayList<>();
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where agentid =? and placeddate between ? and ?";
        PreparedStatement cmd;
          try {
              if(idagencia==null)
              cmd = cnn.prepareStatement(query + " order by username");
              else{
                  cmd = cnn.prepareStatement(query + " and playerid = ? order by username");
                  cmd.setInt(4, idagencia);
              }
        
        cmd.setInt(1, idAgente);
        cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
        cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
        ResultSet resp = cmd.executeQuery();
        float derecho=0,parlay=0,participacion=0,ganancia=0;
        SummaryTickets temp = new SummaryTickets();
        while (resp.next()) {
              if(!resp.getString("username").toLowerCase().equals(temp.getUsername().toLowerCase())){
                derecho = resp.getBigDecimal("derecho")==null?0:resp.getBigDecimal("derecho").floatValue();
                parlay = resp.getBigDecimal("parlay")==null?0:resp.getBigDecimal("parlay").floatValue();
                participacion = resp.getBigDecimal("participacion")==null?0:resp.getBigDecimal("participacion").floatValue();
                ganancia = resp.getBigDecimal("ganancia")==null?0:resp.getBigDecimal("ganancia").floatValue();
                calculaSum(temp,derecho,parlay,participacion,ganancia);
                temp = new  SummaryTickets();
                temp.setAgencia(resp.getString("agencia"));
                temp.setUsername(resp.getString("username"));
                temp.setAgente(resp.getString("agent"));
                temp.setCanttickets(0);
                lista.add(temp);
              }
                  temp.setCanttickets(temp.getCanttickets()+1);
                  acumValue(temp, resp);
        }
        calculaSum(temp,derecho,parlay,participacion,ganancia);
           resp.close();
        cnn.close();
          } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
    
        return lista;
      }
        public List<SummaryTicketAgent> getSummaryOfficeTaq(Integer idOffice,String pfrom, String pto,Integer idagencia){
        List<SummaryTicketAgent> lista = new ArrayList<>();
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where officeid =? and placeddate between ? and ?";
        PreparedStatement cmd;
          try {
              if(idagencia==null)
              cmd = cnn.prepareStatement(query + " order by agent");
              else{
                  cmd = cnn.prepareStatement(query + " and playerid = ? order by agent");
                  cmd.setInt(4, idagencia);
              }
        
        cmd.setInt(1, idOffice);
        cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
        cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
        ResultSet resp = cmd.executeQuery();
        float derecho=0,parlay=0,participacion=0,ganancia=0;
        SummaryTicketAgent temp = new SummaryTicketAgent();
        while (resp.next()) {
              if(!resp.getString("agent").toLowerCase().equals(temp.getAgente().toLowerCase())){
                calculaSumAgent(temp,derecho,parlay,participacion,ganancia);
                derecho = resp.getBigDecimal("derecho")==null?0:resp.getBigDecimal("derecho").floatValue();
                parlay = resp.getBigDecimal("parlay")==null?0:resp.getBigDecimal("parlay").floatValue();
                participacion = resp.getBigDecimal("participacion")==null?0:resp.getBigDecimal("participacion").floatValue();
                ganancia = resp.getBigDecimal("ganancia")==null?0:resp.getBigDecimal("ganancia").floatValue();
                temp = new  SummaryTicketAgent();
                temp.setAgente(resp.getString("agent"));
                temp.setIdAgent(resp.getInt("agentid"));
                temp.setCanttickets(0);
                lista.add(temp);
              }
                  temp.setCanttickets(temp.getCanttickets()+1);
                  acumValueAgent(temp, resp);
        }
          calculaSumAgent(temp,derecho,parlay,participacion,ganancia);
           resp.close();
        cnn.close();
          } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
    
        return lista;
      }
         public List<SummaryTickets> getSummaryTaq(Integer idOffice,String pfrom, String pto,Integer idagencia){
        List<SummaryTickets> lista = new ArrayList<>();
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where officeid =? and placeddate between ? and ?";
        PreparedStatement cmd;
          try {
              if(idagencia==null)
              cmd = cnn.prepareStatement(query + " order by username");
              else{
                  cmd = cnn.prepareStatement(query + " and playerid = ? order by username");
                  cmd.setInt(4, idagencia);
              }
        
        cmd.setInt(1, idOffice);
        cmd.setTimestamp(2, new Timestamp(dateFrom.getTime()));
        cmd.setTimestamp(3, new Timestamp(dateTo.getTime()));
        ResultSet resp = cmd.executeQuery();
        float derecho=0,parlay=0,participacion=0,ganancia=0;
        SummaryTickets temp = new SummaryTickets();
        while (resp.next()) {
              if(!resp.getString("username").toLowerCase().equals(temp.getUsername().toLowerCase())){
                calculaSum(temp,derecho,parlay,participacion,ganancia);
                derecho = resp.getBigDecimal("derechoagent")==null?0:resp.getBigDecimal("derechoagent").floatValue();
                parlay = resp.getBigDecimal("parleyagent")==null?0:resp.getBigDecimal("parleyagent").floatValue();
                participacion = resp.getBigDecimal("COMMPCT")==null?0:resp.getBigDecimal("COMMPCT").floatValue();
                ganancia = resp.getBigDecimal("comagente")==null?0:resp.getBigDecimal("comagente").floatValue();
                temp = new  SummaryTickets();
                temp.setAgencia(resp.getString("agencia"));
                temp.setUsername(resp.getString("username"));
                temp.setAgente(resp.getString("agent"));
                temp.setCanttickets(0);
                lista.add(temp);
              }
                  temp.setCanttickets(temp.getCanttickets()+1);
                  acumValue(temp, resp);
        }
     
        calculaSum(temp,derecho,parlay,participacion,ganancia);
           resp.close();
        cnn.close();
          } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
    
        return lista;
      }
      public boolean deleteTicket(int idagent,int ticket){
          
          try {
               Connection cnn = CommonUtils.getConnection();
               String query = "call Delete_Wager(?,?,?,?)";
               CallableStatement statement =cnn.prepareCall(query);
               statement.setInt(1, ticket);
               statement.setInt(2, idagent);
               statement.setInt(3, 2);
               statement.setString(4, "F");
               boolean success = statement.executeUpdate()>0;
               statement.close();
               cnn.close();
               return success;
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
                return false;
           }
      }
      public boolean paidTicket(int idOffice,int ticket){
          
          try {
               Connection cnn = CommonUtils.getConnection();
               String query = "update wagerh set paid = 'S' , paiddate = getdate(), paidfor = ? where wagerid = ?";
               CallableStatement statement =cnn.prepareCall(query);
               statement.setInt(1, idOffice);
               statement.setInt(2, ticket);
               boolean success = statement.executeUpdate()>0;
               statement.close();
               cnn.close();
               return success;
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
                return false;
           }
      }
      private void acumValue(SummaryTickets resumen,ResultSet value) throws SQLException{
        float riskamount  = value.getBigDecimal("Riskamount")==null?0:value.getBigDecimal("Riskamount").floatValue();
        float amount =value.getBigDecimal("Amount")==null?0:value.getBigDecimal("Amount").floatValue();
        int stat  =value.getInt("stat");
        int wagertype = value.getInt("Wagertype");
        resumen.setSale(resumen.getSale()+(riskamount));
        if(wagertype==11)
            resumen.setSaleparlay(resumen.getSaleparlay()+(riskamount));
        if(value.getInt("Wagertype")==0)
            resumen.setSalederecho(resumen.getSalederecho()+(riskamount));
              if(stat==2){
                    resumen.setPremio((riskamount)+resumen.getPremio()+(amount));
                    if(wagertype==11)
                        resumen.setPremioparlay((riskamount)+resumen.getPremioparlay()+(amount));
                    if(wagertype==0)
                        resumen.setPremioderecho((riskamount)+resumen.getPremioderecho()+(amount));
                }else{
                    if(stat==199 ||stat==102 || stat == 4){
                        resumen.setDevolucion(resumen.getDevolucion()+(riskamount));
                    if(wagertype==11)
                        resumen.setDevolucionparlay(resumen.getDevolucionparlay()+(riskamount));
                    if(wagertype==0)
                        resumen.setDevolucionderecho(resumen.getDevolucionderecho()+(riskamount));
                    }
                }
      }
      private void acumValue(SummaryTickets resumen,ResultSet value,boolean addPremio) throws SQLException{
        float riskamount  = value.getBigDecimal("Riskamount")==null?0:value.getBigDecimal("Riskamount").floatValue();
        float amount =value.getBigDecimal("Amount")==null?0:value.getBigDecimal("Amount").floatValue();
        int stat  =value.getInt("stat");
        int wagertype = value.getInt("Wagertype");
        resumen.setSale(resumen.getSale()+(riskamount));
        if(wagertype==11)
            resumen.setSaleparlay(resumen.getSaleparlay()+(riskamount));
      
        if(value.getInt("Wagertype")==0)
            resumen.setSalederecho(resumen.getSalederecho()+(riskamount));
              if(stat==2){
                    if(addPremio){
                    resumen.setPremio((riskamount)+resumen.getPremio()+(amount));
                    if(wagertype==11)
                        resumen.setPremioparlay((riskamount)+resumen.getPremioparlay()+(amount));
                    if(wagertype==0)
                        resumen.setPremioderecho((riskamount)+resumen.getPremioderecho()+(amount));
                    }
                }else{
                    if(stat==199 ||stat==102 || stat == 4){
                        resumen.setDevolucion(resumen.getDevolucion()+(riskamount));
                    if(wagertype==11)
                        resumen.setDevolucionparlay(resumen.getDevolucionparlay()+(riskamount));
                    if(wagertype==0)
                        resumen.setDevolucionderecho(resumen.getDevolucionderecho()+(riskamount));
                    }
                }
        
      }
      private void acumValueAgent(SummaryTicketAgent resumen,ResultSet value) throws SQLException{
        float riskamount  = value.getBigDecimal("Riskamount")==null?0:value.getBigDecimal("Riskamount").floatValue();
        float amount =value.getBigDecimal("Amount")==null?0:value.getBigDecimal("Amount").floatValue();
        int stat  =value.getInt("stat");
        int wagertype = value.getInt("Wagertype");
        resumen.setSale(resumen.getSale()+(riskamount));
        if(wagertype==11)
            resumen.setSaleparlay(resumen.getSaleparlay()+(riskamount));
        if(value.getInt("Wagertype")==0)
            resumen.setSalederecho(resumen.getSalederecho()+(riskamount));
              if(stat==2){
                    resumen.setPremio((riskamount)+resumen.getPremio()+(amount));
                    if(wagertype==11)
                        resumen.setPremioparlay((riskamount)+resumen.getPremioparlay()+(amount));
                    if(wagertype==0)
                        resumen.setPremioderecho((riskamount)+resumen.getPremioderecho()+(amount));
                }else{
                    if(stat==199 ||stat==102 || stat == 4){
                        resumen.setDevolucion(resumen.getDevolucion()+(riskamount));
                    if(wagertype==11)
                        resumen.setDevolucionparlay(resumen.getDevolucionparlay()+(riskamount));
                    if(wagertype==0)
                        resumen.setDevolucionderecho(resumen.getDevolucionderecho()+(riskamount));
                    }
                }
      }
      private void acumValueAgent(SummaryTicketAgent resumen,ResultSet value,boolean addPremio) throws SQLException{
        float riskamount  = value.getBigDecimal("Riskamount")==null?0:value.getBigDecimal("Riskamount").floatValue();
        float amount =value.getBigDecimal("Amount")==null?0:value.getBigDecimal("Amount").floatValue();
        int stat  =value.getInt("stat");
        int wagertype = value.getInt("Wagertype");
        resumen.setSale(resumen.getSale()+(riskamount));
        if(wagertype==11)
            resumen.setSaleparlay(resumen.getSaleparlay()+(riskamount));
      
        if(value.getInt("Wagertype")==0)
            resumen.setSalederecho(resumen.getSalederecho()+(riskamount));
              if(stat==2){
                    if(addPremio){
                    resumen.setPremio((riskamount)+resumen.getPremio()+(amount));
                    if(wagertype==11)
                        resumen.setPremioparlay((riskamount)+resumen.getPremioparlay()+(amount));
                    if(wagertype==0)
                        resumen.setPremioderecho((riskamount)+resumen.getPremioderecho()+(amount));
                    }
                }else{
                    if(stat==199 ||stat==102 || stat == 4){
                        resumen.setDevolucion(resumen.getDevolucion()+(riskamount));
                    if(wagertype==11)
                        resumen.setDevolucionparlay(resumen.getDevolucionparlay()+(riskamount));
                    if(wagertype==0)
                        resumen.setDevolucionderecho(resumen.getDevolucionderecho()+(riskamount));
                    }
                }
        
      }
      private void acumValuePremio(SummaryTickets resumen,ResultSet value) throws SQLException{
        float riskamount  = value.getBigDecimal("Riskamount")==null?0:value.getBigDecimal("Riskamount").floatValue();
        float amount =value.getBigDecimal("Amount")==null?0:value.getBigDecimal("Amount").floatValue();
        int stat  =value.getInt("stat");
        int wagertype = value.getInt("Wagertype");
              if(stat==2){
                    resumen.setPremio((riskamount)+resumen.getPremio()+(amount));
                    if(wagertype==11)
                        resumen.setPremioparlay((riskamount)+resumen.getPremioparlay()+(amount));
                    if(wagertype==0)
                        resumen.setPremioderecho((riskamount)+resumen.getPremioderecho()+(amount));
                }else{
                    if(stat==199 ||stat==102 || stat == 4){
                        resumen.setDevolucion(resumen.getDevolucion()+(riskamount));
                    if(wagertype==11)
                        resumen.setDevolucionparlay(resumen.getDevolucionparlay()+(riskamount));
                    if(wagertype==0)
                        resumen.setDevolucionderecho(resumen.getDevolucionderecho()+(riskamount));
                    }
                }
      }
      private void calculaSum(SummaryTickets resumen,float derecho, float parlay,float participacion,float ganancia){
     
            resumen.setComisionderecho((resumen.getSalederecho()-resumen.getDevolucionderecho())*(derecho/100));
            resumen.setComisionparlay((resumen.getSaleparlay()-resumen.getDevolucionparlay())*(parlay/100));
            resumen.setComision(resumen.getComisionderecho()+resumen.getComisionparlay());
            resumen.setSaleBrute(resumen.getSale()-resumen.getComision()-resumen.getPremio()-resumen.getDevolucion());
            if(resumen.getSaleBrute()>0)
                resumen.setGanancia(resumen.getSaleBrute()*(ganancia/100));
            else
                resumen.setGanancia(0);
            resumen.setSubtotal(resumen.getSaleBrute()-resumen.getGanancia());
            resumen.setParticipacion(resumen.getSubtotal()*(participacion/100));
            resumen.setSaldo(resumen.getSubtotal()-resumen.getParticipacion());
      }
      private void calculaSumAgent(SummaryTicketAgent resumen,float derecho, float parlay,float participacion,float ganancia){
     
            resumen.setComisionderecho((resumen.getSalederecho()-resumen.getDevolucionderecho())*(derecho/100));
            resumen.setComisionparlay((resumen.getSaleparlay()-resumen.getDevolucionparlay())*(parlay/100));
            resumen.setComision(resumen.getComisionderecho()+resumen.getComisionparlay());
            resumen.setSaleBrute(resumen.getSale()-resumen.getComision()-resumen.getPremio()-resumen.getDevolucion());
            if(resumen.getSaleBrute()>0)
                resumen.setGanancia(resumen.getSaleBrute()*(ganancia/100));
            else
                resumen.setGanancia(0);
            resumen.setSubtotal(resumen.getSaleBrute()-resumen.getGanancia());
            resumen.setParticipacion(resumen.getSubtotal()*(participacion/100));
            resumen.setSaldo(resumen.getSubtotal()-resumen.getParticipacion());
      }
      public List<TicketDetalle> getDetailsbyid(Integer id){
           List<TicketDetalle> lista = new ArrayList<>();
           try {
               Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwTicketComplete where wagerid =?   ";
               PreparedStatement statement =cnn.prepareStatement(query);
               statement.setInt(1, id);
               ResultSet resp = statement.executeQuery();
                while (resp.next()) {
                    lista.add(createDetalleTicket(resp));
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return lista;
      }
      public List<TicketDetalle> getDetailsDeletedbyid(Integer id){
           List<TicketDetalle> lista = new ArrayList<>();
           try {
               Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwticketdeleteddetails where wagerid =?   ";
               PreparedStatement statement =cnn.prepareStatement(query);
               statement.setInt(1, id);
               ResultSet resp = statement.executeQuery();
                while (resp.next()) {
                    lista.add(createDetalleTicket(resp));
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return lista;
      }
       public List<Ticket> getTicket(Integer idAgente,Integer numero){
        List<Ticket> lista = new ArrayList<>();
        
        try {
            Connection cnn = CommonUtils.getConnection();
            String query = "select * from vwTicketSummary where officeid =? and cast(wagerid as varchar(20)) like ?+'%' ";
            PreparedStatement statement;
            statement = cnn.prepareStatement(query);
            statement.setInt(1, idAgente);
            statement.setString(2, numero.toString());
            ResultSet resp = statement.executeQuery();
            while (resp.next()) {
                    lista.add( createTicket(resp));
            }
            resp.close();
            cnn.close();
        } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
      public List<Ticket> getTicketAgente(Integer idAgente,String pfrom, String pto,Integer idagencia){
        if(pfrom==null)pfrom="";  
        if(pto==null)pto="";  
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        List<Ticket> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where agentid =? and placeddate between ? and ? ";
        try {
            
            PreparedStatement statement;
            if(idagencia!=null){
               statement = cnn.prepareStatement(query+" and playerid = ? order by 1 ");
               statement.setInt(4, idagencia);
            }
            else
                statement = cnn.prepareStatement(query+" order by 1 ");
            
            statement.setInt(1, idAgente);
            statement.setTimestamp(2, new java.sql.Timestamp(dateFrom.getTime()));
            statement.setTimestamp(3, new java.sql.Timestamp(dateTo.getTime()));
               
            ResultSet resp = statement.executeQuery();
    
            while (resp.next()) {
                    lista.add( createTicket(resp));
            }
            resp.close();
            cnn.close();
             } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
    }
    public List<Ticket> getTicketOffice(Integer idOffice,String pfrom, String pto,Integer idagencia){
        if(pfrom==null)pfrom="";  
        if(pto==null)pto="";  
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
        List<Ticket> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwTicketSummary where officeid =? and placeddate between ? and ? ";
        try {
            
            PreparedStatement statement;
            if(idagencia!=null){
               statement = cnn.prepareStatement(query+" and playerid = ? order by 1 ");
               statement.setInt(4, idagencia);
            }
            else
                statement = cnn.prepareStatement(query+" order by 1 ");
            
            statement.setInt(1, idOffice);
            statement.setTimestamp(2, new java.sql.Timestamp(dateFrom.getTime()));
            statement.setTimestamp(3, new java.sql.Timestamp(dateTo.getTime()));
               
            ResultSet resp = statement.executeQuery();
    
            while (resp.next()) {
                    lista.add( createTicket(resp));
            }
            resp.close();
            cnn.close();
             } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return lista;
    }
    private String getTypeBet(int type){
        String typeStr = "";
        switch(type){ 
            case 0: typeStr = "Por Derecho"; break;
            case 11: typeStr = "Parlay";break;
            default: typeStr = "-NA";break;
        }
        return typeStr;
    }
    private Ticket createTicket(ResultSet ticket){
        Ticket nv = new Ticket();
          try {
              nv.setAmount((ticket.getBigDecimal("Amount")==null)?0:ticket.getBigDecimal("Amount").intValue());
         
        nv.setDateCreation(CommonUtils.ConvertDateToStrLong(ticket.getTimestamp("Placeddate")));
        nv.setDatePaid(CommonUtils.ConvertDateToStrLong(ticket.getTimestamp("Paiddate")));
        nv.setDescripcion(ticket.getString("descriptionh"));
        nv.setAgencia(ticket.getString("agencia"));
        nv.setIdticket(ticket.getInt("Wagerid"));
        nv.setStatus(CommonUtils.getStatus(ticket.getInt("stat")));
        nv.setStatuspaid(ticket.getString("Paid").toLowerCase().equals("y"));
        nv.setTypebet(getTypeBet(ticket.getInt("Wagertype")));
        nv.setWinamount((ticket.getBigDecimal("Winamount")==null)?0:ticket.getBigDecimal("Winamount").intValue());
        nv.setRiskamount((ticket.getBigDecimal("Riskamount")==null)?0:ticket.getBigDecimal("Riskamount").intValue());
      
         } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return nv;
    }
       private Ticket createTicketGame(ResultSet ticket){
        Ticket nv = new Ticket();
          try {
              nv.setAmount((ticket.getBigDecimal("Amount")==null)?0:ticket.getBigDecimal("Amount").intValue());
         
        nv.setDateCreation(CommonUtils.ConvertDateToStrLong(ticket.getTimestamp("Placeddate")));
        nv.setDatePaid(CommonUtils.ConvertDateToStrLong(ticket.getTimestamp("Paiddate")));
        nv.setDescripcion(ticket.getString("descriptionh"));
        nv.setAgencia(ticket.getString("agencia"));
        nv.setIdticket(ticket.getInt("Wagerid"));
        nv.setStatus(CommonUtils.getStatus(ticket.getInt("stat")));
        nv.setStatuspaid(ticket.getString("Paid").toLowerCase().equals("y"));
        nv.setTypebet(getTypeBet(ticket.getInt("Wagertype")));
        nv.setWinamount((ticket.getBigDecimal("Winamount")==null)?0:ticket.getBigDecimal("Winamount").intValue());
        nv.setRiskamount((ticket.getBigDecimal("Riskamount")==null)?0:ticket.getBigDecimal("Riskamount").intValue());
        nv.setDescGame(ticket.getString("descriptiond"));
        
         } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return nv;
    }
    public List<Ticket> getListTicketbyGame(Integer gameid,Integer idagent){
        List<Ticket> lista = new ArrayList<>();
           try {
               Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwTicketComplete where gameid =?  and officeid =? ";
               PreparedStatement statement =cnn.prepareStatement(query);
               statement.setInt(1, gameid);
               statement.setInt(2, idagent);
               ResultSet resp = statement.executeQuery();
                while (resp.next()) {
                    lista.add(createTicketGame(resp));
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return lista;
    }
    public List<Ticket> getListTicketbydeleted(Integer idagent,String pfrom,String pto,Integer idagencia){
        List<Ticket> lista = new ArrayList<>();
         if(pfrom==null)pfrom="";  
        if(pto==null)pto="";  
        Date dateFrom = (pfrom.equals(""))? CommonUtils.getInitDateMonth(pfrom):CommonUtils.convertStrToShortDate(pfrom);
        Date dateTo = (pto.equals(""))? CommonUtils.getEndDateMonth(pto):CommonUtils.convertStrToShortDateEndHour(pto);
           try {
               Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwTicketDeleted where agentid =? and placeddate between ? and ?  ";
               PreparedStatement statement =cnn.prepareStatement(query);
                statement.setInt(1, idagent);
                 statement.setTimestamp(2, new java.sql.Timestamp(dateFrom.getTime()));
            statement.setTimestamp(3, new java.sql.Timestamp(dateTo.getTime()));
               ResultSet resp = statement.executeQuery();
                while (resp.next()) {
                    lista.add(createTicket(resp));
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return lista;
    }
    private TicketDetalle createDetalleTicket(ResultSet detalle){
        TicketDetalle detalleFinal = new TicketDetalle();
          try {
              detalleFinal.setCant((detalle.getBigDecimal("Points")==null)?null:detalle.getBigDecimal("Points").floatValue());
         
        detalleFinal.setDescripcion(detalle.getString("descriptiond"));
        detalleFinal.setOdd(detalle.getInt("odds"));
        detalleFinal.setStatus(CommonUtils.getStatus(detalle.getInt("statd")));
        detalleFinal.setIdgame(detalle.getInt("Gameid"));
         } catch (SQLException ex) {
              Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
          }
        return detalleFinal;
    }
    public SummaryOffice getSummayGeneralOffice(int officeId){
        SummaryOffice summary =  new SummaryOffice();
        try {
              Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwSummaryOffice where officeid =? and convert(varchar(10),date,103) = convert(varchar(10),getdate(),103)  ";
              PreparedStatement statement =cnn.prepareStatement(query);
              statement.setInt(1, officeId);
              ResultSet resp = statement.executeQuery();
                if (resp.next()) {
                    summary.setIdOffice(officeId);
                    summary.setDate(resp.getString("date"));
                    summary.setSale(resp.getFloat("sale")); 
                    summary.setTickets(resp.getInt("tickets"));
                    summary.setWinPaid(resp.getFloat("winPaid"));
                    summary.setPlayersPlay(resp.getInt("players"));
                    summary.setTicketsD(resp.getInt("ticketd"));
                    summary.setTicketsP(resp.getInt("ticketp"));
                    summary.setSaleDerecho(resp.getFloat("saleDerecho"));
                    summary.setSaleParlay(resp.getFloat("saleParlay"));
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return summary;
    }
     public List<SummaryOffice> getSummayGeneralOfficeList(int officeId){
        List<SummaryOffice> summarys =  new ArrayList<>();
        try {
              Connection cnn = CommonUtils.getConnection();
              String query = "select * from vwSummaryOffice where officeid =? and convert(datetime,date,103) >=dateadd(day,-7,getdate())  order by convert(datetime,date,103)";
              PreparedStatement statement =cnn.prepareStatement(query);
              statement.setInt(1, officeId);
              ResultSet resp = statement.executeQuery();
                while (resp.next()) {
                    SummaryOffice summary = new SummaryOffice();
                    summary.setIdOffice(officeId);
                    summary.setDate(resp.getString("date"));
                    summary.setSale(resp.getFloat("sale")); 
                    summary.setTickets(resp.getInt("tickets"));
                    summary.setWinPaid(resp.getFloat("winPaid"));
                    summary.setPlayersPlay(resp.getInt("players"));
                    summary.setTicketsD(resp.getInt("ticketd"));
                    summary.setTicketsP(resp.getInt("ticketp"));
                    summary.setSaleDerecho(resp.getFloat("saleDerecho"));
                    summary.setSaleParlay(resp.getFloat("saleParlay"));
                    summarys.add(summary);
                }
                resp.close();
                cnn.close();
           }catch(SQLException ex){
                Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
           }
           return summarys;
    }
}
