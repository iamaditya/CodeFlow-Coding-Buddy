package com.example.app1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PojectDis extends AppCompatActivity {
    EditText pn,ps;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poject_dis);
        Intent intent = getIntent();
        username = intent.getStringExtra("user_mail");
    }

    public void nextpage(View view){

        pn = findViewById(R.id.editTextTextEmailAddress);
        ps = findViewById(R.id.editTextText2);

        String project_Name = pn.getText().toString();
        String project_skills = ps.getText().toString();

        if (project_Name.isEmpty() || project_skills.isEmpty()) {
//            Toast.makeText(this, "Please enter project name and project skills", Toast.LENGTH_SHORT).show();
            showErrorDialog("Enter Details");
            return;
        }

        // Split the project skills string into an array of individual skills
        String[] skillsArray = project_skills.split(",");

        // Create an ArrayList from the skills array
        ArrayList<String> skillsList = new ArrayList<>(Arrays.asList(skillsArray));

        // Get the matching users from the database using the DBHelper class
        DBHelper dbHelper = new DBHelper(this);
        List<String> matchingUsers = dbHelper.getMatchingUsers(project_Name,skillsList,username);

        if (matchingUsers.isEmpty()) {
            Toast.makeText(this, "No matching users found", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(PojectDis.this, finalPageOutput.class);
        intent.putStringArrayListExtra("matchingUsers", (ArrayList<String>) matchingUsers);
        intent.putExtra("mystring",project_skills);
        startActivity(intent);
//        Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
    }






    public void loginme(View view){
        Intent in = new Intent(PojectDis.this, login.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }


    public void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage)
                .setTitle("Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}



