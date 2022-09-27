package com.alchemy.newsportal.core.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alchemy.newsportal.core.constants.Constants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
		resourceTypes = "newsportal/components/article-page",
		methods = {HttpConstants.METHOD_GET, HttpConstants.METHOD_POST},
		extensions = "json",
		selectors = {ArticlesInCategoryServlet.RELATED_ARTICLES,ArticlesInCategoryServlet.IN_CATEGORY}
		)
public class ArticlesInCategoryServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -8553463026917506169L;
	
	public static final String IN_CATEGORY = "incategory";
	public static final String RELATED_ARTICLES = "relatedarticles";
	
	private static final Logger LOG = LoggerFactory.getLogger(ArticlesInCategoryServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest request,final SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType(Constants.JSON_CONTENT_TYPE);
		String[] selectors = request.getRequestPathInfo().getSelectors();
		boolean inCategory = Arrays.stream(selectors).anyMatch(IN_CATEGORY::equals);
		boolean relatedArticles = Arrays.stream(selectors).anyMatch(RELATED_ARTICLES::equals);
		
		ResourceResolver resolver = request.getResourceResolver();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		
		String result = "";	
		Resource resource = request.getResource();
    	String[] articleTags = resource.getValueMap().get("cq:tags", String[].class);
    	
    	String categoryTag = findCategoryTag(articleTags,inCategory?true:false);        	
    	    	
    	LOG.info("Category Tag {}",categoryTag);
    	if(categoryTag != null) {
    		Tag categoryTagObj = tagManager.resolve(categoryTag);
    		if(categoryTagObj!=null) {
    			Iterator<Resource> categoryResources = categoryTagObj.find();
    			JsonArrayBuilder resultAry = Json.createArrayBuilder();    			
    			while (categoryResources.hasNext()) {
					
    				JsonObjectBuilder json = Json.createObjectBuilder();
    				Resource categoryResource = (Resource) categoryResources.next();
    				    				
    				Resource articleDetailResource = resolver.getResource(categoryResource.getPath()+"/root/article_grid/comp-left/article_details");
    				if(articleDetailResource != null) {
    					json.add("image", articleDetailResource.getValueMap().get("fileReference", ""));
    				}
    				
    				ValueMap properties = categoryResource.getValueMap();
    				json.add("title", properties.get("jcr:title", ""));
    				json.add("path", categoryResource.getParent().getPath()+".html");
    				if(properties.get("articleExpiry",Date.class) != null) {
    					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
                        String strDate = dateFormat.format(properties.get("articleExpiry",Date.class)); 
                        json.add("Article Expiry", strDate);
    				}    				 
                    resultAry.add(json);					
				}
    			result = resultAry.build().toString();
    		}
    	}
    	
		response.getWriter().write(result);
	}
		
	private String findCategoryTag(String[] articleTags, boolean categoryRootTag) {
    	if(articleTags != null && articleTags.length >0) {
    		for(String articleTag : articleTags) {
    			if(articleTag.startsWith(Constants.CATEGORY_TAG)) {
    				if(categoryRootTag) {
    					String[] tagStr = articleTag.split("/");
        				if(tagStr.length>=2) {
        					return tagStr[0]+"/"+tagStr[1];
        				}
    				} else {
    					return articleTag;
    				}   				
    			}
    		}
    	}
    	return null;
	}
	
}
