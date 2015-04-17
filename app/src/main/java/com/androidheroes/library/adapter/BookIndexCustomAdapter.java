package com.androidheroes.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidheroes.library.BookIndexFragment;
import com.androidheroes.library.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kerry on 17/04/15.
 */
public class BookIndexCustomAdapter extends RecyclerView.Adapter<BookIndexCustomAdapter.ViewHolder>{

    public JSONObject content;
    Context context;
    BookIndexFragment.OnBookSelected listener;

    public BookIndexCustomAdapter(Context context, JSONObject content, BookIndexFragment.OnBookSelected listener){
        this.content = content;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_in_list, parent, false);

        ViewHolder view_holder = new ViewHolder(view);

        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Insertamos la texto obtenido desde el sistema web
        try {
            holder.book_title.setText(content.getJSONArray("books").getJSONObject(position).getString("title"));
            holder.book_author.setText(content.getJSONArray("books").getJSONObject(position).getString("author"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Insertamos el imagen obtenido desde el sistema web
        //Utilizamos la libreria Picasso para el cargado e insercion de la imagen
        try {
            Picasso.with(context)
                    .load("https://eft-library.herokuapp.com" + content.getJSONArray("books").getJSONObject(position).getString("image"))
                    .into(holder.book_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        try {
            return content.getJSONArray("books").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView book_title;
        TextView book_author;
        ImageView book_image;

        public ViewHolder(View view) {
            super(view);
            book_title = (TextView) view.findViewById(R.id.book_title);
            book_author = (TextView) view.findViewById(R.id.book_author);
            book_image = (ImageView) view.findViewById(R.id.book_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                listener.onBookSelected(content.getJSONArray("books").getJSONObject(getPosition()).getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
