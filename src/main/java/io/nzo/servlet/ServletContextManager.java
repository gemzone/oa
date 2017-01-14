package io.nzo.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ServletContextManager implements ServletContextListener
{
	public static ServletContext servletContext = null;
	
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		
		servletContext = event.getServletContext();
		
		
		
		
		
		
/*
		
		try
		{
			ContextManager.connection = ds.getConnection();
		}
		catch (Exception e)
		{
			System.out.println( "Can't get Connection from getConnection(Context):" + e.getMessage());
			e.printStackTrace();
		}
*/
		
	//	TimeUnit time = TimeUnit.MINUTES;			// 1분 마다
	//	long value = 1;
		
		/*
		
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() 
		{
			@Override
			public void run()
			{
				System.out.println("Thread run OK.");

			}
		}, 0, value, time);
		*/
		
		
		
		/*
		try(Procedure db = new Procedure(Server.getConnection() ))
		{
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	}
}
