package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    ".{8,}" +               //at least 8 characters
                    "$");
    DBHelper Dbx;
    EditText uname,umail,upass,uskills,ucontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }

    public void registerme(View view) {

        Dbx = new DBHelper(this);

        uname = findViewById(R.id.editTextText);
        umail = findViewById(R.id.editTextTextEmailAddress2);
        upass = findViewById(R.id.editTextTextPassword);
        uskills = findViewById(R.id.editTextTextPersonName3);
        ucontact = findViewById(R.id.editTextTextPersonName4);

        String xname = uname.getText().toString();
        String xemail = umail.getText().toString();
        String xpass = upass.getText().toString();



        String xcontact = ucontact.getText().toString();



        String xpassword = stringToHash(xpass); //string to hash


        String xskillsStr = uskills.getText().toString().trim();

        List<String> xskills = new ArrayList<>(Arrays.asList(xskillsStr.split(",")));

        StringBuilder errorMessage = new StringBuilder();
        if (xemail.isEmpty()) {
            umail.setError("Field can't be empty");
            errorMessage.append("Email field is empty.\n");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(xemail).matches()) {
            umail.setError("Please enter a valid email address");
            errorMessage.append("Invalid email address.\n");
        }

        if (xpass.isEmpty()) {
            upass.setError("Field can't be empty");
            errorMessage.append("Password field is empty.\n");
        } else if (!PASSWORD_PATTERN.matcher(xpass).matches()) {
            upass.setError("Password too weak");
            errorMessage.append("Password too weak.\n");
        }

        if (xname.equals("") || xemail.equals("") || xpassword.equals("") || xskills.equals("")) {
            Toast.makeText(this, "We don't accept NULL input", Toast.LENGTH_SHORT).show();
            errorMessage.append("Some fields are empty.\n");
        }

        if(xcontact.length()<10){
            ucontact.setError("Enter a valid Number");
        }

        if (errorMessage.length() > 0) {
            return;
        }else {
            boolean b = Dbx.insertData(xname, xemail, xpassword,xskills,xcontact);
            if (b == true) {
                // Insert user's skills into the database
                for (String skill : xskills) {
                    boolean s = Dbx.insertUserSkills(xemail, skill);
                    if (s == false) {
                        Toast.makeText(this, "Found some error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(this, "Sucessfully Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);

            }else{
                Toast.makeText(this, "Email Already Registered", Toast.LENGTH_SHORT).show();
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
}