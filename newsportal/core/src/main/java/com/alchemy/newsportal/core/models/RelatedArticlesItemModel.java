package com.alchemy.newsportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RelatedArticlesItemModel {

	@ValueMapValue
	private String pagePath;
	
	@ValueMapValue
	private String pageTitle;

	public String getPagePath() {
		return pagePath;
	}

	public String getPageTitle() {
		return pageTitle;
	}
	
}
