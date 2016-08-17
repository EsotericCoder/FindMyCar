package com.esotericcoder.findmycar.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.esotericcoder.findmycar.R;
import com.esotericcoder.findmycar.api.ApiService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_LOCATION = 200;
    private static final String TAG = "MainActivity";
    private static final String KEY_EMAIL = "KEY_EMAIL";

    private String email;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng lastLocation;

    private RecyclerView recyclerView;
    private LocationsAdapter adapter;
    private TextView addLocation;

    public static Intent createIntent(Context context, String email) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = getIntent().getStringExtra(KEY_EMAIL);

        // Create an instance of GoogleAPIClient
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }

        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        addLocation = (TextView) findViewById(R.id.add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastLocation != null) {
                    Item item = new Item(System.currentTimeMillis(), lastLocation.latitude, lastLocation.longitude, email);
                    Retrofit retrofit = ApiService.getRetrofit();
                    AddLocationService service = retrofit.create(AddLocationService.class);
                    Call<Locations> call = service.addLocation(item);

                    call.enqueue(new Callback<Locations>() {
                        @Override
                        public void onResponse(Call<Locations> call, Response<Locations> response) {
                            if (adapter != null) {
                                adapter.updateLocations(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<Locations> call, Throwable t) {

                        }
                    });
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new LocationsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //Connect Google API Client when app starts
    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStoredLocations();
    }

    //Disconnect Google API Client when app is stopped
    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getCurrentLocation();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull  ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            //Initialize location variable
            Location mLastLocation;
            //Get last location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            //If the last location was found, get Longitude and Latitude then call the GetWeatherInfo
            if (mLastLocation != null) {
                lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
    }

    private void getStoredLocations() {
        Retrofit retrofit = ApiService.getRetrofit();
        GetLocationService service = retrofit.create(GetLocationService.class);
        Call<Locations> call = service.getLocations(email);

        call.enqueue(new Callback<Locations>() {
            @Override
            public void onResponse(Call<Locations> call, Response<Locations> response) {
                if (adapter != null) {
                    adapter.updateLocations(response.body());
                }
            }

            @Override
            public void onFailure(Call<Locations> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signout:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEditLocation(Locations locations) {
        adapter.updateLocations(locations);
    }
}
