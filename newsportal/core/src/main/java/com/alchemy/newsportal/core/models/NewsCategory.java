package com.alchemy.newsportal.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class})
public class NewsCategory {

	@SlingObject
	ResourceResolver resolver;
	
	@ScriptVariable
	Page currentPage;
	
	private List<TagInfo> tagsList;
	
	@PostConstruct
	public void init() {
		tagsList = new ArrayList<>();
		TagManager tagManager = resolver.adaptTo(TagManager.class);
		Tag categoryTag = tagManager.resolve("newsportal:category");
		Iterator<Tag> childTags = categoryTag.listChildren();
		while (childTags.hasNext()) {
			Tag tag = (Tag) childTags.next();			
			tagsList.add(new TagInfo(tag.getTagID(), tag.getLocalizedTitle(currentPage.getLanguage()), tag.getCount()));
		}
	}

	public List<TagInfo> getTagsList() {
		return tagsList;
	}	
	
}
