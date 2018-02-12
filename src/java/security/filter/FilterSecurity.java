/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security.filter;

import admin.context.CommonUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import security.domain.Permission;


/**
 *
 * @author Yanny Hernandez
 */
public class FilterSecurity implements ContainerRequestFilter  {
    @Override
    public void filter(ContainerRequestContext requestContext)
                    throws IOException {
        if (!requestContext.getUriInfo().getPath().equals("/security/login")){ 
            if(requestContext.getCookies().get("JSESSIONID")==null){
                    deniedAccess(requestContext);
            }else{
             if(!checkAuthorization(requestContext.getHeaders(), requestContext.getCookies().get("JSESSIONID"))){
                 deniedAccess(requestContext);
             }
            }
        }
    }
   
    private boolean  checkAuthorization(MultivaluedMap<String,String> header,Cookie sessionid){
      List<String> lista =   header.get("Authorization");
      if((lista==null)?false:lista.size()>0){
          String credentialSend = CommonUtils.decodeBase64(lista.get(0)==null?"":lista.get(0));
          HttpSession session = SessionTracker.getSession(sessionid.getValue());
          String crendentialSave = CommonUtils.decodeBase64(session.getAttribute("Authorization").toString());
          return crendentialSave.toLowerCase().equals(credentialSend.toLowerCase());
      }
      return false;
    }
    private void deniedAccess(ContainerRequestContext requestContext){
         requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .entity("{\"error\":\"Invalid User\"}")
                    .build());
    }
}
