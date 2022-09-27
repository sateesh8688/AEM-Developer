package com.alchemy.newsportal.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/newsportal/services/recent-articles","/newsportal/services/featured-articles"})
public class RecentArticlesServlet extends SlingSafeMethodsServlet {
	
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		ResourceResolver resolver = request.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		Page articlePage = pageManager.getPage("/content/newsportal/us/en/articles");
		Iterator<Page> articleChildPages = articlePage.listChildren();
		JsonArrayBuilder jsonAry = Json.createArrayBuilder();
		while (articleChildPages.hasNext()) {
			Page page = (Page) articleChildPages.next();
			JsonObjectBuilder json = Json.createObjectBuilder();
			json.add("title", page.getTitle());
			json.add("path", page.getPath()+".html");
			jsonAry.add(json);
		}		
				
		response.setContentType("application/json");		
		response.getWriter().write(jsonAry.build().toString());		
	}
	
}
