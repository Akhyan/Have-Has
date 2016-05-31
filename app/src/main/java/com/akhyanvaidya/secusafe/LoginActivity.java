package com.akhyanvaidya.secusafe;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    EditText lUname, lPassword;
    Button bLogin;
    TextView tRegister;
    CheckBox cbRemember;
    SharedPreferences shareLogin, shareRemember, shareCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        lUname=(EditText) findViewById(R.id.elUsername);
        lPassword= (EditText) findViewById(R.id.elPassword);
        bLogin=(Button) findViewById(R.id.bLogin);
        tRegister=(TextView) findViewById(R.id.tRegister);
        cbRemember=(CheckBox) findViewById(R.id.cbRemember);
        LoadRemember(); //check if remember was checked


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveRemember("Remember_Me", cbRemember.isChecked());
                if (cbRemember.isChecked()) {

                    SaveLogin(lUname.getText().toString(), lPassword.getText().toString());//save login information
                    LoadLoginBackground();

                } else if (lUname.getText().toString().equals("") || lPassword.getText().toString().equals("")) {

                    lUname.setText("");
                    lPassword.setText("");
                    Toast.makeText(LoginActivity.this, "No Username or Password", Toast.LENGTH_SHORT).show();
                }else {

                    LoadLoginBackground();

                    }
                }
            }

            );

            //when tRegsiter is clicked this method will run.
            tRegister.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);//Goes from Login page to Register page
                LoginActivity.this.startActivity(registerIntent);//starts the register.Intent function
            }
        });

    }


    private void SaveLogin(String valueUser, String valuePass){//save username and password
        shareLogin = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor= shareLogin.edit();
        editor.putString("Username", valueUser);
        editor.putString("Password", valuePass);
        editor.commit();
    }

    private void SaveRemember(String key, Boolean value){
        shareRemember= PreferenceManager.getDefaultSharedPreferences(this);
        Editor editorRem= shareRemember.edit();
        editorRem.putBoolean(key, value);
        editorRem.commit();
    }

    private void LoadRemember(){
        shareCheck= PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbRem= shareCheck.getBoolean("Checkbox", false);
        String name= shareCheck.getString("Username", "");
        String pass= shareCheck.getString("Password", "");
        if(cbRem){
            cbRemember.setChecked(true);
        }else{
            cbRemember.setChecked(false);
        }
        lUname.setText(name);
        lPassword.setText(pass);
    }

    private void LoadLoginBackground(){

        LoginBackground loginBackground=new LoginBackground(LoginActivity.this);
        loginBackground.execute("login", lUname.getText().toString(),lPassword.getText().toString());

    }
}
