package com.alchemy.newsportal.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/newsportal/services/recent-articles-qb"})
public class RecentArticlesQBServlet extends SlingSafeMethodsServlet {
	
	@Reference
	QueryBuilder queryBuilder;
	
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		ResourceResolver resolver = request.getResourceResolver();
		JsonArrayBuilder jsonAry = Json.createArrayBuilder();
		
		Map<String, String> props = new HashMap<>();		
		props.put("type", "cq:Page");
		props.put("path", "/content/newsportal/us/en/articles");
		props.put("group.1_property", "jcr:content/cq:template");
		props.put("group.1_property.value", "/conf/newsportal/settings/wcm/templates/article-template");
		props.put("group.2_property", "jcr:content/sling:resourceType");
		props.put("group.2_property.value", "newsportal/components/article-page");
		props.put("group.p.or", "true");
		props.put("orderby", "@jcr:content/jcr:created");
		props.put("orderby.sort", "desc");
		props.put("p.limit", "5");
		
		Query query = queryBuilder.createQuery(PredicateGroup.create(props), resolver.adaptTo(Session.class));
		List<Hit> result = query.getResult().getHits();
		
		for(Hit hit : result) {
			try {
				Resource resource = hit.getResource();
				Page page = resource.adaptTo(Page.class);
				JsonObjectBuilder json = Json.createObjectBuilder();
				json.add("title", page.getTitle());
				json.add("path", page.getPath()+".html");
				jsonAry.add(json);
			} catch (RepositoryException e) {
			}
		}
				
		response.setContentType("application/json");		
		response.getWriter().write(jsonAry.build().toString());		
	}
	
}
