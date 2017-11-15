package com.levirgon.whereami.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Geometry{

	@SerializedName("location")
	private Location location;

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	@Override
 	public String toString(){
		return 
			"Geometry{" + 
			"location = '" + location + '\'' + 
			"}";
		}
}