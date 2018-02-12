/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin.context;

import admin.controller.PlayerController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 *
 * @author Yanny Hernandez
 */
public class CommonUtils {
    private static final SimpleDateFormat dateShortFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat dateLongFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    public static Connection getConnection(){
    try {
            Context ctx = new InitialContext();
            Context initCtx  = (Context) ctx.lookup("java:/comp/env");
            String database = (String) initCtx.lookup("database");
            String user = (String) initCtx.lookup("user");
            String driver = (String) initCtx.lookup("driver");
            String host = (String) initCtx.lookup("server");
            Integer port  = (Integer) initCtx.lookup("port");
            String url  = (String) initCtx.lookup("url");
            String pass  = (String) initCtx.lookup("pass");
            Class.forName(driver);
            return DriverManager.getConnection(url+host+":"+port+"/"+database,user,pass);
        } catch (NamingException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
             return null;
        }
    }
    public static String decodeBase64(String encodeStr){
        try{
            return new String(Base64.getDecoder().decode(encodeStr.replace("Basic ", "")));
        }catch(Exception ex){
            return "";
        }
    }
    public static String encodeBase64(String encodeStr){
        if(encodeStr==null)
            return "";
        try {
            return Base64.getEncoder().encodeToString(encodeStr.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    public static Date convertStrToShortDate(String dateStr){
        try {
            if(dateStr!=null){
                if(dateStr.length()!=0)
                    return dateShortFormat.parse(dateStr);
                else
                    return new Date();
            }else{
                return new Date();
            }
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }
     public static Date convertStrToShortDateEndHour(String dateStr){
        try {
            return endHoursDay(dateShortFormat.parse(dateStr));
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return new Date();
        }
    }
    public static Date convertStrToLongDate(String dateStr){
        try {
            return dateLongFormat.parse(dateStr);
        } catch (ParseException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return  new Date();
        }
    }
    public static String ConvertDateToStrShort(Date date){
        if(date!=null)
            return dateShortFormat.format(date);
        else
            return "";
    }
    public static String ConvertDateToStrLong(Date date){
        if(date != null)
            return dateLongFormat.format(date);
        else
            return "";
    }
    public static Date resetTime(Date date){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.set(Calendar.HOUR_OF_DAY, 0);
       calendar.set(Calendar.MINUTE, 0);
       calendar.set(Calendar.SECOND, 0);
       calendar.set(Calendar.MILLISECOND, 0);
       return calendar.getTime();
    }
    public static Date getInitDateMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        date = resetTime(date);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return date;
    }
    public static Integer convertStringToInt(String value){
        try{
            return Integer.parseInt(value);
        }catch(Exception ex){
            return 0;
        }
    }
    public static Integer convertStringToInt(Character value){
        try{
            return Integer.parseInt(value.toString());
        }catch(Exception ex){
            return 0;
        }
    }
     public static Date getInitDateMonth(String dateStr){
        Calendar calendar = Calendar.getInstance();
        Date date = resetTime(convertStrToShortDate(dateStr));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return date;
    }
    public static Date getEndDateMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        date = resetTime(date);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH,-calendar.get(Calendar.DAY_OF_MONTH)+ 1);
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return endHoursDay(calendar.getTime());
    }
    public static Date endHoursDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
     public static Date getEndDateMonth(String dateStr){
        Calendar calendar = Calendar.getInstance();
        Date date = resetTime(convertStrToShortDate(dateStr));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH,-calendar.get(Calendar.DAY_OF_MONTH)+ 1);
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return endHoursDay(calendar.getTime());
    }
         public static String getStatus(int stat){
        String valueStatus = "";
        switch(stat){
            case 0:valueStatus = "En Juego"; break;
            case 2:valueStatus = "Ganador";break;
            case 3:valueStatus = "Perdedor";break;
            case 4:valueStatus = "Empate";break;
            case 102:valueStatus = "Suspendido";break;
            case 199:valueStatus = "-NA";break;
            default:valueStatus = "-NA";break;
        }
        
        return valueStatus;
    }
        public static String getStatus(String stat){
        String valueStatus = "";
        switch(stat){
            case "O":valueStatus = "En Juego"; break;
            case "C":valueStatus = "Cerrado";break;
            default:valueStatus = "-NA";break;
        }
         return valueStatus;
    }
        
}
