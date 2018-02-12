/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package admin.config;

import java.util.Set;
import javax.ws.rs.core.Application;
import security.service.SecurityService;

/**
 *
 * @author Yanny Hernandez
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(admin.context.GZIPWriterInterceptor.class);
        resources.add(admin.service.ServiceAgent.class);
        resources.add(admin.service.ServiceGame.class);
        resources.add(admin.service.ServicePlayer.class);
        resources.add(admin.service.ServiceReport.class);
        resources.add(admin.service.ServiceTicket.class);
        resources.add(security.service.SecurityService.class);
    }
    
}
