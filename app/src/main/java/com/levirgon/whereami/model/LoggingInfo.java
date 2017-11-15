package com.levirgon.whereami.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class LoggingInfo{

	@SerializedName("experiment_id")
	private List<Object> experimentId;

	@SerializedName("query_geographic_location")
	private String queryGeographicLocation;

	public void setExperimentId(List<Object> experimentId){
		this.experimentId = experimentId;
	}

	public List<Object> getExperimentId(){
		return experimentId;
	}

	public void setQueryGeographicLocation(String queryGeographicLocation){
		this.queryGeographicLocation = queryGeographicLocation;
	}

	public String getQueryGeographicLocation(){
		return queryGeographicLocation;
	}

	@Override
 	public String toString(){
		return 
			"LoggingInfo{" + 
			"experiment_id = '" + experimentId + '\'' + 
			",query_geographic_location = '" + queryGeographicLocation + '\'' + 
			"}";
		}
}