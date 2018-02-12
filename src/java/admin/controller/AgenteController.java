/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.controller;


import admin.context.CommonUtils;
import admin.domain.Agente;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Yanny Hernandez
 */
public class AgenteController {
   private static final Logger LOG = Logger.getLogger(AgenteController.class.getName()); 
    public AgenteController(){

    }
  private final String SELECTBYOFFICE = "select * from vwagent where officeid= ?";
  private final String INSERT = "{? = call dbo.Insert_AGENT_complete(?,?,?,?,?,?,?,?,?,?)}";
  private final String EDIT = "UPDATE agent set name = ?,comision1 = ?, comision2 = ?,phone1 = ?,email = ?  where agentid = ?";
  public List<Agente> getListAgentbyOffice(int idOffice){
      List<Agente> agents = new ArrayList<>();
      try{
          Connection cnn = CommonUtils.getConnection();
          PreparedStatement cmd = cnn.prepareStatement(SELECTBYOFFICE);
          cmd.setInt(1, idOffice);
          ResultSet result = cmd.executeQuery();
          while(result.next()){
              agents.add(createAgent(result));
          }
          result.close();
          cnn.close();
      }catch(Exception ex){
         LOG.log(Level.OFF, ex.getMessage());
      }
      return agents;
  }
  public String edit(Agente agent){
      String resp = "";
      Connection cnn = CommonUtils.getConnection();
      try{
          
          PreparedStatement cmd = cnn.prepareStatement(EDIT);
          cmd.setString(1, agent.getNameagente());
          cmd.setFloat(2, agent.getDerecho());
          cmd.setFloat(3, agent.getParlay());
          cmd.setString(4, agent.getTelf());
          cmd.setString(5, agent.getEmail());
          cmd.setInt(6, agent.getIdagente());
          if(cmd.executeUpdate()>0){
              resp = "Agent Editado Correctamente";
          }else{
              resp = "Edicion del Agente no Confirmada Intente Nuevamente";
          }
          cmd.close();
      
      }catch(Exception ex){
          resp = ex.getMessage();
      }
      finally{
          if(cnn!= null){
              try {
                  cnn.close();
              } catch (SQLException ex) {
                  Logger.getLogger(AgenteController.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      } 
      return  resp;
  }
  public Agente create(Agente agent,String pass){
      try{
          Connection cnn = CommonUtils.getConnection();
          CallableStatement cmd = cnn.prepareCall(INSERT);
          cmd.registerOutParameter(1, java.sql.Types.INTEGER);
          cmd.setString(2, agent.getUsername());
          cmd.setString(3, agent.getNameagente());
          cmd.setString(4, pass);
          cmd.setFloat(5, agent.getParticipacion());
          cmd.setFloat(6, agent.getDerecho());
          cmd.setFloat(7, agent.getParlay());
          cmd.setFloat(8, agent.getGanancia());
          cmd.setInt(9, agent.getOffice());
          cmd.setString(10, agent.getEmail());
          cmd.setString(11, agent.getTelf());
          if(cmd.execute()){
              agent.setIdagente(cmd.getInt(1));
        }else{
              agent = null;
          }
          cmd.close();
          cnn.close();
      }catch(Exception ex){
            Logger.getLogger(AgenteController.class.getName()).log(Level.SEVERE, null, ex);
      }
      return agent;
  }
  private Agente createAgent(ResultSet result) throws SQLException{
      Agente agent = new Agente();
      agent.setNameagente(result.getString("name"));
      agent.setOffice(result.getInt("officeid"));
      agent.setGrouping(result.getString("grouping"));
      agent.setIdagente(result.getInt("agentid"));
      agent.setUsername(result.getString("username"));
      agent.setDerecho(result.getBigDecimal("derecho").floatValue());
      agent.setParlay(result.getBigDecimal("parlay").floatValue());
      agent.setAlquiler(result.getBigDecimal("alquiler").floatValue());
      agent.setEmail(result.getString("email"));
      agent.setTelf(result.getString("phone1"));
      agent.setParticipacion(result.getBigDecimal("participacion").floatValue());
      agent.setGanancia(result.getBigDecimal("ganancia").floatValue());
      return agent;
  }
    
 
   
    
}
