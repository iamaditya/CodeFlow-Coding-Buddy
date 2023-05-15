package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class finalPageOutput extends AppCompatActivity {
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalpageoutput);

        Intent intent = getIntent();
        String receivedString = intent.getStringExtra("myString");
        ArrayList<String> matchingUsers = intent.getStringArrayListExtra("matchingUsers");


        lv = findViewById(R.id.listViewaditya);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matchingUsers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Set the background color of the current item
                view.setBackgroundColor(getResources().getColor(android.R.color.white));

                // Add a separator line before the first entry
                if (position == 0) {

                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTypeface(null, Typeface.BOLD);

                    View separator = new View(getContext());
                    separator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                    separator.setBackgroundColor(getResources().getColor(android.R.color.black));

                    LinearLayout container = new LinearLayout(getContext());
                    container.setOrientation(LinearLayout.VERTICAL);
                    container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    container.addView(separator);
                    container.addView(view);

                    return container;
                }

                // Hide the view for all items except the first item in each group of three
                if (position % 3 != 0) {
                    View separator = new View(getContext());
                    separator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                    separator.setBackgroundColor(getResources().getColor(android.R.color.black));
                    LinearLayout container = new LinearLayout(getContext());
                    container.setOrientation(LinearLayout.VERTICAL);
                    container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    container.addView(separator);
                    container.addView(view);
                    view.setVisibility(View.GONE);
                    return container;

                } else {

                    // Make the first item in each group of three bold
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTypeface(null, Typeface.BOLD);
                }

                return view;
            }
        };


        lv.setDividerHeight(0);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Get the two additional positions of data from the matchingUsers list
                String data1 = matchingUsers.get(position + 1);
                String data2 = matchingUsers.get(position + 2);

                // Create an intent to start the new activity
                Intent intent = new Intent(finalPageOutput.this, MyNewActivity.class);

                // Pass the selected item and additional data to the new activity using the intent
                intent.putExtra("selectedItem", selectedItem);
                intent.putExtra("data1", data1);
                intent.putExtra("data2", data2);

                // Start the new activity
                startActivity(intent);
            }
        });


    }
    
    public void takemetoskills(View v){
//        Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(finalPageOutput.this, PojectDis.class);
        startActivity(i);
    }

}