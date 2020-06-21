package com.example.fishingtrip.recyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.activities.FishingTripsActivity;
import com.example.fishingtrip.activities.NewTripActivity;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.FishingTrip;


import java.util.List;

public class FishingTripRecyclerViewAdapter extends RecyclerView.Adapter<FishingTripRecyclerViewAdapter.ViewHolder> {

    List<FishingTrip> listOfData;
    Context context;

    public FishingTripRecyclerViewAdapter(Context ct, List<FishingTrip> titles){

        listOfData = titles;
        context = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fishing_trips_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.method.setText(listOfData.get(position).getFishingMethod().toUpperCase());
        holder.waterType.setText(listOfData.get(position).getWaterType());
        holder.location.setText("-  " + listOfData.get(position).getLocation());
        holder.image.setImageResource(R.drawable.ic_action_menu_fishingtrip_logo);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tripActivity = new Intent(context, NewTripActivity.class);
                tripActivity.putExtra("FISHING_TRIP_LOCATION", listOfData.get(position).getLocation());
                tripActivity.putExtra("FISHING_TRIP_ID", listOfData.get(position).getFishingTripId());
                context.startActivity(tripActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    /**
     * Delete FishingTrip
     * @param position - Delete selected User on position in List<AppUser>.
     */
    public void deleteTrip(int position) {
        DBHelper dbHelper = new DBHelper(context);
        boolean status = dbHelper.deleteFishingTrip(listOfData.get(position));
        if (status){
            listOfData.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Fishing trip was NOT Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateFishingTrip(int position, AlertDialog.Builder dialogBuilder, LayoutInflater layoutInflater) {
        final DBHelper dbHelper = new DBHelper(context);
        View dialogView = layoutInflater.inflate(R.layout.fishing_trip_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText fishingMethod, waterType, location;
        final Switch isActive;
        Button btnConfirmTripUpdate;

        fishingMethod = dialogView.findViewById(R.id.inputUpdateMethod);
        waterType = dialogView.findViewById(R.id.inputUpdateWaterType);
        location = dialogView.findViewById(R.id.inputUpdateLocation);
        isActive = dialogView.findViewById(R.id.swUpdateTripIsActive);
        btnConfirmTripUpdate = dialogView.findViewById(R.id.btnUpdateFishingTrip);

        final FishingTrip tempTrip = listOfData.get(position);

        fishingMethod.setText(tempTrip.getFishingMethod());
        waterType.setText(tempTrip.getWaterType());
        location.setText(tempTrip.getLocation());
        isActive.setChecked(tempTrip.isActive());

        btnConfirmTripUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempTrip.setLocation(location.getText().toString());
                tempTrip.setFishingMethod(fishingMethod.getText().toString());
                tempTrip.setWaterType(waterType.getText().toString());
                tempTrip.setActive(isActive.isChecked());

                dbHelper.updateTrip(tempTrip);

                Intent fishingTrips = new Intent(context, FishingTripsActivity.class);
                context.startActivity(fishingTrips);
                notifyDataSetChanged();
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView method, waterType, location;
        private ImageView image;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            method = itemView.findViewById(R.id.txtMethodRow);
            waterType = itemView.findViewById(R.id.txtWaterTypeRow);
            location = itemView.findViewById(R.id.txtLocationRow);
            image = itemView.findViewById(R.id.imageFishingTripRow);
            constraintLayout = itemView.findViewById(R.id.constRowFishingTrips);
            constraintLayout.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selection:");
            menu.add(this.getAdapterPosition(), 100, 0, "Update");
            menu.add(this.getAdapterPosition(), 101, 1, "Delete");
        }
    }
}
