package moonvishnya.xyz.luna.login_logic;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import moonvishnya.xyz.luna.R;
import moonvishnya.xyz.luna.main_tab_logic.UserMainActivity;
import moonvishnya.xyz.luna.server_info.Server;
import moonvishnya.xyz.luna.user_info.UserInformation;

public class LoginActivity extends Activity {

    private static EditText username;
    private static EditText password;
    private static Button loginBtn;
    private static TextView registrateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.buttonLogin);
        registrateBtn = (TextView) findViewById(R.id.buttonRegistration);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInformation.setUSERNAME(username.getText().toString());
                UserInformation.setPASSWORD(password.getText().toString());
                Loginer loginer = new Loginer(LoginActivity.this);
                loginer.execute(UserInformation.USERNAME, UserInformation.PASSWORD);
            }
        });

        registrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    private class Loginer extends AsyncTask<String, Void, Boolean> {
        private  boolean success=false;
        private  Integer server_answer;
        private  HttpURLConnection connection;
        private ProgressDialog dialog;
        private Activity activity;
        private Context context;

        public Loginer(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        protected void onPreExecute() {
            dialog.show();
        }

        protected Boolean doInBackground(String... userInfo) {

            try {
                String post_url = Server.SERVER_NAME +
                        Server.AUTHENTICATION_SCRIPT + URLEncoder.encode(userInfo[0], "UTF-8")
                        + "&password="
                        + URLEncoder.encode(userInfo[1], "UTF-8");
                URL url = new URL(post_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.connect();
                server_answer = connection.getResponseCode();
                    if (server_answer == HttpsURLConnection.HTTP_OK) {
                        success = true;
                    }
            } catch (IOException e) {

            } finally {
                connection.disconnect();
            }

            return success;
        }

        protected  void onPostExecute(Boolean result) {
            if (result) {
                UserLoader l = new UserLoader();
                l.execute(UserInformation.getUSERNAME());
                dialog.dismiss();
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT).show();

            }
        }



        }


    private class UserLoader extends AsyncTask<String, Void, Boolean> {

        private String ansver;
        private  HttpURLConnection connection;
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String post_url = Server.SERVER_NAME
                        + Server.USERLOADER_SCRIPT
                        + URLEncoder.encode(params[0], "UTF-8");
                URL url = new URL(post_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.connect();
            } catch (IOException ex) {

            }

            try {
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                ansver = sb.toString();
                ansver ="[" + ansver.substring(ansver.indexOf("8</b><br />") + 12, ansver.indexOf("]") + 1);

                is.close();
                br.close();
                Log.i("t", ansver);


            } catch (Exception e) {

            } finally {
                connection.disconnect();
            }

            if (ansver != null && !ansver.trim().equals("")) {

                try {
                    JSONArray ja = new JSONArray(ansver);
                    JSONObject jo;




                    jo = ja.getJSONObject(0);
                    Log.i("f", jo.getString("status"));
                    UserInformation.setSTATUS(jo.getString("status"));
                    Log.i("f", UserInformation.getSTATUS());
                    UserInformation.setEMAIL(jo.getString("email"));
                    UserInformation.setPHOTO(jo.getString("profilePhoto"));


                } catch (JSONException e) {

                }
            }


            return true;
        }


        protected  void onPostExecute(Boolean result) {
            if (result)
            startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
        }

    }





}
