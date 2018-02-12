/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.controller;

import admin.context.CommonUtils;
import admin.domain.Game;
import admin.domain.MoneyPlay;
import admin.domain.MoneyPlayParticipants;
import admin.domain.Participant;
import admin.domain.PlayType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yanny Hernandez
 */
public class GameController {
    private static final Logger LOG = Logger.getLogger(GameController.class.getName());
    private final TicketController ticket = new TicketController();
    public GameController(){

    }
    public Game createGame(Integer idagente,ResultSet data) throws SQLException{
        Game game = new Game();
        game.setIdgame(data.getInt("Gameid"));
        game.setSport(data.getInt("Gametype"));
        game.setStatus(CommonUtils.getStatus(data.getString("Gamestat")));
        game.setDate(data.getDate("Gamedate"));
        game.setTime(data.getTimestamp("Gametime"));
        game.setDescripcion(data.getString("visitor")+" vs "+data.getString("home"));
        game.setParticipants(new ArrayList<>());
        Participant visitor = new Participant();
        visitor.setRotation(data.getInt("visitorid"));
        visitor.setDescripcion(data.getString("visitor"));
        visitor.setScore(data.getInt("vscore"));
        Participant home = new Participant();
        home.setRotation(data.getInt("homeid"));
        home.setDescripcion(data.getString("home"));
        home.setScore(data.getInt("hscore"));
        game.getParticipants().add(visitor);
        game.getParticipants().add(home);
        game.setBets(data.getInt("jugadas"));
        return game;
    }
    public List<Game> getGamebydate(Integer idagente,Date date){
        List<Game> lista = new ArrayList<>();
        date = CommonUtils.resetTime(date);
        String query = " select gameid, gametype,gamestat,"
                + " gamedate,gametime,visitor,home,visitorid,vscore,"
                + " homeid,hscore, jugadas from  vwgames"
                + " where convert(varchar(10),gamedate ,103) = convert(varchar(10),? ,103)  and "
                + " officeid = ?";
               
        Connection cnn = CommonUtils.getConnection();
        PreparedStatement cmd;
         try {
         cmd= cnn.prepareStatement(query);
        cmd.setDate(1, new java.sql.Date(date.getTime()));
        cmd.setInt(2, idagente);
        ResultSet result = cmd.executeQuery();
        while(result.next()){
                 lista.add(createGame(idagente,result));
        }
        result.close();
       cmd.close();
       cnn.close();
         } catch (SQLException ex) {
             Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
         }
        return lista;
    }
    public List<MoneyPlay> getPlaysMoney(int officeId){
        List<MoneyPlay> lista = new ArrayList<MoneyPlay>();
        try{
            String query= "select * from vwReportRiskWin where oficina = ? order by gametype";
            Connection cnn = CommonUtils.getConnection();
            PreparedStatement cmd = cnn.prepareStatement(query);
            cmd.setInt(1, officeId);
            ResultSet result = cmd.executeQuery();
            MoneyPlay last = new MoneyPlay();
            while(result.next()){
                if(last.getGameid()!=result.getInt("gameid")){
                    last = createMoneyPlay(result);            
                    lista.add(last);
                }
                loadPlay(last, result);
            }
            result.close();
            cnn.close();
        }catch(Exception ex){
            LOG.log(Level.OFF, ex.getMessage());
        }
        return lista;
    } 

    private MoneyPlay createMoneyPlay(ResultSet query) throws SQLException{
        MoneyPlay money = new MoneyPlay();
        money.setDate(query.getDate("gamedate"));
        money.setGameid(query.getInt("gameid"));
        money.setSport(query.getInt("gametype"));
        money.setTime(query.getString("time"));
        money.setVisitor(createParcipantMoney(query,false));
        money.setHome(createParcipantMoney(query,true));
        return money;
    }
    private MoneyPlayParticipants createParcipantMoney(ResultSet result,boolean  isHome) throws SQLException{
        MoneyPlayParticipants participant = new MoneyPlayParticipants();
        participant.setName(result.getString(isHome?"Home":"visitor"));
        participant.setRotation(result.getInt(isHome?"Homeid":"visitorid"));
        participant.setPlaysRisk(new ArrayList<>());
        participant.setPlaysWin(new ArrayList<>());
        return participant;
    }
    private void loadPlay(MoneyPlay current,ResultSet result) throws SQLException{
        String play = result.getString("play");
        if(play.equals("A") || play.equals("E") || play.equals("C")){
            current.getVisitor().getPlaysRisk().add(new PlayType(play,result.getInt("riskTotal")));
            current.getVisitor().getPlaysWin().add(new PlayType(play,result.getInt("winTotal")));
        }
        if(play.equals("B") || play.equals("F") || play.equals("D")){
            current.getHome().getPlaysRisk().add(new PlayType(play,result.getInt("riskTotal")));
            current.getHome().getPlaysWin().add(new PlayType(play,result.getInt("winTotal")));
        }
    }
}

