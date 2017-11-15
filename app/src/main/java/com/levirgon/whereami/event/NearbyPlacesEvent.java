package com.levirgon.whereami.event;

import com.levirgon.whereami.model.ResultsItem;

import java.util.List;

/**
 * Created by noushad on 11/15/17.
 */

public class NearbyPlacesEvent {

    private List<ResultsItem> places;

    public NearbyPlacesEvent(List<ResultsItem> places) {
        this.places = places;
    }

    public List<ResultsItem> getPlaces() {
        return places;
    }
}
