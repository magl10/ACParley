/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import security.controller.SecurityController;
import security.domain.Permission;

/**
 *
 * @author Yanny Hernandez
 */
public class SecurityServlet extends GenericServlet{
   public void init(ServletConfig servletConfig) throws ServletException{
       SecurityController controller = new SecurityController();
       controller.getPermission();
   System.out.print("Ejecuntando");
  }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
