package com.example.beatbyte.fragmentos;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beatbyte.R;
import com.example.beatbyte.adaptador.AdaptadorBuscar;
import com.example.beatbyte.modelos.ModeloInicio;
import com.example.beatbyte.modelos.VideoYT;
import com.example.beatbyte.red.YoutubeAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BuscarFragment extends Fragment {

    private EditText txtBuscar;
    private Button btnBuscar;
    private AdaptadorBuscar adaptador;
    private LinearLayoutManager manager;
    private List<VideoYT> videoList = new ArrayList<>();

    public BuscarFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        txtBuscar = view.findViewById(R.id.txtBuscar);
        btnBuscar = view.findViewById(R.id.btnBuscar);
        RecyclerView rv = view.findViewById(R.id.recyclerQuery);

        adaptador = new AdaptadorBuscar(getContext(), videoList);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adaptador);
        rv.setLayoutManager(manager);


        btnBuscar.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(txtBuscar.getText().toString())){
                getJson(txtBuscar.getText().toString());
            } else{
                Toast.makeText(getContext(), "Necesitas buscar algo", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getJson(String query) {
        String url = YoutubeAPI.BASE_URL + YoutubeAPI.sch + YoutubeAPI.KEY + YoutubeAPI.part + YoutubeAPI.order
                + YoutubeAPI.mxresults + YoutubeAPI.query + query + YoutubeAPI.catId +  YoutubeAPI.type + YoutubeAPI.K;
        Call<ModeloInicio> datos = YoutubeAPI.getVideoInicio().getYT(url);
        datos.enqueue(new Callback<ModeloInicio>() {
            @Override
            public void onResponse(Call<ModeloInicio> call, Response<ModeloInicio> response) {
                if(response.errorBody() !=null ){
                    Log.d(TAG, "onResponse Search:  " + response.errorBody().toString());
                }else{
                    ModeloInicio mi = response.body();
                    if (mi.getItems().size() != 0) {
                        videoList.addAll(mi.getItems());
                        adaptador.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No existe el video buscado.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModeloInicio> call, Throwable t) {
                Log.d(TAG, "onFailure Search: ", t);
            }
        });
    }
}