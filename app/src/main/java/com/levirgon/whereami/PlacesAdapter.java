package com.levirgon.whereami;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.levirgon.whereami.model.ResultsItem;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noushad on 11/15/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private List<ResultsItem> places;
    private Context parentContext;
    private Location mCurrentLocation;


    public PlacesAdapter(Context context) {
        this.mContext = context;
        places = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        parentContext = parent.getContext();
        View viewItem = inflater.inflate(R.layout.place_item, parent, false);
        viewHolder = new PlaceVH(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ResultsItem item = places.get(position);
        PlaceVH placeVH = (PlaceVH) holder;
        placeVH.bind(item);
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }

    public void add(ResultsItem item) {
        places.add(item);
        notifyItemInserted(places.size() - 1);
    }

    public void addAll(List<ResultsItem> items) {
        for (ResultsItem item : items) {
            add(item);
        }
    }

    public void remove(ResultsItem item) {
        int position = places.indexOf(item);
        if (position > -1) {
            places.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(places.get(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation = currentLocation;
    }

    private class PlaceVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPlaceName;
        private TextView mPlaceAddress;
        private TextView mDistance;
        private RadioButton mIsOpen;

        public PlaceVH(View viewItem) {
            super(viewItem);
            mPlaceName = viewItem.findViewById(R.id.place_name_text);
            mPlaceAddress = viewItem.findViewById(R.id.place_address_text);
            mDistance = viewItem.findViewById(R.id.place_distance_text);
            mIsOpen = viewItem.findViewById(R.id.open_now_indicator);
            mIsOpen.setClickable(false);
            viewItem.setOnClickListener(this);
        }

        private void bind(ResultsItem item) {
            mPlaceName.setText(item.getName());
            mPlaceName.setTypeface(EasyFonts.caviarDreams(mContext));
            mPlaceAddress.setText(item.getVicinity());
            if (item.getOpeningHours() != null)
                mIsOpen.setChecked(item.getOpeningHours().isOpenNow());
            else {
                mIsOpen.setVisibility(View.GONE);
            }

            Location targetLocation = new Location("");
            if (item.getGeometry().getPlaceLocation()!= null) {
                targetLocation.setLatitude(item.getGeometry().getPlaceLocation().getLat());
                targetLocation.setLongitude(item.getGeometry().getPlaceLocation().getLng());
                float distanceInMeter = targetLocation.distanceTo(mCurrentLocation);
                int distance = (int) distanceInMeter;
                String unit="";
                if(distance/1000>=1){
                    distance /=1000;
                    unit = "km";
                }else{
                    unit = "m";
                }
                mDistance.setText("Distance :" + String.valueOf(distance) + unit);
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ResultsItem place = places.get(pos);
            MainActivity activity = (MainActivity) mContext;
            activity.onPlaceSelected(place);
        }
    }


}
