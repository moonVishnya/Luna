package moonvishnya.xyz.luna.image_worker;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;

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

import moonvishnya.xyz.luna.server_info.Server;
import moonvishnya.xyz.luna.user_info.UserInformation;

public class CameraImageUploader extends AsyncTask<Void, Void, Void> {

    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    private static Context context;
    public CameraImageUploader(Context context, Uri file_uri, String image_name) {
        this.context = context;
        this.file_uri = file_uri;
        this.image_name = image_name;
    }

    @Override
    protected Void doInBackground(Void... Void) {

        bitmap = BitmapFactory.decodeFile(file_uri.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //   bitmap.recycle();

        byte[] array = stream.toByteArray();
        encoded_string = Base64.encodeToString(array, 0);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        makeRequest();
    }


    private void makeRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST, Server.SERVER_NAME + Server.UPLOAD_SCRIPT + UserInformation.getUSERNAME(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("encoded_string", encoded_string);
                    map.put("image_name", image_name);
                    return map;
                }
            };
            requestQueue.add(request);
        } catch (Exception e) {e.printStackTrace();}
    }



}


