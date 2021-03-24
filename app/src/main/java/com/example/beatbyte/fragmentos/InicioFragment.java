package com.example.beatbyte.fragmentos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beatbyte.R;
import com.example.beatbyte.adaptador.AdaptadorInicio;
import com.example.beatbyte.modelos.ModeloInicio;
import com.example.beatbyte.modelos.VideoYT;
import com.example.beatbyte.red.YoutubeAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class InicioFragment extends Fragment {

    private AdaptadorInicio adaptador;
    private LinearLayoutManager manager;
    private int currentItem, totalItem, scrollOutItem;
    private String nextPageToken = "";
    private boolean isScroll = false;
    private List<VideoYT> videoList = new ArrayList<>();

    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        // Inflate the layout for this fragment

        RecyclerView rv= view.findViewById(R.id.reciclador);
        adaptador =new AdaptadorInicio(getContext(),videoList);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setLayoutManager(manager);
        rv.setAdapter(adaptador);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = manager.getChildCount();
                totalItem = manager.getItemCount();
                scrollOutItem = manager.findFirstVisibleItemPosition();
                if (isScroll && (currentItem + scrollOutItem == totalItem)){
                    isScroll = false;
                    getJson();
                }
            }
        });
        if(videoList.size() == 0){
            getJson();
        }

        return view;
        //return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    private void getJson() {
        String url =YoutubeAPI.BASE_URL + YoutubeAPI.sch + YoutubeAPI.KEY + YoutubeAPI.part + YoutubeAPI.order
                + YoutubeAPI.mxresults + YoutubeAPI.chid;

        if (nextPageToken != ""){
            url = url + YoutubeAPI.NPT + nextPageToken;

        }
        if (nextPageToken == null){
            return;
        }
        Call<ModeloInicio> data = YoutubeAPI.getVideoInicio().getYT(url);
        data.enqueue(new Callback<ModeloInicio>() {
            @Override
            public void onResponse(Call<ModeloInicio> call, Response<ModeloInicio> response) {
                if(response.errorBody() != null){
                    Log.d(TAG, "onResponse: " + response.errorBody());
                }else{
                    ModeloInicio mi = response.body();
                    videoList.addAll(mi.getItems());
                    adaptador.notifyDataSetChanged();
                    if (mi.getNextPageToken() != null){
                        nextPageToken = mi.getNextPageToken();
                    }
                }


            }

            @Override
            public void onFailure(Call<ModeloInicio> call, Throwable t) {
                Log.d(TAG, "onFailure: ", t);
            }
        });

    }
}