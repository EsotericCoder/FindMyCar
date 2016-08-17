package com.esotericcoder.findmycar.main;

import com.esotericcoder.findmycar.api.ApiService;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface DeleteLocationService {
    @DELETE(ApiService.DELETE_LOCATION)
    Call<Locations> deleteLocation(@Query("email") String email, @Query("datetime") long datetime);
}
