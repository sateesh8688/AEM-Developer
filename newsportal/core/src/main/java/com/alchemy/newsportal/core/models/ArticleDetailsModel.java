package com.alchemy.newsportal.core.models;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Path;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ResourcePath;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.alchemy.newsportal.core.services.ArticleService;
import com.alchemy.newsportal.core.services.EloquaIntegrationService;

@Model(
		adaptables = {Resource.class,SlingHttpServletRequest.class}, 
		resourceType = "newsportal/components/article-details",
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(extensions = "json",name = "jackson")
public class ArticleDetailsModel {

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	private String text;
	
	@ValueMapValue
	private String fileReference;
	
	@ScriptVariable
	ValueMap pageProperties;
	
	@ChildResource
	List<RelatedArticlesItemModel> relatedArticles;
	
	@OSGiService
	EloquaIntegrationService eloquaIntegrationService;
	
	@RequestAttribute
	private String type;
	
	boolean showContent = true;

	private String realtedArticles;
	

	@PostConstruct
	public void init() {

		if(pageProperties != null) {
			Date expiryDate = pageProperties.get("articleExpiry", Date.class);
	
			if (expiryDate != null) {
				Date todayDate = new Date();
				if (todayDate.compareTo(expiryDate) > 0) {
					showContent = false;
				}
			}	
		}
		
	}

	public String getText() {
		return text;
	}

	public String getFileReference() {
		return fileReference;
	}
	
	public boolean isShowContent() {
		return showContent;
	}

	public String getRealtedArticles() {
		return realtedArticles;
	}

	public List<RelatedArticlesItemModel> getRelatedArticles() {
		return relatedArticles;
	}

}
