package com.levirgon.whereami;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levirgon.whereami.model.ResultsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noushad on 11/15/17.
 */

public class PlacesAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private List<ResultsItem> places;
    private Context parentContext;


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

//        String text = places.get(position);
//        CategoryVH categoryVH = (CategoryVH) holder;
//        categoryVH.bind(text);
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }

//    private class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private final Button mTitleText;
//
//        public CategoryVH(View viewItem) {
//            super(viewItem);
//            mTitleText = viewItem.findViewById(R.id.categorie_title);
//            mTitleText.setOnClickListener(this);
//        }
//
//        private void bind(String text) {
//            mTitleText.setText(text);
//            mTitleText.setTypeface(EasyFonts.caviarDreams(mContext));
//        }
//
//        @Override
//        public void onClick(View v) {
//            int pos = getAdapterPosition();
//            String text = places.get(pos);
//            Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
//            MainActivity activity = (MainActivity) mContext;
//            activity.onItemSelected(text);
//        }
//    }

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

    private class PlaceVH extends RecyclerView.ViewHolder {
        public PlaceVH(View viewItem) {
            super(viewItem);
        }
    }
}
