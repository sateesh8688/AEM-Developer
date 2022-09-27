package com.alchemy.newsportal.core.models;

public class TagInfo {

	private String tagId;
	
	private String tagTitle;
	
	private Long count;
	
	public TagInfo(String tagId,String tagTitle,Long count) {
		this.tagId = tagId;
		this.tagTitle = tagTitle;
		this.count = count;
	}

	public String getTagId() {
		return tagId;
	}

	public String getTagTitle() {
		return tagTitle;
	}

	public Long getCount() {
		return count;
	}	
	
}
