package com.example.app1;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "project_db";

    public DBHelper(Context context) {
        super(context, "project_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("create table skills(Mail TEXT, Skill TEXT)");
        myDB.execSQL("create table users(Name TEXT, Mail TEXT PRIMARY KEY, Password TEXT, Contact TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1) {
        myDB.execSQL("DROP TABLE IF EXISTS users");
        onCreate(myDB);
    }

    public boolean insertData(String Name,String Mail, String Password, List<String> Skills, String Number){
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Name",Name);
        cv.put("Mail", Mail);
        cv.put("Password",Password);
        cv.put("Contact", Number);

        long result = myDb.insert("users",null,cv);

        if(result == -1){
            return false;
        }else{

            for (String skill : Skills) {
                ContentValues cv2 = new ContentValues();
                cv2.put("Mail", Mail);
                cv2.put("Skill", skill);
                long result2 = myDb.insert("skills", null, cv2);
                if(result2 == -1){
                    return false;
                }
            }
            return true;
        }
    }
    public boolean insertUserSkills(String email, String skill) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Mail", email);
        cv.put("Skill", skill);

        // Check if the skill already exists for the given email
        String[] selectionArgs = { email, skill };
        String query = "SELECT * FROM skills WHERE Mail = ? AND Skill = ?";
        Cursor cursor = myDb.rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0) {
            // Skill already exists, no need to insert
            cursor.close();
            return true;
        }

        // Insert the skill
        long result = myDb.insert("skills", null, cv);
        cursor.close();

        return result != -1;
    }

    public boolean loginuser(String un,String up){
        SQLiteDatabase myDb = this.getWritableDatabase();
        Cursor cursor = myDb.rawQuery("SELECT * FROM users WHERE Mail = ? AND Password = ?", new String[]{un,up});
        try {
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            cursor.close();
        }
    }

    public List<String> getMatchingUsers(String projectType, List<String> requiredSkills, String username) {
        SQLiteDatabase mydb = this.getReadableDatabase();
        List<String> matchingUsers = new ArrayList<>();

        String[] columns = {"Mail"};
        String selection = "Mail IN (SELECT DISTINCT Mail FROM skills WHERE Skill = ?)";
        for (int i = 1; i < requiredSkills.size(); i++) {
            selection += " AND Mail IN (SELECT DISTINCT Mail FROM skills WHERE Skill = ?)";
        }

        String[] selectionArgs = new String[requiredSkills.size()];
        for (int i = 0; i < requiredSkills.size(); i++) {
            selectionArgs[i] = requiredSkills.get(i);
        }

        Cursor cursor = mydb.query("skills", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String mail = cursor.getString(cursor.getColumnIndex("Mail"));
                Cursor cursor2 = mydb.rawQuery("SELECT * FROM users WHERE Mail = ? AND EXISTS (SELECT 1 FROM skills WHERE Mail = ? AND Skill = ?)", new String[]{mail, mail, requiredSkills.get(0)});
                if (cursor2.moveToFirst()) {
                    @SuppressLint("Range") String xname = cursor2.getString(cursor2.getColumnIndex("Name"));
                    @SuppressLint("Range") String xmail = cursor2.getString(cursor2.getColumnIndex("Mail"));
                    @SuppressLint("Range") String xcontact = cursor2.getString(cursor2.getColumnIndex("Contact"));

                    // Check if the current user's email matches the username, and skip if it does
                    if (xmail.equals(username)) {
                        continue;
                    }

                    // Retrieve all skills associated with this user
                    Cursor cursor3 = mydb.rawQuery("SELECT Skill FROM skills WHERE Mail = ?", new String[]{mail});
                    List<String> skillsList = new ArrayList<>();
                    if (cursor3.moveToFirst()) {
                        do {
                            @SuppressLint("Range") String skill = cursor3.getString(cursor3.getColumnIndex("Skill"));
                            skillsList.add(skill);
                        } while (cursor3.moveToNext());
                    }
                    cursor3.close();

                    // Add the user's name, email, contact, and skills to the list
                    if (!matchingUsers.contains(xname)) {
                        matchingUsers.add(xname);
                        matchingUsers.add(xmail);
                        matchingUsers.add(xcontact);
                        // matchingUsers.addAll(skillsList);
                    }
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        return matchingUsers;
    }

}