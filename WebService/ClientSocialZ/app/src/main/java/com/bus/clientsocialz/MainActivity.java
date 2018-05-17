package com.bus.clientsocialz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity  extends AppCompatActivity {

    String hobby;
    Button btn;
    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.cercaPerHobby);
        txt = findViewById(R.id.inserisciHobby);
        btn.setOnClickListener(View -> {
            hobby = txt.getText().toString();
            String error = "L'hobby selezionato è inesistente";
            Intent i = new Intent(this, ListaPraticanti.class);
            if (!hobby.equals("") || !hobby.isEmpty()) {
                i.putExtra("Hobby", hobby);
            } else {
                i.putExtra("Error", error);
            }
            startActivity(i);
            finish();
        });

    }
}

