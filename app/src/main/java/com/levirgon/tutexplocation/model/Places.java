package com.levirgon.tutexplocation.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Places{

	@SerializedName("logging_info")
	private LoggingInfo loggingInfo;

	@SerializedName("html_attributions")
	private List<Object> htmlAttributions;

	@SerializedName("debug_log")
	private DebugLog debugLog;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("status")
	private String status;

	public void setLoggingInfo(LoggingInfo loggingInfo){
		this.loggingInfo = loggingInfo;
	}

	public LoggingInfo getLoggingInfo(){
		return loggingInfo;
	}

	public void setHtmlAttributions(List<Object> htmlAttributions){
		this.htmlAttributions = htmlAttributions;
	}

	public List<Object> getHtmlAttributions(){
		return htmlAttributions;
	}

	public void setDebugLog(DebugLog debugLog){
		this.debugLog = debugLog;
	}

	public DebugLog getDebugLog(){
		return debugLog;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Places{" + 
			"logging_info = '" + loggingInfo + '\'' + 
			",html_attributions = '" + htmlAttributions + '\'' + 
			",debug_log = '" + debugLog + '\'' + 
			",results = '" + results + '\'' +
			",status = '" + status + '\'' + 
			"}";
		}
}