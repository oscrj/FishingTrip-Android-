package com.example.fishingtrip.recyclerView;

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
import com.example.fishingtrip.models.Catch;

import java.util.List;

public class CatchRecyclerViewAdapter extends RecyclerView.Adapter<CatchRecyclerViewAdapter.ViewHolder> {

    List<Catch> listOfData;
    Context context;

    public CatchRecyclerViewAdapter(Context ct, List<Catch> titles) {
        this.listOfData = titles;
        this.context = ct;
    }

    @NonNull
    @Override
    public CatchRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.catch_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatchRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.species.setText(listOfData.get(position).getSpecies());
        holder.length.setText(String.valueOf(listOfData.get(position).getLength() + " cm"));
        holder.weight.setText(String.valueOf(listOfData.get(position).getWeight() + " kg"));
        holder.image.setImageResource(R.drawable.ic_catch_logo);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listOfData.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    /**
     *
     * @param position
     */
    public void deleteFishCaught(int position) {
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
