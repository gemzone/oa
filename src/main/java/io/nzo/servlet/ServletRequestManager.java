package io.nzo.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletRequestManager implements ServletRequestListener
{
	@Override
	public void requestDestroyed(ServletRequestEvent servletRequestEvent)
	{
		//HttpServletRequest request = (HttpServletRequest)servletRequestEvent.getServletRequest();
		//HttpSession session = request.getSession();
		
		// 특정 요청에 처리
	}
	@Override
	public synchronized void requestInitialized(ServletRequestEvent servletRequestEvent)
	{
		// HttpServletRequest request = (HttpServletRequest)servletRequestEvent.getServletRequest();
		// HttpSession session = request.getSession();
		
		// 특정 요청에 처리
		//System.out.println( request.getContentType() );		application/x-www-form-urlencoded
		//System.out.println( request.getMethod() );			POST
		
	}
	
}
