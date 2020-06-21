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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.activities.ProfileActivity;
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
        holder.userName.setText("Username: " + listOfData.get(position).getUserName());
        holder.fullName.setText("Name: " + listOfData.get(position).getFirstName() + " " + listOfData.get(position).getLastName());
        holder.email.setText(listOfData.get(position).getEmail());
        holder.image.setImageResource(R.drawable.ic_action_menu_user);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // List of Users will only be displayed to ADMIN and by click on user -> users profile activity will be showed.
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
    public void updateAppUser(int position, AlertDialog.Builder dialogBuilder, LayoutInflater layoutInflater) {
        final DBHelper dbHelper = new DBHelper(context);
        final View dialogView = layoutInflater.inflate(R.layout.app_user_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText userName, password, firstName, lastName, email;
        Button btnConfirmUpdateUser;

        userName = dialogView.findViewById(R.id.inputUpdateUserName);
        password = dialogView.findViewById(R.id.inputUpdatePassword);
        firstName = dialogView.findViewById(R.id.inputUpdateFirstName);
        lastName = dialogView.findViewById(R.id.inputUpdateLastName);
        email = dialogView.findViewById(R.id.inputUpdateEmail);
        btnConfirmUpdateUser = dialogView.findViewById(R.id.btnConfirmUpdateUser);

        final AppUser tempUser = listOfData.get(position);

        userName.setText(tempUser.getUserName());
        password.setText(tempUser.getPassword());
        firstName.setText(tempUser.getFirstName());
        lastName.setText(tempUser.getLastName());
        email.setText(tempUser.getEmail());

        btnConfirmUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempUser.setUserName(userName.getText().toString());
                tempUser.setPassword(password.getText().toString());
                tempUser.setFirstName(firstName.getText().toString());
                tempUser.setLastName(lastName.getText().toString());
                tempUser.setEmail(email.getText().toString());

                dbHelper.updateUser(tempUser);
                Intent profileActivity = new Intent(context, ProfileActivity.class);
                context.startActivity(profileActivity);
                notifyDataSetChanged();
            }
        });
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

        private TextView userName, fullName, email;
        private ImageView image;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txtAppUserRow);
            fullName = itemView.findViewById(R.id.txtAppUserNameRow);
            email = itemView.findViewById(R.id.txtAppUserEmailRow);
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
