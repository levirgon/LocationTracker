package com.levirgon.tutexplocation;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.levirgon.tutexplocation.Retrofit.PlaceServiceProvider;
import com.levirgon.tutexplocation.event.NearbyPlacesEvent;
import com.levirgon.tutexplocation.model.ResultsItem;
import com.levirgon.tutexplocation.R;
import com.levirgon.tutexplocation.event.ErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private InterstitialAd mInterstitialAd;

    private static final int MY_LOCATION_ACCESS = 101;
    private RecyclerView mCategoryList;
    private RecyclerView mPlacesList;
    private LinearLayoutManager linearLayoutManager;
    private CategoriesAdapter mAdapter;
    private PlacesAdapter mPlacesAdapter;
    private Location mCurrentLocation, destination;
    private Geocoder mGeocoder;
    private List<Address> mAddressList;
    private ProgressDialog progressDialog;
    private PlaceServiceProvider mServiceProvider;
 //   private final int RADIUS = 3000;
    private FloatingActionButton locationButton;
    private LinearLayout mLoadingLayout;
    private TextView loadinTextView,notificationTextView;
    private ImageView pointingIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-7243601331616499~7372862912");

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                initiate();
            }
        });

        initiate();

    }

    private void initiate() {
        loadinTextView = findViewById(R.id.loading_text);
        notificationTextView = findViewById(R.id.notif_text);
        pointingIcon = findViewById(R.id.pointing);
        mLoadingLayout = findViewById(R.id.loading_group);
        mLoadingLayout.setVisibility(View.GONE);
        locationButton = findViewById(R.id.location_access_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadActivity();
            }
        });
        setupCategoriesList();
        setUpPlacesList();
        mServiceProvider = new PlaceServiceProvider();
        progressDialog = new ProgressDialog(this);
        mGeocoder = new Geocoder(this, Locale.getDefault());
        if (isNetworkOnline(this)) {
            requestPermission();
        } else {
            Toast.makeText(this, "Please Turn On Network", Toast.LENGTH_LONG).show();
        }
    }

    private void reloadActivity() {
        finish();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            startActivity(getIntent());
        }

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
        mLoadingLayout.setVisibility(View.VISIBLE);
        loadinTextView.setText("Getting Location");
        notificationTextView.setVisibility(View.VISIBLE);
        pointingIcon.setVisibility(View.VISIBLE);
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
                    Toast.makeText(this, "This App requires PlaceLocation ACCESS to work", Toast.LENGTH_SHORT).show();
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
        categories.add("cafe");
        categories.add("atm");
        categories.add("restaurant");
        categories.add("pharmacy");
        categories.add("bank");
        categories.add("shopping_mall");
        categories.add("gas_station");
        categories.add("university");
        categories.add("library");
        categories.add("beauty_salon");
        categories.add("police");

        mAdapter.addAll(categories);
    }

    private void setUpPlacesList() {

        mPlacesList = findViewById(R.id.nearby_places_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlacesList.setLayoutManager(linearLayoutManager);
        mPlacesList.setItemAnimator(new DefaultItemAnimator());


        if (mPlacesAdapter == null) {
            mPlacesAdapter = new PlacesAdapter(this);
            mPlacesList.setAdapter(mPlacesAdapter);
        } else {
            mPlacesList.setAdapter(mPlacesAdapter);
        }
    }

    public void onItemSelected(String text) {
        if (isNetworkOnline(this)) {
            mPlacesAdapter.setCurrentLocation(mCurrentLocation);
            mLoadingLayout.setVisibility(View.VISIBLE);
            notificationTextView.setVisibility(View.GONE);
            pointingIcon.setVisibility(View.GONE);
            loadinTextView.setText("Loading Places");
            mServiceProvider.requestPlaces(mCurrentLocation, "distance", text);
        } else {
            Toast.makeText(this, "Turn on Network", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            notificationTextView.setVisibility(View.VISIBLE);
            pointingIcon.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
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
        mLoadingLayout.setVisibility(View.GONE);
        List<ResultsItem> places = event.getPlaces();
        mPlacesAdapter.clear();
        mPlacesAdapter.addAll(places);
        //add the data to a list
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        String errorMessage = event.getErrorMessage();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void onPlaceSelected(ResultsItem place) {
        double lat = place.getGeometry().getPlaceLocation().getLat();
        double lng = place.getGeometry().getPlaceLocation().getLng();

        Location destination = new Location("");
        destination.setLatitude(lat);
        destination.setLongitude(lng);


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
