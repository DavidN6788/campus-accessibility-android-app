package com.example.campus_accessability_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_accessability_app.databinding.ActivityHomeBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private boolean speechButtonPressed = false;

    private MapFrontend mapFrontend;
    private static Context appContext;

    protected ActivityHomeBinding binding;
    protected SupportMapFragment mapFragment;
    protected ListView searchListView;
    protected MenuItem searchMenu;
    protected SearchView searchView;

    // Permission code numbers:
    private final int LOCATION_PERMISSION_CODE = 1;

    ArrayAdapter<String> adapter;
    private boolean searchPageSetUp = false;
    public static boolean routeDisplayed = false;

    // Report button system
    protected int reportButtonClicks = 0;
    protected boolean reportButtonClicked = false;
    protected Button report;

    // Floating action button animation
    protected FloatingActionButton addButton, reportButton, favouriteButton, speechButton;
    protected Animation fromBottom, toBottom, rotateOpen, rotateClose;
    protected boolean isOpen = false;

    // Favourite Feature
    protected Button recentButtonOnFavouritePage;
    protected Button favButOnFavPage;
    protected static boolean favPageOpen = false;
    protected static ListView favouriteListView;
    protected static ArrayList<String> favouriteList = new ArrayList<>();
    protected static ListViewAdapter favouriteAdapter;
    protected LinearLayout favouritePage;

    // Recent Feature
    protected Button favButOnRecentPage;
    protected Button recentButtonOnRecPage;
    protected static ListView recentListView;
    protected static boolean recPageOpen = false;
    protected static ArrayList<String> recentList = new ArrayList<>();
    protected static ListViewAdapter recentAdapter;
    protected String destination;
    protected LinearLayout recentPage;

    // Update Features
    protected ListView updateListView;
    protected LinearLayout updatesPage;
    protected ArrayList<Report> reportList = new ArrayList<>();
    protected UpdatesListAdapter updateAdapter;

    // Setting Features
    protected boolean setUpPageOpen = false;
    protected ListView settingListView;
    protected FrameLayout settingsPage;
    protected ArrayList<String> settingsList = new ArrayList<>();
    protected ArrayAdapter<String> settingAdapter;
    protected TextView settingText;

    // Text to Speech
    public static TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Navigation menu:
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindNavigationMenu();

        // Map fragment:
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        appContext = this;

        for (Report report: BackendAPI.reports) {
            Log.e("myapp", "Report = nodes " + report.startNode + " and " + report.endNode + " time: " + report.timestamp.toString());
            reportList.add(0, report);
        }

        createFloatingActionButtons();
        setUpUpdatePage();

        // Populate settings
        settingsList.add("Account");
        settingsList.add("Notifications");
        settingsList.add("Privacy");
        settingsList.add("Security");
        settingsList.add("Help");
        settingsList.add("App Theme");
        settingsList.add("App Accessibility");
        settingsList.add("About");
        settingsList.add("");
        settingsList.add("Log Out");
    }

    protected static Context getAppContext() {
        return appContext;
    }

    @SuppressLint("MissingPermission")
    protected void startUpdatingUserCoordinates() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mapFrontend.setUserLat(location.getLatitude());
                mapFrontend.setUserLong(location.getLongitude());
                //Log.d("location", "User coordinates updated.");
            }
        });
    }

    private void setUpSearchPage() throws InterruptedException {
        if (searchPageSetUp) {
            return;
        }
        searchPageSetUp = true;

        String[] sortedNodeNames = MapFrontend.nodeNames.clone();
        Arrays.sort(sortedNodeNames);

        adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, sortedNodeNames);
        searchListView = (ListView) findViewById(R.id.searchListView);
        searchListView.setAdapter(adapter);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = (String) searchListView.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "You selected: "+destination ,Toast.LENGTH_SHORT).show();
                if (mapFrontend.onDestinationSelected(destination)) {
                    // Does not run if user location data is not currently available.
                    showMapPage();
                    routeDisplayed = true;
                }
            }
        });
        searchListView.setEmptyView(findViewById(R.id.searchEmptyTextView));
        searchListView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview/30429439#30429439
        getMenuInflater().inflate(R.menu.search_menu, menu);

        searchMenu = menu.findItem(R.id.location_search);
        searchView = (SearchView) searchMenu.getActionView();
        searchView.setQueryHint("Search destination...");

        searchView.setOnQueryTextListener(this);
        hideSearchViews(); // as default screen is the home screen
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapFrontend = new MapFrontend(googleMap);
        mapFrontend.setUpMap();

        if (ContextCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // Permission granted:
            mapFrontend.locationGranted();
            startUpdatingUserCoordinates();
        } else {
            // Else, request permission:
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (result[0] == PackageManager.PERMISSION_GRANTED) {
                mapFrontend.locationGranted();
                startUpdatingUserCoordinates();
            } else {
                // TODO: access to location denied
            }
        }
    }

    private void hideSearchViews() {
        if (searchPageSetUp) {
            searchListView.setVisibility(View.GONE);
        }
        searchMenu.setVisible(false);
        searchView.setVisibility(View.GONE);
    }

    private void showSearchViews() {
        searchListView.setVisibility(View.VISIBLE);
        searchMenu.setVisible(true);
        searchView.setVisibility(View.VISIBLE);
    }

    public void showMapPage() {
        hideSearchViews();
        getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        // instruction views are displayed via MapFrontend
    }

    private void hideMapPage() {
        getSupportFragmentManager().beginTransaction().hide(mapFragment).commit(); // open by default; hide if open
        mapFrontend.hideInstructionViews();
        mapFrontend.hideDetailsView();
        mapFrontend.hideBuildingInfoWindow();
    }

    private void setButtonsClickable(boolean set){
        addButton.setClickable(set);
        reportButton.setClickable(set);
        favouriteButton.setClickable(set);
        speechButton.setClickable(set);
    }

    private void hideFloatingActionButtons(){
        if(isOpen){
            addButton.performClick();
        }
        addButton.hide();
        reportButton.setVisibility(View.GONE);
        favouriteButton.setVisibility(View.GONE);
        speechButton.setVisibility(View.GONE);
        setButtonsClickable(false);
    }

    private void showFloatingActionButtons(){
        addButton.show();
    }

    private void createReportButton(){
        reportButtonClicked = true;
        // Report button
        report = findViewById(R.id.button3);
        report.setVisibility(View.GONE);
        if(reportButtonClicks % 2 == 0){
            report.setVisibility(View.VISIBLE);
            mapFrontend.polyLineClickCheck(routeDisplayed);
            reportButtonClicks ++;
        }else{
            reportButtonClicked = false;
            report.setVisibility(View.GONE);
            reportButtonClicks ++;
            if(!routeDisplayed){
                mapFrontend.clearMapObjects();
            }
        }
    }

    /**
     * This method replaces current icon page on toolbar with desired icon page on toolbar
     */
    @SuppressLint("MissingPermission")
    private void bindNavigationMenu(){
        Button recentButtonOnFavouritePage = findViewById(R.id.buttonOne2);
        Button favButOnRecentPage = findViewById(R.id.recentButtonOne3);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            hideSearchViews(); // hide in case visible
            hideMapPage();

            switch (item.getItemId()){
                case R.id.home:
                    hideSettingsPage();
                    hideRecentPage();
                    hideFavouriteFeatures();
                    hideUpdatePage();
                    showFloatingActionButtons();
                    setButtonsClickable(true);
                    getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                    if (routeDisplayed) {
                        mapFrontend.showInstructionViews();
                        mapFrontend.showDetailsView();
                    }
                    break;
                case R.id.favourite:
                    hideFloatingActionButtons();
                    hideSettingsPage();
                    hideUpdatePage();
                    showFavouriteFeatures();
                    setUpFavouritePage();

                    recentButtonOnFavouritePage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideFavouriteFeatures();
                            showRecentPage();
                            setUpRecentPage();
                            Log.d("debug", "recent page open");
                        }
                    });

                    favButOnRecentPage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideRecentPage();
                            showFavouriteFeatures();
                            setUpFavouritePage();
                            Log.d("debug", "fav open");
                        }
                    });
                    break;
                case R.id.settings:
                    hideFloatingActionButtons();
                    hideRecentPage();
                    hideFavouriteFeatures();
                    hideUpdatePage();

                    showSettingPage();
                    setUpSettingPage();
                    break;
                case R.id.search:
                    hideFloatingActionButtons();
                    hideRecentPage();
                    hideFavouriteFeatures();
                    hideSettingsPage();
                    hideUpdatePage();
                    try {
                        setUpSearchPage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showSearchViews();
                    break;
                case R.id.update:
                    updateReports();
                    hideFloatingActionButtons();
                    hideRecentPage();
                    hideFavouriteFeatures();
                    hideSettingsPage();
                    showUpdatePage();
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
    }

    private void createFloatingActionButtons(){
        addButton = findViewById(R.id.button4);
        reportButton = findViewById(R.id.floatingActionReportButton);
        favouriteButton = findViewById(R.id.floatingActionStarButton);
        speechButton = findViewById(R.id.floatingActionSpeechButton);

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReportButton();

            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speechButtonPressed){
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_volume_off_24));
                    speechButtonPressed = false;
                    MapFrontend.ttsEnabled = false;
                }else{
                    speechButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_volume_up_24));
                    speechButtonPressed = true;
                    MapFrontend.ttsEnabled = true;
                    Toast.makeText(getApplicationContext(), "Text to Speech Enabled", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void animation(){
        if(isOpen){
            addButton.startAnimation(rotateClose);
            reportButton.startAnimation(toBottom);
            favouriteButton.startAnimation(toBottom);
            speechButton.startAnimation(toBottom);
            if(reportButtonClicked && !routeDisplayed){
                report.setVisibility(View.GONE);
                mapFrontend.clearMapObjects();
                reportButtonClicks++;
            }else if(reportButtonClicked && routeDisplayed){
                report.setVisibility(View.GONE);
                reportButtonClicks++;
            }
            reportButton.setClickable(false);
            favouriteButton.setClickable(false);
            speechButton.setClickable(false);
            isOpen = false;
        }else{
            addButton.startAnimation(rotateOpen);
            reportButton.startAnimation(fromBottom);
            favouriteButton.startAnimation(fromBottom);
            speechButton.startAnimation(fromBottom);
            reportButton.setClickable(true);
            favouriteButton.setClickable(true);
            speechButton.setClickable(true);
            isOpen = true;
        }
    }

    private void hideFavouriteFeatures(){
        favouritePage = findViewById(R.id.favouriteListPage);
        favouriteListView = findViewById(R.id.listView);
        favButOnFavPage = findViewById(R.id.buttonOne3);
        recentButtonOnFavouritePage = findViewById(R.id.buttonOne2);

        favouritePage.setVisibility(View.GONE);
        favouriteListView.setVisibility(View.GONE);
        favButOnFavPage.setVisibility(View.GONE);
        recentButtonOnFavouritePage.setVisibility(View.GONE);
    }

    private void showFavouriteFeatures(){
        recPageOpen = false;
        favPageOpen = true;
        favouritePage = findViewById(R.id.favouriteListPage);
        favouriteListView = findViewById(R.id.listView);
        favButOnFavPage = findViewById(R.id.buttonOne3);
        recentButtonOnFavouritePage = findViewById(R.id.buttonOne2);

        favouritePage.setVisibility(View.VISIBLE);
        favouriteListView.setVisibility(View.VISIBLE);
        favButOnFavPage.setVisibility(View.VISIBLE);
        recentButtonOnFavouritePage.setVisibility(View.VISIBLE);


    }

    private void setUpFavouritePage(){
        favouriteListView = findViewById(R.id.listView);
        favouriteAdapter = new ListViewAdapter(getApplicationContext(), favouriteList);
        favouriteListView.setAdapter(favouriteAdapter);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(routeDisplayed){
                    favouriteList.add(destination);
                    favouriteListView.setAdapter(favouriteAdapter);
                    Toast.makeText(getApplicationContext(), destination + " added to favourites", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), "No route selected", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void hideRecentPage(){
        recentPage = findViewById(R.id.recentListPage);
        recentListView = findViewById(R.id.recentListView);
        favButOnRecentPage = findViewById(R.id.recentButtonOne2);
        recentButtonOnRecPage = findViewById(R.id.recentButtonOne3);

        recentPage.setVisibility(View.GONE);
        recentListView.setVisibility(View.GONE);
        favButOnRecentPage.setVisibility(View.GONE);
        recentButtonOnRecPage.setVisibility(View.GONE);

    }

    private void showRecentPage(){
        recPageOpen = true;
        favPageOpen = false;
        recentPage = findViewById(R.id.recentListPage);
        recentListView = findViewById(R.id.recentListView);
        favButOnRecentPage = findViewById(R.id.recentButtonOne2);
        recentButtonOnRecPage = findViewById(R.id.recentButtonOne3);

        recentPage.setVisibility(View.VISIBLE);
        recentListView.setVisibility(View.VISIBLE);
        favButOnRecentPage.setVisibility(View.VISIBLE);
        recentButtonOnRecPage.setVisibility(View.VISIBLE);

    }

    private void setUpRecentPage(){
        recentListView = findViewById(R.id.recentListView);
        recentAdapter = new ListViewAdapter(getApplicationContext(), recentList);
        recentListView.setAdapter(recentAdapter);
        if(routeDisplayed && !recentList.contains(destination)){
            recentList.add(destination);
            recentListView.setAdapter(recentAdapter);
        }
    }

    public static void removeRecentItem(int remove){
        recentList.remove(remove);
        recentListView.setAdapter(recentAdapter);
    }

    public static void removeFavouriteItem(int remove){
        favouriteList.remove(remove);
        favouriteListView.setAdapter(favouriteAdapter);
    }

    private void hideSettingsPage(){
        settingText = findViewById(R.id.textView4);
        settingsPage = findViewById(R.id.settingsPage);
        settingListView = findViewById(R.id.settingsListView);

        settingText.setVisibility(View.GONE);
        settingsPage.setVisibility(View.GONE);
        settingListView.setVisibility(View.GONE);
    }

    private void showSettingPage(){
        settingText = findViewById(R.id.textView4);
        settingsPage = findViewById(R.id.settingsPage);
        settingListView = findViewById(R.id.settingsListView);

        settingText.setVisibility(View.VISIBLE);
        settingsPage.setVisibility(View.VISIBLE);
        settingListView.setVisibility(View.VISIBLE);
    }

    private void setUpSettingPage(){
        settingListView = findViewById(R.id.settingsListView);
        settingAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, settingsList);
        settingListView.setAdapter(settingAdapter);
    }

    private void hideUpdatePage(){
        updatesPage = findViewById(R.id.updatePage);

        updatesPage.setVisibility(View.GONE);
        updateListView.setVisibility(View.GONE);
    }

    private void showUpdatePage(){
        updatesPage = findViewById(R.id.updatePage);

        updatesPage.setVisibility(View.VISIBLE);
        updateListView.setVisibility(View.VISIBLE);
    }

    protected void updateReports() {
        BackendAPI backendAPI = new BackendAPI(MainActivity.getContext());
        backendAPI.fetchReports();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAdapter.clear();
                reportList.clear();

                for (Report report: backendAPI.reports) {
                    reportList.add(0, report);
                }

                Log.e("test", String.valueOf(reportList));
                updateAdapter.addAll(reportList);

                updateAdapter.notifyDataSetChanged();
                updateListView.invalidateViews();
                updateListView.refreshDrawableState();
            }
        });
    }

    private void setUpUpdatePage() {
        updateListView = findViewById(R.id.updatesListView);
        updateAdapter = new UpdatesListAdapter(getApplicationContext(), reportList);
        updateListView.setAdapter(updateAdapter);

        updateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private Polyline getLineFromEdge(HashMap<Polyline, GraphEdge> polylineGraphEdgeHashMap, GraphEdge edge) {
        for(Map.Entry<Polyline, GraphEdge> entry: polylineGraphEdgeHashMap.entrySet()){
            if(edge.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }
}