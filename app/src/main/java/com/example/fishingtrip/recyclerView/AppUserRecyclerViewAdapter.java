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
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.AppUser;

import java.util.List;

public class AppUserRecyclerViewAdapter extends RecyclerView.Adapter<AppUserRecyclerViewAdapter.ViewHolder> {

    List<AppUser> listOfData;
    Context context;

    public AppUserRecyclerViewAdapter(Context ct, List<AppUser> titles){
        listOfData = titles;
        context = ct;
    }

    @NonNull
    @Override
    public AppUserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.app_user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppUserRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.title.setText(listOfData.get(position).getUserName());

        holder.image.setImageResource(R.drawable.ic_action_menu_user);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked on: " + listOfData.get(position), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    /**
     * Update AppUser
     * @param position - Update the selected user.
     */
    public void updateAppUser(int position) {
        // Update user...

    }

    /**
     * Delete AppUser
     * @param position - Delete selected User on position in List<AppUser>/>.
     */
    public void deleteAppUser(int position) {
        DBHelper dbHelper = new DBHelper(context);
        boolean status = dbHelper.deleteUser(listOfData.get(position));
        if (status){
            listOfData.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "User was NOT Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView title;
        private ImageView image;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txtAppUserRow);
            image = itemView.findViewById(R.id.imageAppUser);
            constraintLayout = itemView.findViewById(R.id.constRowAppUser);
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
