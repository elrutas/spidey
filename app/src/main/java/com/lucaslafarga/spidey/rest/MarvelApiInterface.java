package com.lucaslafarga.spidey.rest;

import com.lucaslafarga.spidey.models.GetAllComicsResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MarvelApiInterface {

    @GET("/v1/public/characters/{characterId}/comics")
    Observable<GetAllComicsResponse> getMarvelComicsData (
            @Path("characterId") String characterId,
            @Query("ts") String timestamp,
            @Query("apikey") String api_key,
            @Query("hash") String md5Hash);
}
