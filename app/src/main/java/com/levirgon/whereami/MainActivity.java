package com.levirgon.whereami;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.levirgon.whereami.Retrofit.PlaceServiceProvider;
import com.levirgon.whereami.event.ErrorEvent;
import com.levirgon.whereami.event.NearbyPlacesEvent;
import com.levirgon.whereami.model.ResultsItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static final int MY_LOCATION_ACCESS = 101;
    private RecyclerView mCategoryList;
    private LinearLayoutManager linearLayoutManager;
    private CategoriesAdapter mAdapter;
    private Location mCurrentLocation;
    private Geocoder mGeocoder;
    private List<Address> mAddressList;
    private ProgressDialog progressDialog;
    private PlaceServiceProvider mServiceProvider;
    private int mRadius = 1000;
    private FloatingActionButton locationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiate();
    }

    private void initiate() {
        locationButton = findViewById(R.id.location_access_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadActivity();
            }
        });
        setupCategoriesList();
        if (isNetworkOnline(this)) {
            mServiceProvider = new PlaceServiceProvider();
            progressDialog = new ProgressDialog(this);
            mGeocoder = new Geocoder(this, Locale.getDefault());
            requestPermission();
        } else {
            Toast.makeText(this, "Please Turn On Network", Toast.LENGTH_LONG).show();
        }

    }

    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    public static boolean isNetworkOnline(Context con) {
        boolean status;
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm != null ? cm.getNetworkInfo(0) : null;

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                assert cm != null;
                netInfo = cm.getNetworkInfo(1);

                status = netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    private void getLocation() {
        Intent locationIntent = new Intent(this, UserLocationActivity.class);
        startActivityForResult(locationIntent, 0);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                        , MY_LOCATION_ACCESS);
            }

        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_ACCESS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "This App requires Location ACCESS to work", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                break;
        }
    }

    private void setupCategoriesList() {
        mCategoryList = findViewById(R.id.categories_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCategoryList.setLayoutManager(linearLayoutManager);
        mCategoryList.setItemAnimator(new DefaultItemAnimator());
        if (mAdapter == null) {
            mAdapter = new CategoriesAdapter(this);
            mCategoryList.setAdapter(mAdapter);
        } else {
            mCategoryList.setAdapter(mAdapter);
        }
        List<String> categories = new ArrayList<>();
        categories.add("hospital");
        categories.add("laundry");
        categories.add("atm");
        categories.add("library");
        categories.add("bank");
        categories.add("beauty_salon");
        categories.add("cafe");
        categories.add("pharmacy");
        categories.add("restaurant");
        categories.add("gas_station");
        categories.add("university");
        mAdapter.addAll(categories);
    }

    public void onItemSelected(String text) {
        mServiceProvider.requestPlaces(mCurrentLocation, mRadius, text);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mCurrentLocation = data.getParcelableExtra("CURRENT_LOCATION");
            locationButton.setVisibility(View.GONE);
            getLocationInformation(mCurrentLocation);
        }

    }

    private void getLocationInformation(Location location) {
        try {
            mAddressList = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = mAddressList.get(0).getAddressLine(0);
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNearbyPlacesEvent(NearbyPlacesEvent event) {
        //  mProgressBar.setVisibility(View.GONE);
        List<ResultsItem> places = event.getPlaces();
        //add the data to a list
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        String errorMessage = event.getErrorMessage();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}
