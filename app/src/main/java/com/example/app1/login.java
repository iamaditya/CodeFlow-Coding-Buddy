package com.example.app1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    ".{8,}" +               //at least 8 characters
                    "$");
    EditText usrn, pass;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DBHelper(this);
    }

    public void userRegistered(View view) {
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);
    }

    public void userLogin(View view) {

        usrn = findViewById(R.id.editTextTextEmailAddress2);
        pass = findViewById(R.id.editTextTextPassword);

        String username = usrn.getText().toString();
        String xpass = pass.getText().toString();


        String password = stringToHash(xpass);

//        String gotuser = db.getname(username, password);
//        Toast.makeText(this, "Got you", Toast.LENGTH_SHORT).show();
        StringBuilder errorMessage = new StringBuilder();
        if (username.isEmpty()) {
            usrn.setError("Field can't be empty");
            errorMessage.append("Email field is empty.\n");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usrn.setError("Please enter a valid email address");
            errorMessage.append("Invalid email address.\n");
        }

        if (xpass.isEmpty()) {
            pass.setError("Field can't be empty");
            errorMessage.append("Password field is empty.\n");
        } else if (!PASSWORD_PATTERN.matcher(xpass).matches()) {
            pass.setError("Incorrect Password");
            errorMessage.append("Password too weak.\n");
        }

        if (errorMessage.length() > 0) {
            return;
        }else {
            boolean check = db.loginuser(username, password);
            if (check == true) {

                    Intent intent = new Intent(login.this, PojectDis.class);
                    intent.putExtra("user_mail",username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

            } else {
                showErrorDialog("Incorrect Credentials");

            }

        }
    }

    public static String stringToHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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