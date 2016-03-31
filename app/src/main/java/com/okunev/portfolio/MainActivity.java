package com.okunev.portfolio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Button spotify = (Button)findViewById(R.id.spotifyButton);
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Spotify.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v){
        Toast.makeText(MainActivity.this, "This button will launch my "+
                ((Button)v).getText().toString(),Toast.LENGTH_LONG).show();
    }

    public void onClick2(View v){
        Toast.makeText(MainActivity.this, "This button will launch my "+
                ((Button)v).getText().toString()+" APP",Toast.LENGTH_LONG).show();
    }

    public void onClick3(View v){
        Toast.makeText(MainActivity.this, "This button will launch my CAPSTONE APP",Toast.LENGTH_LONG).show();
    }
}
