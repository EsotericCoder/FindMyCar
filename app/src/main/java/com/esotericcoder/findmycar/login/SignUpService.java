package com.esotericcoder.findmycar.login;

import com.esotericcoder.findmycar.api.ApiService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpService {
    @POST(ApiService.SIGNUP)
    Call<Void> createRequest(@Body User user);
}
