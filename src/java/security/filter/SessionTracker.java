/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security.filter;

/**
 *
 * @author dev00
 */
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.*;
public class SessionTracker implements HttpSessionListener {
  private static Set<HttpSession> sessions = new HashSet<>();
  @Override
  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    sessions.add(httpSessionEvent.getSession());
  }
  @Override
  public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    sessions.remove(httpSessionEvent.getSession());
  }
  public static List<HttpSession> getSessions() {
    List<HttpSession> sessionsList = new ArrayList<>(sessions);
    Collections.sort(sessionsList, new SessionComparator());
    return Collections.unmodifiableList(sessionsList);
  }
  public static HttpSession getSession(String id){
      HttpSession session = null;
      for (HttpSession httpSession : sessions) {
          if(httpSession.getId() == null ? id == null : httpSession.getId().equals(id)){
              session = httpSession;
          }
          
      }
      return session;
  }
  public static class SessionComparator implements Comparator<HttpSession> {
    @Override
    public int compare(HttpSession session1, HttpSession session2) {
      return (int) (session1.getLastAccessedTime() - session2.getLastAccessedTime());
    }
  }
}
