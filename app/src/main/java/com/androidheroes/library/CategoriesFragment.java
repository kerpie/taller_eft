package com.androidheroes.library;

import android.app.Activity;
import android.net.LinkAddress;
import android.os.Bundle;
import android.app.Fragment;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.androidheroes.library.adapter.RecyclerViewCustomAdapter;
import com.androidheroes.library.volley.VolleySingleton;

import org.json.JSONArray;

public class CategoriesFragment extends Fragment {

    //Objeto para la comunicación con la Actividad
    private OnCategorySelected listener;

    //Componentes de la interfaz
    RecyclerView categories_list;
    ProgressBar loader;
    TextView error_message;

    //Componentes para el RecyclerView
    LinearLayoutManager layout_manager;
    RecyclerViewCustomAdapter content_adapter;

    //Componentes para la conexión con el sistema web
    private VolleySingleton volley_client;
    protected RequestQueue request_queue;

    //Constructor por defecto
    public CategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //Iniciamos la instanciación del objeto, extrayendolo de la interfaz
        categories_list = (RecyclerView) view.findViewById(R.id.categories_list);
        loader = (ProgressBar) view.findViewById(R.id.categories_list_loader);
        error_message = (TextView) view.findViewById(R.id.categories_list_error_message);

        onPreStartConnection();

        categories_list.setHasFixedSize(true);

        //Creamos un administrador para el contenido de la lista
        layout_manager = new LinearLayoutManager(getActivity());

        //Indicamos que el listado utilice el administrador que acabas de crear
        categories_list.setLayoutManager(layout_manager);

        volley_client = VolleySingleton.getInstance(getActivity().getApplicationContext());
        request_queue = volley_client.getRequest_queue();

        makeRequest();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCategorySelected) activity;
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

    public interface OnCategorySelected {
        // TODO: Update argument type and name
        public void onCategorySelected(int position);
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

    private void makeRequest(){
        String url = "https://eft-library.herokuapp.com/categories.json";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                //Creamos un adaptador de contenido para que 'adapte' el contenido crudo que obtienes de la web a cada articulo dentro de la lista
                content_adapter = new RecyclerViewCustomAdapter(getActivity(), jsonArray, listener);
                categories_list.setAdapter(content_adapter);
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
    }

    public void onPreStartConnection() {
        //Escondemos el listado y mostramos el cargador
        loader.setVisibility(View.VISIBLE);
        categories_list.setVisibility(View.GONE);
    }

    public void onConnectionFinished() {
        //Escondemos el cargador y mostramos el listado
        loader.setVisibility(View.GONE);
        categories_list.setVisibility(View.VISIBLE);
    }

    public void onConnectionFailed(String error) {
        loader.setVisibility(View.GONE);
        categories_list.setVisibility(View.GONE);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

}