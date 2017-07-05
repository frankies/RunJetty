package org.jetty.run;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Hello world!
 *
 */
public class App {

  private final static String SHUTDOWN_SECRET_TOKEN = "#02924e4lfdddlkddjd959595sasdjidd3wsdd3fa32993-2f020d939-LL33KASDAsadfdKSDJK#";

  public static void main(String[] args) throws Exception {
    startUpWithJsp(args);
  }

  private static void startUpWithJsp(String[] args) throws Exception {

    if (args.length == 0) {
      System.err.println(" The .war file path must be input!");
      return;
    }

    
    String warFilePath = args[0];
    System.out.println("Input .war file path:" + warFilePath);

    int port = 8080;
    if (isShutdownCmd(args)) {
      System.out.println("Shutting server at port '" + port + "'");
      if(args.length > 1) {
        port = Integer.parseInt(args[0]); // args: [java class] 8080 shutdown
      }
      System.out.println("Server port: " + port);
      attemptShutdown(port, SHUTDOWN_SECRET_TOKEN);
      return ;
    }else if (args.length >= 2){
        port = Integer.parseInt(args[1]);
    }
    System.out.println("Starting server at port '" + port + "'");
    
    Server server = new Server(port);

    // Setup JMX
    // MBeanContainer mbContainer = new MBeanContainer(
    // ManagementFactory.getPlatformMBeanServer());
    // server.addBean(mbContainer);

    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath("/");
    File warFile = new File(warFilePath);
    if (!warFile.exists()) {
      throw new RuntimeException(
          "Unable to find WAR File: " + warFile.getAbsolutePath());
    }
    webapp.setWar(warFile.getAbsolutePath());

    // This webapp will use jsps and jstl. We need to enable the
    // AnnotationConfiguration in order to correctly
    // set up the jsp container
    Configuration.ClassList classlist = Configuration.ClassList
        .setServerDefault(server);
    classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
        "org.eclipse.jetty.annotations.AnnotationConfiguration");

    // Set the ContainerIncludeJarPattern so that jetty examines these
    // container-path jars for tlds, web-fragments etc.
    // If you omit the jar that contains the jstl .tlds, the jsp engine will
    // scan for them instead.
    webapp.setAttribute(
        "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
        ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$");

    // Shutdown handle..
    Handler shutdown = new ShutdownHandler(SHUTDOWN_SECRET_TOKEN, true, false);

    // A WebAppContext is a ContextHandler as well so it needs to be set to
    // the server so it is aware of where to
    // send the appropriate requests.
    server.setHandler(new HandlerList(shutdown, webapp));

    // Configure a LoginService.
    // Since this example is for our test webapp, we need to setup a
    // LoginService so this shows how to create a very simple hashmap based
    // one. The name of the LoginService needs to correspond to what is
    // configured in the webapp's web.xml and since it has a lifecycle of
    // its own we register it as a bean with the Jetty server object so it
    // can be started and stopped according to the lifecycle of the server
    // itself.
    // HashLoginService loginService = new HashLoginService();
    // loginService.setName("Test Realm");
    // loginService.setConfig("src/test/resources/realm.properties");
    // server.addBean(loginService);

    // Start things up!
    server.start();

    // The use of server.join() the will make the current thread join and
    // wait until the server is done executing.
    // See http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
    server.join();
  }

  public static void attemptShutdown(int port, String shutdownCookie) {
    try {
      URL url = new URL(
          "http://localhost:" + port + "/shutdown?token=" + URLEncoder.encode(shutdownCookie, "UTF-8"));
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      int sc = connection.getResponseCode();
      boolean isOk = sc == 200;
      
      if(isOk) {
        System.out.println("Shutting down Ok!");
      }else {
        System.err.println("Shutting down " + "failed, status code:" + connection.getResponseCode()+ ", message:" + connection.getResponseMessage());
      }
    } catch (Exception e) {
      System.err.println("Shutting down " + "failed, may be target server is closed? ");
      System.exit(2);
    }
  }

  private static boolean isShutdownCmd(String[] args) {

    for (String arg : args) {
      if (arg.toLowerCase().indexOf("shutdown") > -1) {
        return true;
      }
    }
    return false;
  }
}
