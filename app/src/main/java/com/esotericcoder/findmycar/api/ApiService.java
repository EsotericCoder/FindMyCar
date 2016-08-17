package com.esotericcoder.findmycar.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static final String BASE_URL = "http://52.34.186.228:3000";
    public static final String SIGNUP = "/signup";
    public static final String LOGIN = "/login";
    public static final String GET_LOCATIONS = "/getLocations";
    public static final String ADD_LOCATION = "/addLocation";
    public static final String DELETE_LOCATION = "/deleteLocation";
    public static final String EDIT_LOCATION = "/updateLocation";

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
