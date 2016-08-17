package com.esotericcoder.findmycar.main;

import com.esotericcoder.findmycar.api.ApiService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddLocationService {
    @POST(ApiService.ADD_LOCATION)
    Call<Locations> addLocation(@Body Item item);
}
