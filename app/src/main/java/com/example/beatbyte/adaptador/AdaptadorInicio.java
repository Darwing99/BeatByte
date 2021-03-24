package com.example.beatbyte.adaptador;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beatbyte.R;
import com.example.beatbyte.modelos.VideoYT;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AdaptadorInicio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<VideoYT> videoList;


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public AdaptadorInicio(Context context, List<VideoYT> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView txtTitulo;
        TextView txtFecha;


        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail=itemView.findViewById(R.id.thumbnailq);
            txtTitulo=itemView.findViewById(R.id.txtTitulo);
            txtFecha=itemView.findViewById(R.id.txtFecha);


        }


        public void setData(VideoYT data) {
            String getTitulo = data.getSnippet().getTitle();
            String getFecha = data.getSnippet().getPublishedAt();
            String getThumb = data.getSnippet().getThumbnails().getMedium().getUrl();
            txtFecha.setText(getFecha);
            txtTitulo.setText(getTitulo);
            Picasso.get()
                    .load(getThumb)
                    .placeholder(R.mipmap.loader)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: Correcto");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "onSuccess: Algo salio mal :c", e);
                        }
                    });
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View vista = inflater.inflate(R.layout.lista_inicio, parent, false);
        return new YoutubeHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoYT videoYT = videoList.get(position);
        YoutubeHolder yth = (YoutubeHolder) holder;

        yth.setData(videoYT);
    }


}
