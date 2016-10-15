package moonvishnya.xyz.luna.chats_activity;



        import android.app.Activity;
        import android.content.ContentResolver;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.provider.MediaStore;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.text.format.Time;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.SimpleAdapter;
        import android.widget.Toast;


        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import moonvishnya.xyz.luna.R;
        import moonvishnya.xyz.luna.server_info.Server;
        import moonvishnya.xyz.luna.user_info.UserInformation;


public class DialogsActivity extends Activity {

    ListView recList;
    List<ContactInfo> dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        recList = (ListView) findViewById(R.id.cardList);
//        recList.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList.setLayoutManager(llm);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(Server.SERVER_NAME + Server.JSON_MESSAGESLOADER_SCRIPT + UserInformation.getUSERNAME());
        MessagesAdapter messagesAdapter = new MessagesAdapter(this, dialogs);
        assert recList != null;
        recList.setAdapter(messagesAdapter);

//        recList.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recList.setLayoutManager(llm);
//
//        ContactAdapter ca = new ContactAdapter(createList(30));
//        recList.setAdapter(ca);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.serch_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    protected List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.username = "@Moon";
            ci.msg = "Hi Luna";
            ci.time = "сегодня, 14:21";

            result.add(ci);

        }

        return result;
    }


    private String downloadUrl(String strUrl) throws IOException {

        String json = "";
        InputStream inputStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = "";

// ansver.substring(ansver.indexOf("8</b><br />") + 12, ansver.indexOf("]") + 1);

            // sb.append("{\"Messages\":");
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String answer = sb.toString();
            answer =  answer.substring(answer.indexOf("8</b><br />") + 12, answer.indexOf("]") + 1);
            Log.i("answer", answer);
            String jsonA = "{\"Messages\":[";
            jsonA += answer;
            Log.i("jsonA", jsonA);
            jsonA += "}";
            Log.i("jsonA", jsonA);
            sb.append("}");
            json = jsonA;
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            inputStream.close();
            Log.i("i",json);
        }

        return json;
    }



    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String json = null;

        @Override
        protected String doInBackground(String... url) {
            try {
                json = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
            listViewLoaderTask.execute(result);
        }
    }


    private class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter> {

        JSONObject jObject;

        @Override
        protected SimpleAdapter doInBackground(String... strJson) {
            try {
                jObject = new JSONObject(strJson[0]);
                JsonParser jsonParser = new JsonParser();
                jsonParser.parse(jObject);
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }


            List<HashMap<String, Object>> messages = null;
            JsonParser jsonParser = new JsonParser();

            try {
                messages = jsonParser.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            String[] from = {"name", "time", "content", "cover"};
            int[] to = {R.id.chat_username, R.id.chat_time, R.id.chat_msg_content, R.id.pic};
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), messages, R.layout.activity_chats, from, to);

            return adapter;
        }

        @Override
        protected void onPostExecute(SimpleAdapter adapter) {
            recList.setAdapter(adapter);

            for (int i = 0; i < adapter.getCount(); i++) {
                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
                String imgUrl = (String) Server.SERVER_NAME + hm.get("cover");

                ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
                HashMap<String, Object> hmDownload = new HashMap<String, Object>();
                hm.put("cover", imgUrl);
                hm.put("position", i);

                imageLoaderTask.execute(hm);
            }
        }
    }

    private class ImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

            InputStream iStream = null;
            String imgUrl = (String) hm[0].get("cover");
            int position = (Integer) hm[0].get("position");
            String usID = (String) hm[0].get("name");
            URL url;
            try {
                url = new URL(Server.SERVER_NAME + "/photos/profile_" + usID + imgUrl);
                Log.i("id", usID);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                File cacheDirectory = getBaseContext().getCacheDir();
                File tmpFile = new File(cacheDirectory.getPath() + position + ".png");
                FileOutputStream fOutStream = new FileOutputStream(tmpFile);
                Bitmap b = BitmapFactory.decodeStream(iStream);
                b.compress(Bitmap.CompressFormat.PNG, 80, fOutStream);
                fOutStream.flush();
                fOutStream.close();
                HashMap<String, Object> hmBitmap = new HashMap<String, Object>();
                hmBitmap.put("cover", tmpFile.getPath());
                hmBitmap.put("position", position);
                return hmBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            String path = (String) result.get("cover");
            int position = (int) result.get("position");
            MessagesAdapter adapter = (MessagesAdapter) recList.getAdapter();
            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);
            hm.put("cover", path);
            adapter.notifyDataSetChanged();
        }
    }

}

