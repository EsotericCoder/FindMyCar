package com.esotericcoder.findmycar.main;

import com.esotericcoder.findmycar.api.ApiService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetLocationService {
    @GET(ApiService.GET_LOCATIONS)
    Call<Locations> getLocations(@Query("email") String email);
}
