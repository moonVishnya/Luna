package moonvishnya.xyz.luna.main_tab_logic;



import android.app.FragmentManager;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.net.HttpURLConnection;

import moonvishnya.xyz.luna.R;
import moonvishnya.xyz.luna.dialog_fragment.DialogFragment;
import moonvishnya.xyz.luna.server_info.Server;
import moonvishnya.xyz.luna.user_info.UserInformation;



public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static TextView username;
    private static TextView status;
    private static ImageView prof_photo;
    private static HttpURLConnection connection;
    private static final String TITLE = "LUNA";
    private ImageView prof_image;
    private ImageView prof_background;
    private TextView prof_status;
    private TextView prof_name;
    private TextView prof_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        setTitle(TITLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    //    initMyPage();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        initHeader(headerLayout);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.dialogs_frame, new DialogFragment()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            // Handle the camera action
        } else if (id == R.id.nav_messages) {

            //

            //


        } else if (id == R.id.nav_music) {
            startActivity(new Intent(getApplicationContext(), AudioActivity.class));

        } else if (id == R.id.nav_options) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initMyPage() {


        prof_image = (ImageView) findViewById(R.id.my_page_profile_image);
        prof_status = (TextView) findViewById(R.id.my_page_status);
        prof_username = (TextView) findViewById(R.id.main_textview_title);
        prof_background = (ImageView) findViewById(R.id.main_imageview_placeholder);
        Log.i("ke", UserInformation.getSTATUS());

        Picasso.with(getApplicationContext()).load(Server.SERVER_NAME + "/photos/profile_" + UserInformation.getUSERNAME() + ".JPG" ).into(prof_image);
        Picasso.with(getApplicationContext()).load(Server.SERVER_NAME + "/photos/profile_" + UserInformation.getUSERNAME() + ".JPG").into(prof_background);
        prof_status.setText(UserInformation.getSTATUS());
        prof_username.setText(UserInformation.getUSERNAME());

    }

    private void initHeader(final View headerLayout) {
        username = (TextView) headerLayout.findViewById(R.id.username_header);
        status = (TextView) headerLayout.findViewById(R.id.status_header);
        username.setText(" " + UserInformation.getUSERNAME());
        status.setText(" " + UserInformation.getSTATUS());
        prof_photo = (ImageView) headerLayout.findViewById(R.id.profile_img_header);
        Picasso.with(this)
                .load(Server.SERVER_NAME + "/photos/profile_" + UserInformation.getUSERNAME() + ".JPG")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        headerLayout.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        Picasso.with(getApplicationContext()).load(Server.SERVER_NAME + "/photos/profile_" + UserInformation.getUSERNAME() + ".JPG" ).into(prof_photo);
    }

//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }



}
