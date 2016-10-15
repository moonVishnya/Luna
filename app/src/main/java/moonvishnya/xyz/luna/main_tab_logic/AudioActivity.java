package moonvishnya.xyz.luna.main_tab_logic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import moonvishnya.xyz.luna.R;
import moonvishnya.xyz.luna.image_worker.CameraImageUploader;
import moonvishnya.xyz.luna.permissions.PermissionVerifier;
import moonvishnya.xyz.luna.server_info.Server;
import moonvishnya.xyz.luna.user_info.UserInformation;

/**
 * Created by Федя on 05.10.2016.
 */
public class AudioActivity extends Activity {

    private static Button b;
    private String image_name;
    private File file;
    private Uri file_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        PermissionVerifier.verifyStoragePermissions(this);
        b = (Button) findViewById(R.id.camerabtn);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getFileUri();
                i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                startActivityForResult(i, 10);
            }
        });
    }

    private void getFileUri() {
        image_name = "profile_" + UserInformation.getUSERNAME();
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name
        );

        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            new CameraImageUploader(getApplicationContext(), file_uri, image_name).execute();
        }
    }


}
