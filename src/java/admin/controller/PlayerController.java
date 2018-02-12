/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.controller;

import admin.context.CommonUtils;
import admin.domain.Agencia;
import admin.domain.EditAgencia;
import admin.domain.StateAgencia;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Yanny Hernandez
 */
public class PlayerController {
   
    public PlayerController(){
     
        
    }
    public static Agencia createPlayer(ResultSet player) throws SQLException{
        Agencia nv = new Agencia();
        nv.setAtrisk(player.getBigDecimal("Atrisk")==null?0:player.getBigDecimal("Atrisk").floatValue());
        nv.setBalance(player.getBigDecimal("Curbalance")==null?0:player.getBigDecimal("Curbalance").floatValue());
        nv.setCierre(player.getInt("Cierre"));
        nv.setComisionderecho(player.getBigDecimal("Derecho")==null?0:player.getBigDecimal("Derecho").floatValue());
        nv.setComisionparlay(player.getBigDecimal("Parlay")== null?0 :player.getBigDecimal("Parlay").floatValue());
        nv.setCreditlimit(player.getBigDecimal("Creditlimit")==null?0:player.getBigDecimal("Creditlimit").floatValue());
        nv.setDatecreation( player.getDate("Accountopened")==null?new Date():player.getDate("Accountopened"));
        nv.setIdagencia(player.getInt("Playerid"));
        nv.setIsprepaid(player.getString("prepago")==null?false:player.getString("prepago").toLowerCase().equals("s"));
        nv.setIsprint(player.getString("Printer")==null?false:player.getString("Printer").toLowerCase().equals("s"));
        nv.setIstaquilla(player.getString("Taquilla")==null?false:player.getString("Taquilla").toLowerCase().equals("s"));
        nv.setLimitcombina(player.getBigDecimal("Limitecombinada")==null?0:player.getBigDecimal("Limitecombinada").floatValue());
        nv.setLimitderecho(player.getFloat("Limitederecho"));
        nv.setLimitparlay(player.getBigDecimal("limite_parley")==null?0:player.getBigDecimal("limite_parley").floatValue());
        nv.setNameagencia(player.getString("Lname"));
        nv.setNameagente(player.getString("agent"));
        nv.setPartipacion(player.getBigDecimal("Participacion")==null?0:player.getBigDecimal("Participacion").floatValue());
        nv.setPass(CommonUtils.encodeBase64(player.getString("Onlinepwd")));
        nv.setPorcentajeganancia(player.getBigDecimal("ComnPct")==null?0:player.getBigDecimal("ComnPct").floatValue());
        nv.setTypeprint(CommonUtils.convertStringToInt(player.getString("applet")));
        nv.setUsername(player.getString("Playerid2"));
        nv.setIdAgent(player.getInt("agentid"));
        nv.setLastbet(player.getDate("Lastwager")==null?new Date ():player.getDate("Lastwager"));
        nv.setMaxbet(player.getFloat("Maxwager"));
        nv.setProporcion(player.getInt("proporcion"));
        nv.setIsactiva(player.getString("Playerstat")==null?false:player.getString("Playerstat").toLowerCase().equals("n" ));
        nv.setLimitsaleday(player.getBigDecimal("Limiteventasdiario")==null?0:player.getBigDecimal("Limiteventasdiario").floatValue());
        nv.setResponsable(player.getString("fname")==null?"Responsable no Indicado":(player.getString("fname").trim()=="")?"Responsable no Indicado":player.getString("fname").trim());
        nv.setIdOffice(player.getInt("officeid"));
        nv.setEmail(player.getString("email"));
        nv.setPhone(player.getString("phone"));
        return nv;
    }
 public static StateAgencia createStateAgencia(ResultSet player) throws SQLException{
        Agencia nv = createPlayer(player);
        StateAgencia state = new StateAgencia();
        state.setTicket(player.getInt("enjuego"));
        state.setPaid(player.getInt("pagados"));
        state.setNopaid(player.getInt("porpagar"));
        state.setAgencia(nv);
        return state;
    }
    public boolean  freeAgencia(Integer idagencia){
        try{
            Connection cnn = CommonUtils.getConnection();
            String query = "update player set serial = '' where playerid = ?";
            PreparedStatement cmd = cnn.prepareStatement(query);
            cmd.setInt(1, idagencia);
            boolean success =   cmd.executeUpdate()>0;
            cmd.close();
            cnn.close();
            return success;
        }catch(SQLException ex){
            return false;
        }
    }
    public boolean  resetPass(Integer idagencia){
        try{
            Connection cnn = CommonUtils.getConnection();
            String query = "update player set onlinepwd = '123' where playerid = ?";
            PreparedStatement cmd = cnn.prepareStatement(query);
            cmd.setInt(1, idagencia);
            boolean success =   cmd.executeUpdate()>0;
            cmd.close();
            cnn.close();
            return success;
        }catch(SQLException ex){
            return false;
        }
    }
    public boolean  changeStat(Integer idagencia){
        try{
            Agencia agencia = getAgenciaByUserName(idagencia);
            Connection cnn = CommonUtils.getConnection();
            String query = "update player set playerstat = ? where playerid = ?";
            PreparedStatement cmd = cnn.prepareStatement(query);
            cmd.setString(1, agencia.isIsactiva()?"I":"N");
            cmd.setInt(2, idagencia);
            boolean success =  cmd.executeUpdate()>0;
            cmd.close();
            cnn.close();
            return success;
        }catch(SQLException ex){
            return false;
        }
    }
    public List<StateAgencia> getStateAgencia(int idagente){
        List<StateAgencia> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from stateAgencia where agentid = ?";
        PreparedStatement statement;
        try {
            statement = cnn.prepareStatement(query);
             statement.setInt(1, idagente);
             ResultSet rst = statement.executeQuery();
             while(rst.next()){
                 lista.add(createStateAgencia(rst));
             }
             rst.close();
             cnn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         return lista;
    }
    public List<Agencia> getAgenciaByagent(int idagente){
        List<Agencia> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwPlayer where agentid = ?";
        PreparedStatement statement;
        try {
            statement = cnn.prepareStatement(query);
             statement.setInt(1, idagente);
             ResultSet rst = statement.executeQuery();
             while(rst.next()){
                 lista.add(createPlayer(rst));
             }
             rst.close();
             statement.close();
             cnn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         return lista;
    }
     public List<Agencia> getAgenciaByOffice(int ifOffice){
        List<Agencia> lista = new ArrayList<>();
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwPlayer where officeId = ?";
        PreparedStatement statement;
        try {
            statement = cnn.prepareStatement(query);
             statement.setInt(1, ifOffice);
             ResultSet rst = statement.executeQuery();
             while(rst.next()){
                 lista.add(createPlayer(rst));
             }
             rst.close();
             statement.close();
             cnn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         return lista;
    }
    public Agencia CreateAgencia(Agencia nv){
         Connection cnn = CommonUtils.getConnection();
         String query = "{ ? = call dbo.Insert_player_complete("
                 + "?,?,?,?,?,"
                 + "?,?,?,?,?,"
                 + "?,?,?,?,?,"
                 + "?,?,?,?,?,"
                 + "?,?) }";
        try {
            Agencia agencia;
             try (CallableStatement storedProcedure = cnn.prepareCall(query)) {
                 storedProcedure.registerOutParameter(1,java.sql.Types.INTEGER);
                 storedProcedure.setString(2, nv.getNameagencia());
                 storedProcedure.setInt(3, nv.getIdAgent());
                 storedProcedure.setString(4, nv.getUsername());
                 storedProcedure.setString(5, nv.getPass());
                 storedProcedure.setInt(6,nv.getIdOffice());
                 storedProcedure.setString(7,nv.isIstaquilla()?"S":"N");
                 storedProcedure.setFloat(8,nv.getCreditlimit());
                 storedProcedure.setInt(9, nv.getLimitparlay().intValue());
                 storedProcedure.setInt(10, nv.getLimitderecho().intValue());
                 storedProcedure.setInt(11, nv.getLimitsaleday().intValue());
                 storedProcedure.setString(12, nv.getResponsable());
                 storedProcedure.setString(13, nv.getPhone());
                 storedProcedure.setString(14, nv.getEmail());
                 storedProcedure.setString(15, nv.getTypeprint()==null?"3":nv.getTypeprint().toString());
                 storedProcedure.setInt(16, nv.getComisionderecho().intValue());
                 storedProcedure.setInt(17, nv.getComisionparlay().intValue());
                 storedProcedure.setInt(18, nv.getPorcentajeganancia().intValue());
                 storedProcedure.setInt(19, nv.getPartipacion().intValue());
                 storedProcedure.setInt(20, nv.getProporcion());
                 storedProcedure.setInt(21, nv.getMaxbet().intValue());
                 storedProcedure.setBoolean(22, nv.isPardos());
                 storedProcedure.setInt(23, nv.getLimitcombina().intValue());
                 agencia = null;
                 if(storedProcedure.executeUpdate()>0){
                     int idagencia = storedProcedure.getInt(1);
                     agencia = getAgenciaByUserName(idagencia);
                 }else{
                     return null;
                 }}
            cnn.close();
            return agencia;
        } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public Agencia getAgenciaByUserName(String username){
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwPlayer where playerid2 =?";
       
        try {
             PreparedStatement statement = cnn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rst = statement.executeQuery();
            Agencia agencia = null;
             if(rst.next()){
                    
                 agencia= createPlayer(rst);
            }
             rst.close();
             statement.close();
             cnn.close();
             return agencia;
          } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public Agencia edit(Agencia edit){
        try {
              Connection cnn = CommonUtils.getConnection();
             String query = 
                     " update player set  applet=?, limitederecho=?, limite_parley=?,"
                     + " limiteventasdiario=?,MAXWAGER=?, limitecombinada=?, "
                     + " proporcion = ?,email = ?, phone = ?,fname =?,lname=?, "
                     + " printer= ?, agentid = ?,derecho=?,Parlay = ?,"
                     + " participacion = ?, ComnPct = ?  where playerid = ?";
             PreparedStatement statement = cnn.prepareStatement(query);
            statement.setInt(1, edit.getTypeprint());
            statement.setInt(2, edit.getLimitderecho().intValue());
            statement.setInt(3, edit.getLimitparlay().intValue());
            statement.setInt(4, edit.getLimitsaleday().intValue());
            statement.setInt(5, edit.getMaxbet().intValue());
            statement.setInt(6, edit.getLimitcombina().intValue());
            statement.setInt(7, edit.getProporcion());
            statement.setString(8,edit.getEmail());
            statement.setString(9, edit.getPhone());
            statement.setString(10,edit.getNameagencia());
            statement.setString(11, edit.getResponsable());
            statement.setString(12, edit.isIsprint()?"S":"N");
            statement.setInt(13,edit.getIdAgent());
            
            statement.setInt(14, edit.getComisionderecho().intValue());
            statement.setInt(15, edit.getComisionparlay().intValue());
            statement.setInt(16, edit.getPartipacion().intValue());
            statement.setInt(17, edit.getPorcentajeganancia().intValue());
            statement.setInt(18, edit.getIdagencia());
           Agencia agencia = null;
             if( statement.executeUpdate()>0){
                    
                 agencia= getAgenciaByUserName(edit.getIdagencia());
            }
             
             statement.close();
             cnn.close();
             return agencia;
          } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public Agencia getAgenciaByUserName(int username){
        Connection cnn = CommonUtils.getConnection();
        String query = "select * from vwPlayer where playerid =?";
       
        try {
             PreparedStatement statement = cnn.prepareStatement(query);
            statement.setInt(1, username);
            ResultSet rst = statement.executeQuery();
            Agencia agencia = null;
             if(rst.next()){
                    
                 agencia= createPlayer(rst);
            }
             rst.close();
             statement.close();
             cnn.close();
             return agencia;
          } catch (SQLException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
