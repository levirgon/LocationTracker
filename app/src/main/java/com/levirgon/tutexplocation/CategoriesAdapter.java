package com.levirgon.tutexplocation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.levirgon.tutexplocation.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noushad on 11/15/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private List<String> categories;
    private Context parentContext;


    public CategoriesAdapter(Context context) {
        this.mContext = context;
        categories = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        parentContext = parent.getContext();
        View viewItem = inflater.inflate(R.layout.list_item, parent, false);
        viewHolder = new CategoryVH(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String text = categories.get(position);
        CategoryVH categoryVH = (CategoryVH) holder;
        categoryVH.bind(text);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    private class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Button mTitleText;

        public CategoryVH(View viewItem) {
            super(viewItem);
            mTitleText = viewItem.findViewById(R.id.categorie_title);
            mTitleText.setOnClickListener(this);
        }

        private void bind(String text) {
            mTitleText.setText(text);
            mTitleText.setTypeface(EasyFonts.caviarDreams(mContext));
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String text = categories.get(pos);
            Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
            MainActivity activity = (MainActivity) mContext;
            activity.onItemSelected(text);
            /*Intent intent = new Intent(activity,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryText",text);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);*/
        }
    }

    public void add(String item) {
        categories.add(item);
        notifyItemInserted(categories.size() - 1);
    }

    public void addAll(List<String> items) {
        for (String item : items) {
            add(item);
        }
    }

    public void remove(String item) {
        int position = categories.indexOf(item);
        if (position > -1) {
            categories.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(categories.get(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

}
