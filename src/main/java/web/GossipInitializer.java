package web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;

import model.UserService;

/**
 * Application Lifecycle Listener implementation class GossipInitializer
 *
 */
@WebListener
public class GossipInitializer implements ServletContextListener {
	
    /**
     * Default constructor. 
     */
    public GossipInitializer() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	var context = sce.getServletContext();
    	var USERS = context.getRealPath("/users");
    	context.setAttribute("userService", new UserService(USERS));
    }
	
}
