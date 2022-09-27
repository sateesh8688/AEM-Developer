package com.alchemy.newsportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.wcm.core.components.models.Title;

@Model(
		adaptables = {Resource.class, SlingHttpServletRequest.class},
		adapters = TitleModel.class,
		resourceType = "newsportal/components/title",
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
		)
public class TitleModelImpl implements TitleModel {
	
	@Self
	@Via(type = ResourceSuperType.class)
	Title title;
	
	@ValueMapValue
	private String subTitle;

	public String getSubTitle() {
		return subTitle;
	}
	
	@Override
	public String getText() {
        return title.getText();
    }
	
	@Override
	public String getType() {
        return title.getType();
    }
	
}
