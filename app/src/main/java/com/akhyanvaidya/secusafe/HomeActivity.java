package com.akhyanvaidya.secusafe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class HomeActivity extends AppCompatActivity {


    TextView tvHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvHomepage = (TextView) findViewById(R.id.tvWelcome);
        String message = getIntent().getStringExtra("message");
        tvHomepage.setText(message);

    }
}
