package com.esotericcoder.findmycar.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esotericcoder.findmycar.R;
import com.esotericcoder.findmycar.api.ApiService;
import com.esotericcoder.findmycar.edit.EditLocationDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private Context context;
    private Locations locations;
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

    public LocationsAdapter(Context context) {
        this.context = context;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView dateView;
        ImageView navigateView;
        ImageView editView;
        ImageView deleteView;

        public LocationViewHolder(View itemView) {
            super(itemView);

            dateView = (TextView) itemView.findViewById(R.id.item_date);
            navigateView = (ImageView) itemView.findViewById(R.id.navigate);
            editView = (ImageView) itemView.findViewById(R.id.edit);
            deleteView = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        LocationViewHolder viewHolder = new LocationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        if (locations != null) {
            final Item item = locations.getItems().get(position);

            Date date = new Date(item.getDatetime());
            holder.dateView.setText(sdf.format(date));

            holder.navigateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMap(context, item.getLongitude(), item.getLatitude());
                }
            });

            holder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditLocationDialogFragment fragment = EditLocationDialogFragment.newInstance(item);
                    fragment.show(((FragmentActivity) context).getSupportFragmentManager(), "Dialog");
                }
            });

            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Retrofit retrofit = ApiService.getRetrofit();
                    DeleteLocationService service = retrofit.create(DeleteLocationService.class);
                    Call<Locations> call = service.deleteLocation(item.email, item.getDatetime());

                    call.enqueue(new Callback<Locations>() {
                        @Override
                        public void onResponse(Call<Locations> call, Response<Locations> response) {
                            updateLocations(response.body());
                        }

                        @Override
                        public void onFailure(Call<Locations> call, Throwable t) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return locations == null ? 0 : locations.getItems().size();
    }

    public void updateLocations(Locations locations) {
        if (locations != null) {
            this.locations = locations;
            notifyDataSetChanged();
        }
    }

    private static void showMap(Context context, double longitude, double latitude){
        String coordinate = "google.navigation:q=" + Double.toString(latitude) + ", " +
                Double.toString(longitude) + "&mode=w";
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse(coordinate);

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        context.startActivity(mapIntent);
    }
}
