package com.kenfestoche.smartcoder.kenfestoche;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kenfestoche.smartcoder.kenfestoche.model.Utilisateur;
import com.kenfestoche.smartcoder.kenfestoche.webservices.WebService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;


public class MapActivity extends Fragment implements LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap googleMap;

    GPSTracker gps;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Utilisateur User;
    ImageView imLegAmis;
    ImageView imLegInconnu;
    ImageView imLegKiffs;
    ImageView imFlecheMap;
    ImageView imGeoloc;
    ImageView imPhotoMap;
    ViewPager pager;
    Boolean clicAmis;
    Boolean clicKiff;
    Boolean clicInconnu;
    Boolean cliclsoiree;
    ImageButton btFetes;
    ImageButton btDebit;
    ImageButton btconcert;
    ImageButton btbar;
    ImageButton btbarnuit;
    ImageButton btboitenuit;
    ImageButton btautres;

    TextView txFetes;
    TextView txDate;
    TextView txHoraire;
    TextView txConcert;
    TextView txBar;
    TextView txBoiteNuit;
    TextView txBarNuit;
    TextView txAutres;
    TextView txDebit;
    TextView txDateDebit;
    TextView txHoraireDebit;

    TextView txNomSoiree;
    TextView txDescriptif;
    TextView txHoraireSoiree;
    TextView txDateFermeture;
    TextView txAdresse;
    TextView txLienSite;
    TextView txTypeSoiree;
    Marker marker;


    private TimePicker timePicker1;

    private Calendar myCalendar;

    ImageView impopupmap;

    Button Valider;
    Button ValiderDetail;

    LinearLayout rlvFetesSoiree;

    LinearLayout lnDetailSoiree;

    RelativeLayout rlvMap;

    ImageView imSoiree;

    RefreshMap refreshMap;

    MarkerOptions MaPosition;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        clicAmis=false;
        clicInconnu=false;
        clicKiff=false;
        cliclsoiree=false;
        pager = (ViewPager) container;



        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        imLegAmis = (ImageView) rootView.findViewById(R.id.imlegamis);
        imLegKiffs = (ImageView) rootView.findViewById(R.id.imlegmatch);
        imLegInconnu = (ImageView) rootView.findViewById(R.id.imleginconnu);
        imFlecheMap = (ImageView) rootView.findViewById(R.id.imFlecheDroiteMap);
        imGeoloc = (ImageView) rootView.findViewById(R.id.imgeoloc);
        imPhotoMap = (ImageView) rootView.findViewById(R.id.imPhotoMap);
        imSoiree = (ImageView) rootView.findViewById(R.id.imSoiree);

        btFetes = (ImageButton) rootView.findViewById(R.id.rdFetes);
        btDebit = (ImageButton) rootView.findViewById(R.id.rdAlcool);
        btconcert = (ImageButton) rootView.findViewById(R.id.rdConcert);
        btbar = (ImageButton) rootView.findViewById(R.id.rdBar);
        btboitenuit = (ImageButton) rootView.findViewById(R.id.rdBoite);
        btbarnuit = (ImageButton) rootView.findViewById(R.id.rdBarNuit);
        btautres = (ImageButton) rootView.findViewById(R.id.rdAutre);
        Valider = (Button) rootView.findViewById(R.id.btValidSoiree);
        ValiderDetail = (Button) rootView.findViewById(R.id.btValidDetail);
        impopupmap = (ImageView) rootView.findViewById(R.id.impopupmap);
        lnDetailSoiree = (LinearLayout) rootView.findViewById(R.id.lndetailsoiree);

        lnDetailSoiree.setVisibility(View.GONE);

        Typeface face=Typeface.createFromAsset(getActivity().getAssets(),"fonts/weblysleekuil.ttf");


        txFetes = (TextView) rootView.findViewById(R.id.txfetes);
        txDate = (TextView) rootView.findViewById(R.id.txdate);
        txHoraire = (TextView) rootView.findViewById(R.id.txhoraire);
        txConcert = (TextView) rootView.findViewById(R.id.txConcert);
        txBar = (TextView) rootView.findViewById(R.id.txBar);
        txBarNuit = (TextView) rootView.findViewById(R.id.txBarNuit);
        txAutres = (TextView) rootView.findViewById(R.id.txAutre);
        txBoiteNuit= (TextView) rootView.findViewById(R.id.txBoite);


        txNomSoiree= (TextView) rootView.findViewById(R.id.txNomSoiree);
        txDescriptif= (TextView) rootView.findViewById(R.id.txDescriptif);
        txHoraireSoiree= (TextView) rootView.findViewById(R.id.txHoraire);
        txAdresse= (TextView) rootView.findViewById(R.id.txAdresse);
        txDateFermeture= (TextView) rootView.findViewById(R.id.txFermetureExceptionnel);
        txLienSite= (TextView) rootView.findViewById(R.id.txLienSite);
        txTypeSoiree= (TextView) rootView.findViewById(R.id.txTypeSoiree);

        txDebit = (TextView) rootView.findViewById(R.id.txdebitalcool);
        txDateDebit = (TextView) rootView.findViewById(R.id.txdatedebit);
        txHoraireDebit = (TextView) rootView.findViewById(R.id.txhorairedebit);

        txFetes.setTypeface(face);
        txDate.setTypeface(face);
        txHoraire.setTypeface(face);
        txConcert.setTypeface(face);
        txBar.setTypeface(face);
        txBarNuit.setTypeface(face);
        txAutres.setTypeface(face);
        txBoiteNuit.setTypeface(face);

        txNomSoiree.setTypeface(face);
        txDescriptif.setTypeface(face);
        txHoraireSoiree.setTypeface(face);
        txAdresse.setTypeface(face);
        txDateFermeture.setTypeface(face);
        txLienSite.setTypeface(face);
        txTypeSoiree.setTypeface(face);

        txDebit.setTypeface(face);
        txDateDebit.setTypeface(face);
        txHoraireDebit.setTypeface(face);

        Valider.setTypeface(face);


        rlvFetesSoiree = (LinearLayout) rootView.findViewById(R.id.rlvSoiree);
        rlvMap= (RelativeLayout) rootView.findViewById(R.id.rlvMap);
        imLegAmis.setImageResource(R.drawable.amisgras);
        imLegKiffs.setImageResource(R.drawable.matchgras);
        imLegInconnu.setImageResource(R.drawable.inconnugras);
        imGeoloc.setImageResource(R.drawable.flechemap);



        imPhotoMap.setVisibility(View.INVISIBLE);

        User = FragmentsSliderActivity.User;

        if(User.bfetes){
            btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bconcert){
            btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bbarnuit){
            btbarnuit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bbar){
            btbar.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bboitenuit){
            btboitenuit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bautre){
            btautres.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        if(User.bdebit){
            btDebit.setBackgroundColor(Color.parseColor("#2c2954"));
        }

        txHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txHoraire.setText("horaire : " + hourOfDay + ":" + minute);
                                User.horaire=hourOfDay + ":" + minute;
                                User.save();
                            }
                        }, hour, minute, true);

                timePickerDialog.show();

            }

        });

        txHoraireDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar = Calendar.getInstance();

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txHoraireDebit.setText("horaire : " + hourOfDay + ":" + minute);
                                User.horairedebit=hourOfDay + ":" + minute;
                                User.save();
                            }
                        }, hour, minute, true);

                timePickerDialog.show();

            }

        });

        txDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCalendar = Calendar.getInstance();

                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_YEAR);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txDate.setText("date : " + i2 + "/" + i1 + "/" + i);
                        User.date= i + "-" + i1 + "-" + i2;
                        User.save();
                    }
                },year,month,day);
                datePicker.show();


            }
        });

        txDateDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCalendar = Calendar.getInstance();

                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_YEAR);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txDateDebit.setText("date : " + i2 + "/" + i1 + "/" + i);
                        User.datedebit=i + "-" + i1 + "-" + i2;
                        User.save();
                    }
                },year,month,day);
                datePicker.show();


            }
        });




        ValiderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnDetailSoiree.setVisibility(View.GONE);
            }
        });

        imSoiree.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                impopupmap.setVisibility(View.INVISIBLE);
                rlvFetesSoiree.setVisibility(View.VISIBLE);
                User.popupmap=1;
                User.save();
                WebService WS = new WebService(getContext());
                WS.SaveUser(User);
                cliclsoiree=true;
                return false;
            }
        });

        imSoiree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cliclsoiree){
                    cliclsoiree=false;
                    imSoiree.setImageResource(R.drawable.boutonsoiree);
                    if(User!=null) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        // For dropping a marker at a point on the Map
                        LatLng moi = new LatLng(latitude, longitude);

                        MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                        googleMap.addMarker(MaPosition);
                        WebService WS = new WebService(getContext());
                        JSONArray ListPositions;

                        ListPositions = WS.GetSoirees(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                                marker.setTag(Position.getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }else{
                    cliclsoiree=true;
                    imSoiree.setImageResource(R.drawable.soireebarre);

                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);

                    if(User!=null) {
                        WebService WS = new WebService(getContext());
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                    marker.setTag("0");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (clicKiff == false) {
                            ListPositions = WS.GetLastPositionUserMatch(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (clicAmis == false) {
                            ListPositions = WS.GetLastPositionUserAmis(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                }
            }
        });

        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlvFetesSoiree.setVisibility(View.INVISIBLE);
            }
        });

        btFetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bfetes)
                {
                    btFetes.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bfetes=false;

                }else{
                    btFetes.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bfetes=true;
                }

                User.save();

            }
        });

        btDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bdebit)
                {
                    btDebit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bdebit=false;

                }else{
                    btDebit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bdebit=true;
                }

                User.save();
            }
        });

        btconcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bconcert)
                {
                    btconcert.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bconcert=false;

                }else{
                    btconcert.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bconcert=true;
                }

                User.save();
            }
        });

        btbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bbar)
                {
                    btbar.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbar=false;

                }else{
                    btbar.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbar=true;
                }

                User.save();
            }
        });

        btboitenuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bboitenuit)
                {
                    btboitenuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bboitenuit=false;

                }else{
                    btboitenuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bboitenuit=true;
                }

                User.save();
            }
        });

        btbarnuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bbarnuit)
                {
                    btbarnuit.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bbarnuit=false;

                }else{
                    btbarnuit.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bbarnuit=true;
                }

                User.save();
            }
        });

        btautres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.bautre)
                {
                    btautres.setBackgroundColor(Color.parseColor("#d2d2db"));
                    User.bautre=false;

                }else{
                    btautres.setBackgroundColor(Color.parseColor("#2c2954"));
                    User.bautre=true;
                }

                User.save();
            }
        });

        imGeoloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    googleMap.addCircle(new CircleOptions()
                            .center(moi)
                            .radius(300)
                            .strokeWidth(2)
                            .strokeColor(Color.rgb(41,41,84))
                            .fillColor(Color.TRANSPARENT));


                }
            }
        });


        imFlecheMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });


        imLegAmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clicAmis == false) {
                    imLegAmis.setImageResource(R.drawable.amisgrasligne);
                    clicAmis = true;

                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);
                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);

                    if (User != null) {
                        WebService WS = new WebService(getContext());
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                    marker.setTag("0");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (clicKiff == false) {
                            ListPositions = WS.GetLastPositionUserMatch(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (clicAmis == false) {
                            ListPositions = WS.GetLastPositionUserAmis(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }

                } else {
                    imLegAmis.setImageResource(R.drawable.amisgras);
                    clicAmis = false;
                    double latitude;
                    double longitude;
                    WebService WS = new WebService(getContext());
                    JSONArray ListPositions = WS.GetLastPositionUserAmis(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                            marker.setTag(Position.getString("id_user"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        imLegKiffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicKiff==false){
                    imLegKiffs.setImageResource(R.drawable.matchgrasligne);
                    clicKiff=true;

                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);

                    if(User!=null) {
                        WebService WS = new WebService(getContext());
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                    marker.setTag("0");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (clicKiff == false) {
                            ListPositions = WS.GetLastPositionUserMatch(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (clicAmis == false) {
                            ListPositions = WS.GetLastPositionUserAmis(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                    marker.setTag(Position.getString("id_user"));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    }
                }else{
                    imLegKiffs.setImageResource(R.drawable.matchgras);
                    clicKiff=false;

                    double latitude;
                    double longitude;
                    WebService WS = new WebService(getContext());
                    JSONArray ListPositions = WS.GetLastPositionUserMatch(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                            marker.setTag(Position.getString("id_user"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }
        });



        imLegInconnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicInconnu==false){
                    imLegInconnu.setImageResource(R.drawable.inconnugrasligne);
                    clicInconnu=true;


                    googleMap.clear();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);

                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);

                    if(User!=null) {
                        WebService WS = new WebService(getContext());
                        JSONArray ListPositions;
                        if (clicInconnu == false) {
                            ListPositions = WS.GetLastPositionUserInconnu(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                    marker.setTag("0");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        if (clicKiff == false) {
                            ListPositions = WS.GetLastPositionUserMatch(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        if (clicAmis == false) {
                            ListPositions = WS.GetLastPositionUserAmis(User);

                            for (int i = 0; i < ListPositions.length(); i++) {
                                JSONObject Position = null;

                                try {
                                    Position = ListPositions.getJSONObject(i);

                                    latitude = Position.getDouble("latitude");
                                    longitude = Position.getDouble("longitude");

                                    LatLng user = new LatLng(latitude, longitude);

                                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                    marker.setTag(Position.getString("id_user"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                }else{
                    imLegInconnu.setImageResource(R.drawable.inconnugras);
                    clicInconnu=false;

                    double latitude;
                    double longitude;
                    WebService WS = new WebService(getContext());
                    JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                            marker.setTag("0");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
                //imLegInconnu.setImageResource(R.drawable.legendeinconnusbarre);
            }
        });



        //sleep for 1s in background...
        gps = new GPSTracker(rootView.getContext());

        if(gps.canGetLocation==false){
            gps.showSettingsAlert();

        }

        pref = getActivity().getSharedPreferences("EASER", getActivity().MODE_PRIVATE);

        editor = pref.edit();
        //User = Utilisateur.findById(Utilisateur.class,pref.getLong("UserId", 0));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        MyLocationListener locationListener = new MyLocationListener();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //googleMap.setMyLocationEnabled(true);

                // check if GPS enabled
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // For dropping a marker at a point on the Map
                    LatLng moi = new LatLng(latitude, longitude);


                    MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));


                    googleMap.addMarker(MaPosition);

                    if(User!=null){
                        WebService WS = new WebService(getContext());


                        JSONArray ListSoirees = WS.GetSoirees(User);

                        for (int i = 0; i < ListSoirees.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListSoirees.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                                marker.setTag(Position.getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                                marker.setTag("0");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ListPositions = WS.GetLastPositionUserMatch(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);


                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                                marker.setTag(Position.getString("id_user"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        ListPositions = WS.GetLastPositionUserAmis(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                                marker.setTag(Position.getString("id_user"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }


                    // For zooming automatically to the location of the marker
                    if(FragmentsSliderActivity.Localiser){
                        // For dropping a marker at a point on the Map
                        moi = new LatLng(Double.parseDouble(FragmentsSliderActivity.latitude),Double.parseDouble(FragmentsSliderActivity.longitude));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }else{
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(moi).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                }

                imPhotoMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(),Conversation.class);
                        i.putExtra("id_kiffs",(String) imPhotoMap.getTag());
                        i.putExtra("id_user",User.id_user);
                        i.putExtra("prive",1);
                        startActivity(i);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if(marker.getTitle().toString().equals("soirée")){
                            WebService WS = new WebService(getContext());

                            JSONArray Soiree = WS.GetInfoSoiree(marker.getTag().toString());

                            String photo ="";
                            if(Soiree!=null && Soiree.length()>0){

                                try {
                                    txNomSoiree.setText("Nom : " + Soiree.getJSONObject(0).getString("nomsoiree"));
                                    txAdresse.setText(Soiree.getJSONObject(0).getString("adresse"));
                                    txHoraireSoiree.setText("Horaire d'ouverture : De " + Soiree.getJSONObject(0).getString("horairedebut") + " à " + Soiree.getJSONObject(0).getString("horairefin"));
                                    txDescriptif.setText(Soiree.getJSONObject(0).getString("descriptif"));
                                    txLienSite.setText("Lien du site : " + Soiree.getJSONObject(0).getString("liensite"));

                                    if(Soiree.getJSONObject(0).getString("typedebit").equals("1")){
                                        txLienSite.setText("Choix de débit : " + Soiree.getJSONObject(0).getString("choixdebit"));
                                        txTypeSoiree.setVisibility(View.GONE);
                                    }else{
                                        switch (Soiree.getJSONObject(0).getString("typesoiree")) {
                                            case "1":
                                                txTypeSoiree.setText("Type soirée : Bar");
                                                break;

                                            case "2":
                                                txTypeSoiree.setText("Type soirée : Bar de nuit");
                                                break;

                                            case "3":
                                                txTypeSoiree.setText("Type soirée : Boîte de nuit");
                                                break;

                                            case "4":
                                                txTypeSoiree.setText("Type soirée : Concert");
                                                break;


                                        }

                                    }
                                    lnDetailSoiree.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else{
                            WebService WS = new WebService(getContext());

                            JSONArray UserList = WS.GetinfoUser(marker.getTag().toString());

                            String photo ="";
                            if(UserList!=null && UserList.length()>0){
                                try {
                                    photo = UserList.getJSONObject(0).getString("photo");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Picasso.with(getContext()).load(photo).resize(300,300).into(imPhotoMap);
                                //imPhotoMap.setImageBitmap(bitmap);
                                try {
                                    imPhotoMap.setTag(UserList.getJSONObject(0).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                imPhotoMap.setVisibility(View.VISIBLE);
                            }else{
                                imPhotoMap.setVisibility(View.INVISIBLE);
                            }
                        }


                        //Using position get Value from arraylist
                        return false;
                    }
                });



            }
        });


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }





        refreshMap = new RefreshMap();
        refreshMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        return rootView;

    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {

            User.nbaffichemap = User.nbaffichemap+1;

            if(User.nbaffichemap==3 && User.popupmap==0){
                impopupmap.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // For dropping a marker at a point on the Map
        LatLng moi = new LatLng(location.getLatitude(),location.getLongitude());

        googleMap.clear();
        double latitude = 0;
        double longitude = 0;
        MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));

        googleMap.addMarker(MaPosition);

        if(User!=null){
            WebService WS = new WebService(getContext());


            JSONArray ListSoirees = WS.GetSoirees(User);

            for (int i = 0; i < ListSoirees.length(); i++) {
                JSONObject Position = null;

                try {
                    Position = ListSoirees.getJSONObject(i);

                    latitude = Position.getDouble("latitude");
                    longitude = Position.getDouble("longitude");

                    LatLng user = new LatLng(latitude, longitude);

                    marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                    marker.setTag(Position.getString("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

            for (int i = 0; i < ListPositions.length(); i++) {
                JSONObject Position = null;

                try {
                    Position = ListPositions.getJSONObject(i);

                    latitude = Position.getDouble("latitude");
                    longitude = Position.getDouble("longitude");

                    LatLng user = new LatLng(latitude, longitude);

                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                    marker.setTag("0");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ListPositions = WS.GetLastPositionUserMatch(User);

            for (int i = 0; i < ListPositions.length(); i++) {
                JSONObject Position = null;

                try {
                    Position = ListPositions.getJSONObject(i);

                    latitude = Position.getDouble("latitude");
                    longitude = Position.getDouble("longitude");

                    LatLng user = new LatLng(latitude, longitude);

                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                    marker.setTag(Position.getString("id_user"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ListPositions = WS.GetLastPositionUserAmis(User);

            for (int i = 0; i < ListPositions.length(); i++) {
                JSONObject Position = null;

                try {
                    Position = ListPositions.getJSONObject(i);

                    latitude = Position.getDouble("latitude");
                    longitude = Position.getDouble("longitude");

                    LatLng user = new LatLng(latitude, longitude);

                    marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                    marker.setTag(Position.getString("id_user"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class RefreshMap extends AsyncTask {



        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            googleMap.clear();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // For dropping a marker at a point on the Map
            LatLng moi = new LatLng(latitude, longitude);

            googleMap.addMarker(new MarkerOptions().position(moi).title("Ma Position").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi)));

            googleMap.addCircle(new CircleOptions()
                    .center(moi)
                    .radius(300)
                    .strokeWidth(2)
                    .strokeColor(Color.rgb(41,41,84))
                    .fillColor(Color.TRANSPARENT));

            if (User != null) {
                WebService WS = new WebService(getContext());
                JSONArray ListPositions;

                if (cliclsoiree == false) {
                    ListPositions = WS.GetSoirees(User);

                        for (int i = 0; i < ListPositions.length(); i++) {
                            JSONObject Position = null;

                            try {
                                Position = ListPositions.getJSONObject(i);

                                latitude = Position.getDouble("latitude");
                                longitude = Position.getDouble("longitude");

                                LatLng user = new LatLng(latitude, longitude);

                                marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                                marker.setTag(Position.getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                }

                if (clicInconnu == false) {
                    ListPositions = WS.GetLastPositionUserInconnu(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                            marker.setTag("0");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                if (clicKiff == false) {
                    ListPositions = WS.GetLastPositionUserMatch(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                            marker.setTag(Position.getString("id_user"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

                if (clicAmis == false) {
                    ListPositions = WS.GetLastPositionUserAmis(User);

                    for (int i = 0; i < ListPositions.length(); i++) {
                        JSONObject Position = null;

                        try {
                            Position = ListPositions.getJSONObject(i);

                            latitude = Position.getDouble("latitude");
                            longitude = Position.getDouble("longitude");

                            LatLng user = new LatLng(latitude, longitude);

                            marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                            marker.setTag(Position.getString("id_user"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }


            }

        }

        @Override
        protected Void doInBackground(Object... params) {
            if(User!=null){

                while(true){
                    try {
                        Thread.sleep(10000);

                        publishProgress(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);



        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // For dropping a marker at a point on the Map
            LatLng moi = new LatLng(location.getLatitude(),location.getLongitude());

            googleMap.clear();
            double latitude = 0;
            double longitude = 0;
            MaPosition = new MarkerOptions().position(moi).title("Ma Position").snippet("Moi").icon(BitmapDescriptorFactory.fromResource(R.drawable.goutemoi));

            googleMap.addMarker(MaPosition);

            if(User!=null){
                WebService WS = new WebService(getContext());


                JSONArray ListSoirees = WS.GetSoirees(User);

                for (int i = 0; i < ListSoirees.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListSoirees.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        marker = googleMap.addMarker(new MarkerOptions().position(user).title("soirée").icon(BitmapDescriptorFactory.fromResource(R.drawable.bouteillesoiree)));
                        marker.setTag(Position.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                JSONArray ListPositions = WS.GetLastPositionUserInconnu(User);

                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouterose)));
                        marker.setTag(Position.getString("0"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                ListPositions = WS.GetLastPositionUserMatch(User);

                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.gouteverte)));
                        marker.setTag(Position.getString("id_user"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                ListPositions = WS.GetLastPositionUserAmis(User);

                for (int i = 0; i < ListPositions.length(); i++) {
                    JSONObject Position = null;

                    try {
                        Position = ListPositions.getJSONObject(i);

                        latitude = Position.getDouble("latitude");
                        longitude = Position.getDouble("longitude");

                        LatLng user = new LatLng(latitude, longitude);

                        marker = googleMap.addMarker(new MarkerOptions().position(user).title(Position.getString("pseudo")).icon(BitmapDescriptorFactory.fromResource(R.drawable.goutebleu)));
                        marker.setTag(Position.getString("id_user"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


        }

        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String s) {

        }

        public void onProviderDisabled(String s) {

        }
    }
}


