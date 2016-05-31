package com.akhyanvaidya.secusafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


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


public class LoginBackground extends AsyncTask<String, Void, String> {
    String login_url="http://192.168.2.5/havehas/login.php";

    Context ctx;
    Activity activity;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    EditText userNameClear, passwordClear;
    String loadUser, loadPass, sessionUser;

    SharedPreferences shareLogin, shareUser;





    public LoginBackground(Context ctx){            //constructor
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
        if(method.equals("login")){
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

            userNameClear = (EditText) activity.findViewById(R.id.elUsername);
            passwordClear = (EditText) activity.findViewById(R.id.elPassword);


            if(code.equals("login_true")){
                //shared pref for session only username is passed
                sessionUser= userNameClear.getText().toString(); //convert Edittext to String
                ShareUserName("Session_User", sessionUser ); //create session of user

                Intent intent = new Intent(activity, LoginHome.class);
                intent.putExtra("message", message);
                activity.startActivity(intent);
            }else if(code.equals("login_false")){
                LoadRemember();
                ClearRemember();
                ClearLoginEditText(passwordClear);
                Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            Log.d("Whats wrong?", json.toString());
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

    }



    public void ClearLoginEditText(EditText passwordClear){         //clear the detail before moving forward
        passwordClear.setText("");
    }

    private void LoadRemember(){
        //load shared pref
        shareLogin= PreferenceManager.getDefaultSharedPreferences(activity);
        loadUser= shareLogin.getString("Username", " ");
        loadPass= shareLogin.getString("Password", " ");
        userNameClear.setText(loadUser);
        passwordClear.setText(loadPass);
    }


    //method for sharedpreference
    private void ClearRemember(){  //clear shared pref
        shareLogin= PreferenceManager.getDefaultSharedPreferences(activity);
        shareLogin.edit().remove("Username").commit();
        shareLogin.edit().remove("Password").commit();
    }

    private void ShareUserName(String key, String value){
        shareUser= PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editorUser = shareUser.edit();
        editorUser.putString(key, value);
        editorUser.commit();

    }
}
