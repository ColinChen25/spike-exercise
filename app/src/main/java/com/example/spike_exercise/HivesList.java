package com.example.spike_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HivesList extends AppCompatActivity {
    Button addButton;
    String current_username;
    RecyclerView recyclerView;
    HivesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_hive_list);
        current_username = getIntent().getStringExtra("current_username");
        addButton = findViewById(R.id.add_hive);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nagivation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navHives:
                        break;
                    case R.id.navSearch:
                        Toast.makeText(HivesList.this, "navSearch selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navProfile:
                        Toast.makeText(HivesList.this, "navProfile selected", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HivesList.this, UserProfile.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        String whereClause = "username = '" + ApplicationClass.user.getProperty("username") + "'";

        DataQueryBuilder query = DataQueryBuilder.create();
        query.setWhereClause(whereClause);
        query.setGroupBy("created");

        recyclerView = findViewById(R.id.hive_list_recycler_view);



        Backendless.Persistence.of(Hives.class).find(query, new AsyncCallback<List<Hives>>() {
            @Override
            public void handleResponse(List<Hives> response) {

                ApplicationClass.hives = response;
                adapter = new HivesAdapter(HivesList.this, response);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(HivesList.this));

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent (HivesList.this, HiveAdd.class);
                intent.putExtra("current_username", current_username);
                startActivity(intent);
            }
        });
    }
}