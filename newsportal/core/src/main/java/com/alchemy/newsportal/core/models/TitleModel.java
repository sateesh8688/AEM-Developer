package com.alchemy.newsportal.core.models;

import com.adobe.cq.wcm.core.components.models.Title;

public interface TitleModel extends Title {
	
	default String getSubTitle() {
        return null;
    }	
	
}
