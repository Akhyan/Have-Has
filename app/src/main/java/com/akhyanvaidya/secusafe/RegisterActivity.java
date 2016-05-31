package com.akhyanvaidya.secusafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    AlertDialog.Builder regBuild;
    EditText rName, rUserName, rPass, rABN, rPhone, rEmail;
    Button rRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        rName= (EditText) findViewById(R.id.etName);
        rUserName=(EditText) findViewById(R.id.etUserName);
        rPass=(EditText) findViewById(R.id.etPassword);
        rABN=(EditText) findViewById(R.id.etAbn);
        rPhone=(EditText) findViewById(R.id.etPhone);
        rEmail=(EditText) findViewById(R.id.etEmail);
        rRegister=(Button) findViewById(R.id.bRegister);


        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rName.getText().toString().equals("")||rUserName.getText().toString().equals("")||
                        rPass.getText().toString().equals("")|| rEmail.getText().toString().equals("")
                        || rPhone.getText().toString().equals("")|| rABN.getText().toString().equals("")){

                    rPass.setText("");
                    Toast.makeText(RegisterActivity.this, "Some fields are/is missing", Toast.LENGTH_SHORT).show();

                }
                else{
                    RegisterBackground registerBackground=new RegisterBackground(RegisterActivity.this);
                    registerBackground.execute("register", rName.getText().toString(),rEmail.getText().toString(),
                                                rUserName.getText().toString(), rPass.getText().toString(), rPhone.getText().toString()
                            , rABN.getText().toString());
                }
            }

        });

    }
}
