package com.esotericcoder.findmycar.edit;

import com.esotericcoder.findmycar.api.ApiService;
import com.esotericcoder.findmycar.main.Item;
import com.esotericcoder.findmycar.main.Locations;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface EditLocationService {
    @PUT(ApiService.EDIT_LOCATION)
    Call<Locations> editLocation(@Body Item item);
}
