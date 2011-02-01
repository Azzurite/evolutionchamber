package com.fray.evo.ui.cli;

import java.util.List;

@SuppressWarnings("serial")
public class UnknownKeywordException extends Exception{
	private final List<String> keywords;
	
	public UnknownKeywordException(List<String> keywords){
		this.keywords = keywords;
	}
	
	public List<String> getKeywords(){
		return keywords;
	}
}
