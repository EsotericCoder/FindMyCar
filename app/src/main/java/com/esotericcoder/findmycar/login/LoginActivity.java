package com.esotericcoder.findmycar.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esotericcoder.findmycar.R;
import com.esotericcoder.findmycar.api.ApiService;
import com.esotericcoder.findmycar.main.MainActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout loginContainer;
    private EditText loginEmail;
    private EditText loginPassword;
    private TextView loginAction;
    private TextView signupToggle;

    private LinearLayout signupContainer;
    private EditText signupEmail;
    private EditText signupPassword;
    private TextView signupAction;
    private TextView loginToggle;

    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofit = ApiService.getRetrofit();
        initializeViews();
    }

    private void initializeViews() {
        loginContainer = (LinearLayout) findViewById(R.id.login_container);
        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginAction = (TextView) findViewById(R.id.log_in_action);
        signupToggle = (TextView) findViewById(R.id.sign_up_toggle);

        signupContainer = (LinearLayout) findViewById(R.id.sign_up_container);
        signupEmail = (EditText) findViewById(R.id.sign_up_email);
        signupPassword = (EditText) findViewById(R.id.sign_up_password);
        signupAction = (TextView) findViewById(R.id.sign_up_action);
        loginToggle = (TextView) findViewById(R.id.log_in_toggle);

        signupToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginContainer.setVisibility(View.GONE);
                signupContainer.setVisibility(View.VISIBLE);
            }
        });

        loginToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleToLogin();
            }
        });

        signupAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(signupEmail.getText().toString(), signupPassword.getText().toString());
                SignUpService service = retrofit.create(SignUpService.class);
                Call<Void> call = service.createRequest(user);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Account successfully created!", Toast.LENGTH_SHORT).show();
                            toggleToLogin();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Account cannot be created.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(loginEmail.getText().toString(), loginPassword.getText().toString());
                LoginService service = retrofit.create(LoginService.class);
                Call<JsonObject> call = service.createRequest(user);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            JSONObject object;
                            try {
                                object = new JSONObject(response.body().toString());
                                startActivity(MainActivity.createIntent(LoginActivity.this, object.getString("email")));
                            } catch (JSONException e) {
                                Log.e("JSONException", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Cannot login.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void toggleToLogin() {
        signupContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.VISIBLE);
    }
}
