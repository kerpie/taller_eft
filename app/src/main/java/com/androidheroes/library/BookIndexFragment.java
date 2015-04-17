package com.androidheroes.library;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidheroes.library.adapter.BookIndexCustomAdapter;
import com.androidheroes.library.volley.VolleySingleton;

import org.json.JSONObject;

public class BookIndexFragment extends Fragment {

    private OnBookSelected  listener;

    public TextView name;
    public RecyclerView book_list;
    public ProgressBar book_list_loader;
    public TextView error_message;

    //Componentes para el RecyclerView
    LinearLayoutManager layout_manager;
    BookIndexCustomAdapter content_adapter;

    //Componentes para la conexión con el sistema web
    private VolleySingleton volley_client;
    protected RequestQueue request_queue;

    public BookIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_index, container, false);

        name = (TextView) view.findViewById(R.id.book_index_choose_category_text);
        book_list_loader = (ProgressBar) view.findViewById(R.id.book_list_loader);
        book_list = (RecyclerView) view.findViewById(R.id.book_list);
        error_message = (TextView) view.findViewById(R.id.book_list_error_message);

        book_list_loader.setVisibility(View.GONE);
        book_list.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);

        book_list.setHasFixedSize(true);

        //Creamos un administrador para el contenido de la lista
        layout_manager = new LinearLayoutManager(getActivity());

        //Indicamos que el listado utilice el administrador que acabas de crear
        book_list.setLayoutManager(layout_manager);

        volley_client = VolleySingleton.getInstance(getActivity().getApplicationContext());
        request_queue = volley_client.getRequest_queue();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnBookSelected ) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBookSelected {
        // TODO: Update argument type and name
        public void onBookSelected(int book_id);
    }

    public void hideTextView(){
        name.setVisibility(View.GONE);
    }

    public void makeRequest(int category_id){
        String url = "https://eft-library.herokuapp.com/categories/"+category_id+".json";
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                content_adapter = new BookIndexCustomAdapter(getActivity(), response, listener);
                book_list.setAdapter(content_adapter);
                onConnectionFinished();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error.toString());
            }
        });
        addToQueue(request);
    }

    public void addToQueue(Request request){
        if(request != null){
            request.setTag(this);
            if (request_queue == null) {
                request_queue = volley_client.getRequest_queue();
            }
            request.setRetryPolicy(new DefaultRetryPolicy(6000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            onPreStartConnection();
            request_queue.add(request);
        }
    }

    public void onPreStartConnection() {
        //Escondemos el listado y mostramos el cargador
        name.setVisibility(View.GONE);
        book_list_loader.setVisibility(View.VISIBLE);
        book_list.setVisibility(View.GONE);
        error_message.setVisibility(View.GONE);
    }

    public void onConnectionFinished() {
        //Escondemos el cargador y mostramos el listado
        name.setVisibility(View.GONE);
        book_list_loader.setVisibility(View.GONE);
        book_list.setVisibility(View.VISIBLE);
        error_message.setVisibility(View.GONE);
    }

    public void onConnectionFailed(String error) {
        name.setVisibility(View.GONE);
        book_list_loader.setVisibility(View.GONE);
        book_list.setVisibility(View.GONE);
        error_message.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

}
