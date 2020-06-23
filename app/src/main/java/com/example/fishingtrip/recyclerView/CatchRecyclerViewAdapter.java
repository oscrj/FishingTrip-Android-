package com.example.fishingtrip.recyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.Catch;

import java.util.List;

public class CatchRecyclerViewAdapter extends RecyclerView.Adapter<CatchRecyclerViewAdapter.ViewHolder> {

    List<Catch> listOfData;
    Context context;
    private LayoutInflater layoutInflater;

    public CatchRecyclerViewAdapter(Context ct, List<Catch> titles) {
        this.listOfData = titles;
        this.context = ct;
    }
    
    /**
     * Needed LayoutInflater to create custom Dialog. quick fix and not a great solution. Don't have time to do find
     * a better solution today...
     * @param inflater - i get from the activity im in. (context)
     */
    public void setLayoutInflater(LayoutInflater inflater){
        this.layoutInflater = inflater;
    }

    @NonNull
    @Override
    public CatchRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.catch_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CatchRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.species.setText(listOfData.get(position).getSpecies());
        holder.length.setText(listOfData.get(position).getLength() + " cm");
        holder.weight.setText(listOfData.get(position).getWeight() + " kg");
        holder.image.setImageResource(R.drawable.ic_catch_logo);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                View dialogView = layoutInflater.inflate(R.layout.catch_details_dialog, null);
                alertBuilder.setView(dialogView);

                TextView species, length, weight, description;
                ImageView caughtFish;

                species = dialogView.findViewById(R.id.catchDialogTxtSpecies);
                length = dialogView.findViewById(R.id.catchDialogTxtLength);
                weight = dialogView.findViewById(R.id.catchDialogTxtWeight);
                description = dialogView.findViewById(R.id.catchDialogTxtDescription);
                caughtFish = dialogView.findViewById(R.id.catchDialogImage);

                species.setText(listOfData.get(position).getSpecies());
                length.setText(listOfData.get(position).getLength() + " cm");
                weight.setText(listOfData.get(position).getWeight() + " kg");
                description.setText("Description.....");

                AlertDialog addCatchDetails = alertBuilder.create();
                addCatchDetails.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    /**
     * Delete catch
     * @param position - Delete selected catch on position in List<Catch>.
     */
    public void deleteFishCaught(int position) {
        DBHelper dbHelper = new DBHelper(context);
        boolean status = dbHelper.deleteCatch(listOfData.get(position));
        if (status){
            listOfData.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Caught fish was NOT removed!", Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView species, length, weight;
        private ImageView image;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            species = itemView.findViewById(R.id.txtSpeciesRow);
            length = itemView.findViewById(R.id.txtLengthRow);
            weight = itemView.findViewById(R.id.txtWeightRow);
            image = itemView.findViewById(R.id.imgCatchRow);
            constraintLayout = itemView.findViewById(R.id.constRowCatch);
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
