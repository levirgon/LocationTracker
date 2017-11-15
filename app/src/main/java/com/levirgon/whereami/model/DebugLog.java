package com.levirgon.whereami.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DebugLog{

	@SerializedName("line")
	private List<Object> line;

	public void setLine(List<Object> line){
		this.line = line;
	}

	public List<Object> getLine(){
		return line;
	}

	@Override
 	public String toString(){
		return 
			"DebugLog{" + 
			"line = '" + line + '\'' + 
			"}";
		}
}