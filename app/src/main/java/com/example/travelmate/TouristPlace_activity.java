package com.example.travelmate;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.travelmate.APIS.LocationKeyApi;
import com.example.travelmate.APIS.WeatherApi;
import com.example.travelmate.Adapter.NearByImageAdapter;
import com.example.travelmate.Adapter.PlacePhotosAdapter;
import com.example.travelmate.Adapter.ViewMyRatingAdapter;
import com.example.travelmate.locationkey.LocationKey;
import com.example.travelmate.utility.SaveRating;
import com.example.travelmate.utility.constants;
import com.example.travelmate.utility.util;
import com.example.travelmate.utility.weathericon;
import com.example.travelmate.weather.Weather;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cz.intik.overflowindicator.OverflowPagerIndicator;
import cz.intik.overflowindicator.SimpleSnapHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristPlace_activity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvPhotos, rvratings;
    ArrayList<String> photos, reviews;
    TextView tvPlaceName, tvAbout, tvviewMap, tvRainProbablity;
    ImageView ivWeatherIcon;
    TextView tvTemp, tvHeadline, tvWindspeed;
    LinearLayout weather;
    String locationkey;
    String geolocation;
    String geolocation1;
    FirebaseDatabase database;
    Toolbar toolbar;
    RecyclerView rvnearby;
    DatabaseReference mRef;
    OverflowPagerIndicator overflow;
    String details = "true";
    ArrayList<Integer> images;
    ArrayList<String> name;
    Button btnaddrating, btnviewrating;
    ScrollView scrollview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_place_activity);
        findIds();
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        reviews = new ArrayList<>();
        images = new ArrayList<>();
        name = new ArrayList<>();
        onBackButton();
        setName();
        setImageList();
        photos = new ArrayList<>();
        Intent intent = getIntent();
        geolocation = intent.getStringExtra("geocordinates");
        Log.e("geolocation", geolocation);
        if (geolocation.length() < 19) {
            geolocation = addChar(geolocation, '.', 2);
            geolocation = addChar(geolocation, '.', 12);
        }
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        //  findlatlong(geolocation);
        getDataFromFirebase();
        // getWeatherForecast(locationkey);
        tvviewMap.setOnClickListener(this);
        weather.setOnClickListener(this);
        btnaddrating.setOnClickListener(this);
        btnviewrating.setOnClickListener(this);
        setNearByrecyclerView();
    }

    private void onBackButton() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backicon));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

    private void setName() {
        name.add("Food");
        name.add("Atm");
        name.add("Hotels");
        name.add("Parking");
        name.add("Hospital");
        name.add("Shopping");
        name.add("Bus Stand");
    }

    private void setImageList() {
        images.add(R.drawable.food);
        images.add(R.drawable.atm);
        images.add(R.drawable.hotel);
        images.add(R.drawable.parking);
        images.add(R.drawable.hospital);
        images.add(R.drawable.shopping);
        images.add(R.drawable.bustand);
    }

    private void setNearByrecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvnearby.setLayoutManager(manager);
        NearByImageAdapter nearByImageAdapter = new NearByImageAdapter(getApplicationContext(), images, name, geolocation);
        rvnearby.setAdapter(nearByImageAdapter);
    }

    private void getDataFromFirebase() {
        geolocation1 = geolocation;
        geolocation1 = geolocation1.substring(0, 2) + geolocation1.substring(3);
        geolocation1 = geolocation1.substring(0, 11) + geolocation1.substring(12);
        Log.e("check", geolocation1);
        mRef.child(geolocation1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getFirebaseData(DataSnapshot dataSnapshot) {
        try {
            tvPlaceName.setText(dataSnapshot.child("Placename").getValue().toString());
            tvAbout.setText(dataSnapshot.child("About").getValue().toString());
            Log.e("photo1", dataSnapshot.child("Photos").child("Photo1").getValue().toString());
            photos.add(dataSnapshot.child("Photos").child("Photo1").getValue().toString());
            photos.add(dataSnapshot.child("Photos").child("Photo2").getValue().toString());
            photos.add(dataSnapshot.child("Photos").child("Photo3").getValue().toString());
        } catch (Exception e) {
            Log.e("msg", e.getMessage());
        }
        recyclerViewPhotos(photos);

    }

    private void findlatlong(String geolocation) {


        Double latitide = Double.valueOf(this.geolocation.substring(0, 9));
        Double longitude = Double.valueOf(this.geolocation.substring(10, 19));

        String latlong = latitide + "%2C" + longitude;

        try {
            getLocationKey(latlong);
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
    }

    private void getLocationKey(String latlong) {


        Call<LocationKey> locationkey = LocationKeyApi.LocationKeyApi().getLocationKey(constants.WeatherKEY, latlong, details);
        locationkey.enqueue(new Callback<LocationKey>() {
            @Override
            public void onResponse(Call<LocationKey> call, Response<LocationKey> response) {


                getKey(response);
            }

            @Override
            public void onFailure(Call<LocationKey> call, Throwable t) {

            }
        });
    }

    private void getKey(Response<LocationKey> response) {
        if (response != null) {
            locationkey = response.body().getKey();
            Log.e("locationkey", locationkey);
            getWeatherForecast(locationkey);
        } else {
            Log.e("error1", "error");
        }

    }

    private void getWeatherForecast(String locationkey) {

        Call<Weather> weatherCall = WeatherApi.WeatherApi().getWeather(locationkey, constants.WeatherKEY, details, details);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                putWeatgerData(response);

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

            }
        });

    }

    private void putWeatgerData(Response<Weather> response) {
        tvHeadline.setText(String.valueOf(response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMinimum().getValue()) + "°C");
        Double temp = response.body().getDailyForecasts().get(0).getRealFeelTemperature().getMaximum().getValue();
        tvTemp.setText(String.valueOf(temp) + "°C");
        tvWindspeed.setText(response.body().getDailyForecasts().get(0).getDay().getIconPhrase());
        tvRainProbablity.setText(String.valueOf("Rain Probablity:" + response.body().getDailyForecasts().get(0).getDay().getRainProbability()) + "%");
        int icon = response.body().getDailyForecasts().get(0).getDay().getIcon();
        ivWeatherIcon.setImageResource(weathericon.WeatherIcon(icon));
    }


    private void findIds() {
        toolbar = findViewById(R.id.toolbar);
        rvPhotos = findViewById(R.id.rvPhotos);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvAbout = findViewById(R.id.tvAbout);
        tvviewMap = findViewById(R.id.tvviewOnMap);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvWindspeed = findViewById(R.id.tvwinspeed);
        tvHeadline = findViewById(R.id.tvHeadline);
        weather = findViewById(R.id.weatherlayout);
        overflow = findViewById(R.id.view_pager_indicator);
        tvRainProbablity = findViewById(R.id.tvRainProbablity);
        rvnearby = findViewById(R.id.rvnearby);
        btnaddrating = findViewById(R.id.btnaddrating);
        btnviewrating = findViewById(R.id.btnviewrating);
        rvratings = findViewById(R.id.rvViewRatings);
        scrollview = findViewById(R.id.scrollview);
    }


    private void recyclerViewPhotos(ArrayList<String> photos1) {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(manager);
        PlacePhotosAdapter placePhotosAdapter = new PlacePhotosAdapter(this, photos1, overflow);
        rvPhotos.setAdapter(placePhotosAdapter);
        overflow.attachToRecyclerView(rvPhotos);
        SimpleSnapHelper pagerSnapHelper = new SimpleSnapHelper(overflow);
        rvPhotos.setOnFlingListener(null);
        pagerSnapHelper.attachToRecyclerView(rvPhotos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvviewOnMap:
                viewOnMap();
                break;
            case R.id.weatherlayout:
                onWeatherClick();
                break;
            case R.id.btnaddrating:
                onRatingClicked();
                break;
            case R.id.btnviewrating:
                onViewRatings();
        }
    }

    private void onViewRatings() {
        if (btnviewrating.getText().equals(getResources().getString(R.string.view_ratings))) {
            rvratings.setVisibility(View.VISIBLE);
            btnviewrating.setText(getResources().getString(R.string.hide_rating));
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rvratings.setLayoutManager(manager);
            FirebaseDatabase.getInstance().getReference().child(geolocation1).child("Rating").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            reviews.add(ds.getKey());
                        }
                        ViewMyRatingAdapter viewMyRatingAdapter = new ViewMyRatingAdapter(getApplicationContext(), reviews, dataSnapshot, geolocation1);
                        rvratings.setAdapter(viewMyRatingAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error", databaseError.getMessage());
                }
            });
        } else if (btnviewrating.getText().equals(getResources().getString(R.string.hide_rating))) {
            rvratings.setVisibility(View.GONE);
            btnviewrating.setText(getResources().getString(R.string.view_ratings));
            reviews.clear();
        }


    }

    private void onRatingClicked() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.ratingbar);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        savinguserratings(dialog);
    }

    private void savinguserratings(final Dialog dialog) {
        final LinearLayout linearsave = dialog.findViewById(R.id.linearsave);
        Button btnok = dialog.findViewById(R.id.btnok);
        linearsave.setVisibility(View.VISIBLE);
        final LinearLayout linearlayoutrating = dialog.findViewById(R.id.linearlayoutrating);
        final Button btnrating = dialog.findViewById(R.id.btnrate);
        final EditText review = dialog.findViewById(R.id.review);
        final RatingBar rbRating = dialog.findViewById(R.id.rbRating);
        final EditText reviewtitle = dialog.findViewById(R.id.reviewtite);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRatingSubmit(btnrating, review, rbRating, dialog, reviewtitle, linearlayoutrating, linearsave);
            }
        });
    }

    private void onRatingSubmit(Button btnrating, final EditText review, final RatingBar rbRating, final Dialog dialog, final EditText reviewtitle, final LinearLayout linearlayoutrating, final LinearLayout linearsave) {

        if (rbRating.getRating() == 0) {
            util.toast(getApplicationContext(), getResources().getString(R.string.selectrating));
        } else {
            FirebaseDatabase.getInstance().getReference().child("User Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SaveRating saveRating = new SaveRating(rbRating.getRating(), review.getText().toString().trim(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), dataSnapshot.child("Profile").getValue().toString(), reviewtitle.getText().toString().trim());
                    FirebaseDatabase.getInstance().getReference().child(geolocation1).child("Rating").child(decode(FirebaseAuth.getInstance().getCurrentUser().getEmail())).setValue(saveRating);
                    linearsave.setVisibility(View.GONE);
                    linearlayoutrating.setVisibility(View.VISIBLE);
//                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void onWeatherClick() {
        startActivity(new Intent(this, weatheractivity.class).putExtra("geocoordinates1", geolocation));
    }

    private void viewOnMap() {

        startActivity(new Intent(this, map_2_activity.class).putExtra("geocoordinatesmap", geolocation));
    }

    public String decode(String email) {
        String decoded = email.replace('@', '_');
        return decoded.replace('.', '!');
    }

    public String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
}
