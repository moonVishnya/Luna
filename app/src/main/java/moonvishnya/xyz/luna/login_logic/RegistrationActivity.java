package moonvishnya.xyz.luna.login_logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import moonvishnya.xyz.luna.R;
import moonvishnya.xyz.luna.main_tab_logic.UserMainActivity;
import moonvishnya.xyz.luna.registration_validator.Validator;
import moonvishnya.xyz.luna.server_info.Server;
import moonvishnya.xyz.luna.user_info.UserInformation;


public class RegistrationActivity extends AppCompatActivity {

    private static ActionBar actionBar;
    private static EditText etUsername;
    private static EditText etPassword;
    private static EditText etEmail;
    private static Button OK;
    private static TextInputLayout usernameLayout;
    private static TextInputLayout emailLayout;

    private static final String WRONG_USERNAME = "Недопустимый логин";
    private static final String WRONG_EMAIL = "Некорректный e-mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        usernameLayout = (TextInputLayout) findViewById(R.id.input_layout_username);
        usernameLayout.setErrorEnabled(true);

        emailLayout = (TextInputLayout) findViewById(R.id.input_layout_email);
        emailLayout.setErrorEnabled(true);


        etUsername = (EditText) findViewById(R.id.registration_etUsername);
        etPassword = (EditText) findViewById(R.id.registration_etPassword);
        etEmail = (EditText) findViewById(R.id.registration_etEmail);
        OK = (Button) findViewById(R.id.registration_confirmBtn);
//        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6f8fc7")));
//        actionBar.setDisplayHomeAsUpEnabled(true);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                if (Validator.isValid(username) && Validator.isValid(password) &&
                        Validator.isValid(email)) {
                    RegistrableUser r = new RegistrableUser(RegistrationActivity.this);
                    r.execute(username, password, email);

                } else {
                    if (!Validator.isValid(username))
                    usernameLayout.setError(WRONG_USERNAME);
                    if (!Validator.isValid(email))
                    emailLayout.setError(WRONG_EMAIL);
                }
            }
        });

    }

    private class RegistrableUser extends AsyncTask<String, Void, Boolean> {
        private  boolean success=false;
        private  Integer server_answer;
        private HttpURLConnection connection;
        private ProgressDialog dialog;
        private Activity activity;
        private Context context;
        private String username, password, email;

        public RegistrableUser(Activity activity) {
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
                        Server.REGISTRATION_SCRIPT + URLEncoder.encode(userInfo[0], "UTF-8")
                        + "&password="
                        + URLEncoder.encode(userInfo[1], "UTF-8")
                        + "&email="
                        + URLEncoder.encode(userInfo[2], "UTF-8");
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
            if (success) {
                username = userInfo[0];
                password = userInfo[1];
                email = userInfo[2];
            }

            return success;
        }

        protected  void onPostExecute(Boolean result) {
            if (result) {
                UserInformation.setUSERNAME(username);
                UserInformation.setPASSWORD(password);
                UserInformation.setEMAIL(email);
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Выберите другой логин/email", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
