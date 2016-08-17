package com.esotericcoder.findmycar.edit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.esotericcoder.findmycar.R;
import com.esotericcoder.findmycar.api.ApiService;
import com.esotericcoder.findmycar.main.Item;
import com.esotericcoder.findmycar.main.Locations;
import com.esotericcoder.findmycar.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditLocationDialogFragment extends DialogFragment {

    private static final String KEY_ITEM = "KEY_ITEM";

    private EditText editLatitude;
    private EditText editLongitude;
    private TextView editAction;

    private Item updatedItem;

    public static EditLocationDialogFragment newInstance (Item item) {
        EditLocationDialogFragment fragment = new EditLocationDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(KEY_ITEM, item);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit, container, false);

        updatedItem = getArguments().getParcelable(KEY_ITEM);

        editLatitude = (EditText) v.findViewById(R.id.edit_latitude);
        editLongitude = (EditText) v.findViewById(R.id.edit_longitude);
        editAction = (TextView) v.findViewById(R.id.edit_action);

        editAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editLatitude.getText().toString().isEmpty()) {
                    updatedItem.setLatitude(Double.parseDouble(editLatitude.getText().toString()));
                }

                if (!editLongitude.getText().toString().isEmpty()) {
                    updatedItem.setLongitude(Double.parseDouble(editLongitude.getText().toString()));
                }

                Retrofit retrofit = ApiService.getRetrofit();
                EditLocationService service = retrofit.create(EditLocationService.class);
                Call<Locations> call = service.editLocation(updatedItem);

                call.enqueue(new Callback<Locations>() {
                    @Override
                    public void onResponse(Call<Locations> call, Response<Locations> response) {
                        MainActivity callingActivity = (MainActivity) getActivity();
                        callingActivity.onEditLocation(response.body());
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<Locations> call, Throwable t) {

                    }
                });
            }
        });

        return v;
    }
}
