/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package security.controller;

import admin.context.CommonUtils;
import admin.domain.Office;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.domain.Authentication;
import security.domain.Permission;
import security.domain.PermissionDao;

/**
 * Clase Controlladora de la seguridad de usuario
 * @author Yanny Hernandez
 */
public class SecurityController {
    private final String GET = "select * from permission where app = ?";
    private final String ADD = "insert into Permission(app,service,method,description) values(?,?,?,?)";    
    public Authentication logon(String username, String pass){
       Office login = null;
       Authentication logeo = new Authentication();
        try{
            Connection cnn = CommonUtils.getConnection();
            String query = "select offices.* from offices inner join users on users.loginname = offices.login and  users.loginname = ? and passwd = ?";
           PreparedStatement statement = cnn.prepareStatement(query);
           statement.setString(1, username);
           statement.setString(2, pass);
           ResultSet rst = statement.executeQuery();
           if((rst.next())){
                login = createLogin(rst);
                if(login!=null){
                    logeo.setSuccess(Boolean.TRUE);
                    logeo.setMessage("Bienvenido al Manejador de Agencias");
                }
           }else{
                logeo.setSuccess(Boolean.FALSE);
                logeo.setMessage("Usuario o Contraseña Incorrecto");
           }
           rst.close();
           cnn.close();
       }catch(SQLException ex){
           Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
           logeo.setSuccess(Boolean.FALSE);
           logeo.setMessage(":( Hubo Problemas Conectandonos Con el Servidor Intentelo Nuevamente");
       }
       logeo.setLogin(login);
       return logeo;
    }
    public  List<Permission> getPermission(){
          List<Permission> permissions = new ArrayList<>();
          List<Class> classes = new ArrayList<>(Arrays.asList( getClassesInPackage("admin.service")));
          List<Method> methods = new ArrayList<>();
          classes.stream().forEach( clase->methods.addAll(Arrays.asList(clase.getDeclaredMethods())));
          for (Method method : methods) {
              List<Permission> permisionTemp =Arrays.asList(method.getAnnotation(Permission.class));
              if(permisionTemp.get(0)!=null)
              permissions.addAll(permisionTemp);       
          }
        updatePermission(permissions);
        return permissions;
    }
    public Authentication changepass(String user,String pass,String newpass){
         Authentication change = new Authentication();
         try{
              Connection cnn = CommonUtils.getConnection();
              String query = "update offices set clave= ? where officeid = ? and  clave = ?";
              PreparedStatement statement = cnn.prepareStatement(query);
           statement.setString(1, newpass);
           statement.setString(2, user);
           statement.setString(3, pass);
           if(statement.executeUpdate()>0){
                change.setSuccess(Boolean.TRUE);
                change.setMessage("Cambio de Password Exitoso");
           }else{
                change.setSuccess(Boolean.FALSE);
                change.setMessage("Usuario o Contraseña Incorrecto");
           }
         }catch(Exception ex){
              Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
              change.setSuccess(Boolean.FALSE);
              change.setMessage(":( Hubo Problemas Conectandonos Con el Servidor Intentelo Nuevamente");
         }
         return change;
     }
    private void updatePermission(List<Permission> permissions){
        try {
            int idApp = getPropIdApp();
            List<PermissionDao> permissionSave = getPermissionSave();
            for (Permission permission : permissions) {
                boolean save = false;
                for(PermissionDao permissiondao: permissionSave){
                    if(permission.method().equals(permissiondao.getMethod())&&permission.service().equals(permissiondao.getService())&&idApp==permissiondao.getApp())
                    {
                        save = save = true;
                        break;
                    }
                }
                if(!save){
                    savePermission(permission,idApp);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    private boolean savePermission(Permission permission, int app){
        boolean success = true;
        try{
            Connection cnn = CommonUtils.getConnection();
            PreparedStatement cmd = cnn.prepareStatement(ADD);
            cmd.setInt(1, app);
            cmd.setString(2, permission.service());
            cmd.setString(3, permission.method());
            cmd.setString(4, permission.description());
            success = cmd.executeUpdate()>0;
            cmd.close();
            cnn.close();
        }catch(SQLException ex){
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }
    private List<PermissionDao> getPermissionSave() throws IOException{
        List<PermissionDao> permission = new ArrayList<>();
        try {
            Connection cnn = CommonUtils.getConnection();
            PreparedStatement cmd = cnn.prepareStatement(GET);
               cmd.setInt(1, getPropIdApp());
            ResultSet result = cmd.executeQuery();
         
            while(result.next()){
                permission.add(createPermission(result));
            }
            cmd.close();
            cnn.close();
            
        }catch (SQLException ex) {
            Logger.getLogger(SecurityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return permission;
    }
    private PermissionDao createPermission(ResultSet result) throws SQLException{
        PermissionDao permission = new PermissionDao();
        permission.setIdPermission(result.getInt("idPermission"));
        permission.setApp(result.getInt("app"));
        permission.setService(result.getString("service"));
        permission.setMethod(result.getString("method"));
        permission.setDescription(result.getString("description"));
        return permission;
    }
    private Office createLogin(ResultSet loginBrute) throws SQLException{
        Office login = new Office();
       login.setNombre(loginBrute.getString("OFFICEDESC"));
       login.setUsername(loginBrute.getString("login"));
       login.setGrupo(loginBrute.getString("grupo"));
       login.setOfficeid(loginBrute.getInt("officeid"));
       login.setLink(loginBrute.getString("link"));
       login.setPassword(CommonUtils.encodeBase64(loginBrute.getString("clave")));
       return login;
    }
    private  Class[] getClassesInPackage(String pckgname) {
        File directory = getPackageDirectory(pckgname);
        if (!directory.exists()) {
            throw new IllegalArgumentException("Could not get directory resource for package " + pckgname + ".");
        }
 
        return getClassesInPackage(pckgname, directory);
    }
    private  Class[] getClassesInPackage(String pckgname, File directory) {
        List<Class> classes = new ArrayList<Class>();
        for (String filename : directory.list()) {
            if (filename.endsWith(".class")) {
                String classname = buildClassname(pckgname, filename);
                try {
                    classes.add(Class.forName(classname));
                } catch (ClassNotFoundException e) {
                    System.err.println("Error creating class " + classname);
                }
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }
    private  String buildClassname(String pckgname, String filename) {
        return pckgname + '.' + filename.replace(".class", "");
    }
    private  File getPackageDirectory(String pckgname) {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if (cld == null) {
            throw new IllegalStateException("Can't get class loader.");
        }
 
        URL resource = cld.getResource(pckgname.replace('.', '/'));
        if (resource == null) {
            throw new RuntimeException("Package " + pckgname + " not found on classpath.");
        }
 
        return new File(resource.getFile());
    }
    private int getPropIdApp() throws IOException {
		Properties prop = new Properties();
		String propFileName = "admin/config/config.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		String idApp = prop.getProperty("idApp");
		return Integer.parseInt(idApp);
	}
}
