package com.esotericcoder.findmycar.login;

import com.esotericcoder.findmycar.api.ApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST(ApiService.LOGIN)
    Call<JsonObject> createRequest(@Body User user);
}
