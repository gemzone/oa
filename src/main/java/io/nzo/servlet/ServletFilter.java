package io.nzo.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ServletFilter implements Filter
{
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{
		// 필터적용
		((HttpServletResponse)res)
			.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
		
		chain.doFilter(req, res);
	}
	
	@Override 
	public void destroy() {
		
	}
	
	@Override 
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
