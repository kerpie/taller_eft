package com.androidheroes.library.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidheroes.library.CategoriesFragment;
import com.androidheroes.library.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by kerry on 17/04/15.
 */
public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder> {

    public JSONArray content;
    Context context;

    CategoriesFragment.OnCategorySelected listener;

    public RecyclerViewCustomAdapter(Context context, JSONArray json_array, CategoriesFragment.OnCategorySelected listener){
        content = json_array;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.item_category_in_list, parent, false);

        ViewHolder view_holder = new ViewHolder(view);

        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //Insertamos la texto obtenido desde el sistema web
        try {
            viewHolder.category_title.setText(content.getJSONObject(i).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Insertamos el imagen obtenido desde el sistema web
        //Utilizamos la libreria Picasso para el cargado e insercion de la imagen
        Picasso.with(context).load("http://lorempixel.com/150/150/animals/" + (i + 1)).into(viewHolder.category_image);
    }

    @Override
    public int getItemCount() {
        return content.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView category_title;
        ImageView category_image;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            category_title = (TextView) view.findViewById(R.id.category_name_in_item);
            category_image = (ImageView) view.findViewById(R.id.category_image_in_item);
            card_view = (CardView) view.findViewById(R.id.category_item_card);

            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                listener.onCategorySelected(content.getJSONObject(getPosition()).getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
