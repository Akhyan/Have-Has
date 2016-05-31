package com.akhyanvaidya.secusafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class RegisterBackground extends AsyncTask<String, Void, String> {
    String register_url="http://10.0.2.2/havehas/register.php";
    String login_url="http://10.0.2.2/havehas/login.php";

    Context ctx;
    Activity activity;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;


    public RegisterBackground(Context ctx){            //constructor
        this.ctx=ctx;
        activity=(Activity) ctx; //Convert to the same datatype
    }

    protected void onPreExecute(){
        builder = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting you to server ....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override//Take a register event
    protected String doInBackground(String... params){
        String method=params[0];
        if (method.equals("register")){
            try {
                URL url=new URL(register_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String name=params[1];
                    String email=params[2];
                    String userName=params[3];
                    String password=params[4];
                    String phone=params[5];
                    String abNumber=params[6];
                String data= URLEncoder.encode("name", "UTF-8")+"="+ URLEncoder.encode(name,"UTF-8")+"&"+
                             URLEncoder.encode("email", "UTF-8")+"="+ URLEncoder.encode(email,"UTF-8")+"&"+
                             URLEncoder.encode("userName", "UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")+"&"+
                             URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8")+"&"+
                             URLEncoder.encode("phone", "UTF-8")+"="+ URLEncoder.encode(phone,"UTF-8")+"&"+
                             URLEncoder.encode("abNumber", "UTF-8")+"="+ URLEncoder.encode(abNumber,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                String line = "";
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }

                httpURLConnection.disconnect();
                Thread.sleep(5000);

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(method.equals("login")){
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String userName = params[1];
                String password = params[2];
                String data= URLEncoder.encode("userName", "UTF-8")+"="+ URLEncoder.encode(userName,"UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8")+"="+ URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder= new StringBuilder();
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line+"\n");
                }

                httpURLConnection.disconnect();
                Thread.sleep(5000);
                Log.d("Test", "Test 3 pass");

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            progressDialog.dismiss();
            JSONObject jsonObject= new JSONObject(json);
            JSONArray jsonArray= jsonObject.getJSONArray("server_response");
            JSONObject JO = jsonArray.getJSONObject(0);
            String code = JO.getString("code");
            String message = JO.getString("message");
            if (code.equals("reg_true")) {
                showDialogue("Registration Successful", message, code);
            } else if (code.equals("reg_false"))
            {
                showDialogue("Registration failed!! ", message, code);
            }else if(code.equals("login_true")){
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.putExtra("message", message);
                activity.startActivity(intent);
            }else if(code.equals("login_false")){
                showDialogue("Login failed!! ", message, code);
            }
        } catch (JSONException e) {
            Log.d("Whats wrong?", json.toString());
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

    }

    public void showDialogue(String title, String message, String code) {
        builder.setTitle(title);

        if (code.equals("reg_true") || code.equals("reg_false")) {

            builder.setMessage(message);
            builder.setPositiveButton("Goto Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if(code.equals("login_false")){
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    EditText userName, password;
                    userName = (EditText) activity.findViewById(R.id.elUsername);
                    password = (EditText) activity.findViewById(R.id.elPassword);

                    userName.setText("");
                    password.setText("");

                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
