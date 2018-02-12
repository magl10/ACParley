package admin.context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 *
 * @author Yordanys Pupo Dieguez
 */
public class ContextUtil {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("taquilla.context.languages.language");
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat serialDateFormat = new SimpleDateFormat("ddMMyyyy");

    public static final int MEMORY_THRESHOLD   = 1048576;   // 1MB
    public static final int MAX_FILE_SIZE      = 16777216;  // 16MB
    public static final int MAX_REQUEST_SIZE   = 16782336;  //>16MB
   
 
    

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
   
    public static String formatOdds(String odds,int sport){
        odds= "+"+odds;
        if(!"+0".equals(odds.trim())&&!"+0,0".equals(odds.trim())&&!"+0.0".equals(odds.trim())){
            odds = odds.replace(",0","").replace(".0","").replace(",5","&frac12;").replace(".5","&frac12;").replace("-0","&nbsp;-").replace("+0 ","+").replace("+-","&nbsp;-");
            return   odds;}
        else
            if(sport==2||sport==1)
                return "PK";
            else
                return "";
    }
    
    public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
    }
    public static String getValueCookie(Cookie[] listcookie,String searchingCookie){
        String valuereturing ="";
        if(listcookie!=null){
            try{
                for (Cookie cookie : listcookie) {
                    if(cookie.getName() == null ? searchingCookie == null : cookie.getName().equals(searchingCookie)){
                        valuereturing = cookie.getValue();
                        break;
                    }
                }
                }catch(Exception ex){
                    valuereturing ="";
                        }
            }
        return valuereturing;
    }
     public static Cookie getCookie(Cookie[] listcookie,String searchingCookie){
        Cookie valuereturing =null;
        for (Cookie cookie : listcookie) {
            if(cookie.getName() == null ? searchingCookie == null : cookie.getName().equals(searchingCookie)){
                valuereturing = cookie;
                break;
            }
        }
        return valuereturing;
    }
    public static Cookie createCookie(String name,String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(356*24*60*60);
        return cookie;
    }
    public static String getMessage(String messageKey) {
        return BUNDLE.getString( messageKey);
    }
    public static Date getDateEnd(Date date){
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        temp.set(Calendar.HOUR_OF_DAY, 23);
        temp.set(Calendar.MINUTE, 59);
        temp.set(Calendar.SECOND, 59);
        return temp.getTime();
    }
    public static Date getDate(String date)  {
        try{
        if (date == null || date.trim().isEmpty())
            return new Date();
        if(date.length()>0){
            if(date.length()>10)
                return simpleDateFormat.parse(date.substring(0, 10).replace("-","/"));
            else
                return simpleDateFormat.parse(date.replace("-","/"));
        }
        else
            return new Date();
        }catch(ParseException ex){
            return new Date();        
        }
        
    }
        public static Date getDateUE(String date)  {
        try{
        if (date == null || date.trim().isEmpty())
            return new Date();
        if(date.length()>0)
            return simpleDateFormat2.parse(date.substring(0, 10).replace("-","/"));
        else
            return new Date();
        }catch(ParseException ex){
            return new Date();        
        }
        
    }
    public static Float stringtoFloat(String value ){
        if(value== null)
            return  0f;
            else
        return Float.parseFloat(value.replace(",", "."));
    }
    
    public static Date getSimpleDateFormat(String date) throws ParseException {
        
        return simpleDateFormat.parse(date);
    }
    
    public static String getSimpleDateFormat(Date date) {
        return simpleDateFormat.format(date);
    }
   public static Date GetDateFormatOnlyDate(String value) throws ParseException{
        Calendar c  = Calendar.getInstance();
        c.setTime(getDate(value));
        return getSimpleDateFormat(c.get(Calendar.DATE)+"/"+(c.get(Calendar.MONTH)+1) +"/"+c.get(Calendar.YEAR));
    }
    public static String GetDateFormatIso(String value) throws ParseException{
        Calendar c  = Calendar.getInstance();
        c.setTime(getDate(value));
        return c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1) +"-"+c.get(Calendar.DATE);
    }
    public static String GetDateFormatVE(String value) throws ParseException{
        Calendar c  = Calendar.getInstance();
        c.setTime(getDate(value));
        return c.get(Calendar.YEAR) +"-"+c.get(Calendar.DATE)+"-"+(c.get(Calendar.MONTH)+1);
    }
     public static SimpleDateFormat getObjSimpleDateFormat(String date) {
        return new SimpleDateFormat( date);
    }
    public static Date getSerialDateFormat(String date) throws ParseException {
        return serialDateFormat.parse(date);
    }
    
    public static String getSerialDateFormat(Date date) {
        return serialDateFormat.format(date);
    }
        
    public static void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspName) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/" + jspName + ".jsp").forward(request, response);
    }
    public static void forwardToJspRoot(HttpServletRequest request, HttpServletResponse response, String jspName) throws ServletException, IOException {
        request.getRequestDispatcher( jspName + ".jsp").forward(request, response);
    }
    public static void forwardToUrl(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        request.getRequestDispatcher(url).forward(request, response);
    }
    
    
    
    public static void sendBLMessage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(message);
        out.flush();
    }
    
   
    public static void sendTBMessage(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(message);
        out.flush();
    }
    
    public static int stringToInt(String value) {
        if(value== null) return -1;
        else{
            value=value.replace(",", ".");
            return Integer.parseInt(value);
        }
    }
    
 
    
    public static String formatDouble(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00", new DecimalFormatSymbols(Locale.getDefault()));
        return decimalFormat.format(value);
    }
    
   
       
}
