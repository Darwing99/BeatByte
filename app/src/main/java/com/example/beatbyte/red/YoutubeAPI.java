package com.example.beatbyte.red;


import com.example.beatbyte.modelos.ModeloInicio;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class YoutubeAPI {


    //BASE URL VIDEOS FILTRADOS POR MAS POPULARES DE HONDURAS

    //----------------------------------------------------------------------------------

    public static final String BASE_URL_P = "https://www.googleapis.com/youtube/v3/";
    public static final String vidP = "videos?=";
    public static final String partP = "$&part=id, snippet,statistics, status";
    public static final String chartP = "&chart=mostPopular";
    public static final String regionCodeP = "&regionCode=HN";
    public static final String catIdP = "&videoCategoryId=10";

    //----------------------------------------------------------------------------------

    //MOSTRAR INICIO ESTATICO

    //----------------------------------------------------------------------------------
    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    public static final String KEY= "key=AIzaSyCe4T7jI617stWae7hyxAmALQVV3wp41A0";
    public static final String CHANNEL_ID = "UCNYW2vfGrUE6R5mIJYzkRyQ";
    public static final String sch = "search?";
    public static final String part= "&part=snippet";
    public static final String order= "&order=relevance";
    public static final String mxresults="&maxResults=20";
    public static final String chid="&channelId=" + CHANNEL_ID;
    public static final String catId="&videoCategoryId=10";
    public static final String NPT = "&pageToken=";
    public static final String K = "&regionCode=US";
    //----------------------------------------------------------------------------------

    //CONSULTAS PERSONLIZADAS

    //----------------------------------------------------------------------------------
    public static final String query="&q=";
    public static final String type="&type=video";
    //----------------------------------------------------------------------------------

    public interface InicioVideos{
        @GET
        Call<ModeloInicio> getYT(@Url String url);
    }

    public static InicioVideos videoInicio= null;

    public static InicioVideos getVideoInicio(){
        if(videoInicio==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            videoInicio =retrofit.create(InicioVideos.class);

        }
        return videoInicio;
    }

}