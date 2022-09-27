package com.alchemy.newsportal.core.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
		resourceTypes = "newsportal/components/article-page",
		methods = {HttpConstants.METHOD_GET, HttpConstants.METHOD_POST},
		extensions = "json",
		selectors = {"find","recent"}
		)
public class FindArticlesSQLServlet extends SlingSafeMethodsServlet {
	
	
	
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		String param = request.getParameter("q");
		
		ResourceResolver resolver = request.getResourceResolver();
		JsonArrayBuilder jsonAry = Json.createArrayBuilder();
		String[] selectors = request.getRequestPathInfo().getSelectors();
		
		String findPagesQuery  = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) and CONTAINS(s.*, '"+param+"')"; 
		String recentArticlesQuery = "SELECT * FROM [cq:Page] AS s WHERE ISDESCENDANTNODE([/content/newsportal]) AND s.[jcr:content/cq:template]='/conf/newsportal/settings/wcm/templates/article-template' AND s.[jcr:content/sling:resourceType]='newsportal/components/article-page' AND s.[jcr:content/jcr:created] >= CAST('2022-08-01T16:56:38.474+05:30' AS DATE) AND  s.[jcr:content/jcr:created] <= CAST('2022-08-31T16:56:38.474+05:30' AS DATE)";
		
		String query = null;
		
		if(Arrays.stream(selectors).anyMatch("find"::equals)) {	
			query = findPagesQuery;
		} else {
			query = recentArticlesQuery;
		}		
		
		Iterator<Resource>  result = resolver.findResources(query, Query.JCR_SQL2);
		while (result.hasNext()) {
			Resource resource = (Resource) result.next();
			Page page = resource.adaptTo(Page.class);
			JsonObjectBuilder json = Json.createObjectBuilder();
			json.add("title", page.getTitle());
			json.add("path", page.getPath()+".html");
			jsonAry.add(json);
		}		
						
		response.setContentType("application/json");		
		response.getWriter().write(jsonAry.build().toString());		
	}
	
}
