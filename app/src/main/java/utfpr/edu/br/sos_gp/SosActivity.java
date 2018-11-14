package utfpr.edu.br.sos_gp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SosActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitude;
    private double longitude;


    public SosActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //Ao clicar no botao sos, o usuario envia sua localizaçao par ao banco e vai para a tela de menu de ajuda
        final ImageView btnSos = findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                final String id_user = String.valueOf(preferences.getInt("id_user", 0));

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkUserLocationPermission();
                    return;
                }

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location == null){
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                longitude = location.getLongitude();
                latitude = location.getLatitude();

                final LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                };

                String URL = Constants.IP_ADDRESS + "insert_location.php";

                //Biblioteca Ion para chamada HTTP
                Ion.with(getBaseContext())
                        .load(URL)
                        .setBodyParameter("id_user", id_user)
                        .setBodyParameter("latitude", String.valueOf(latitude))
                        .setBodyParameter("longitude", String.valueOf(longitude))
                        //recebe objeto do tipo json
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (result.get("retorno").getAsString().equals("YES")) {
                                    Toast.makeText(getBaseContext(), "Sucesso", Toast.LENGTH_LONG).show();
                                } else if (result.get("retorno").getAsString().equals("NO")) {
                                    Toast.makeText(getBaseContext(), "NO", Toast.LENGTH_LONG).show();
                                } else if (result.get("retorno").getAsString().equals("LOCATION_ERROR")) {
                                    Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                Intent it = new Intent(SosActivity.this, MenuActivity.class);
                startActivity(it);




                //Toast.makeText(getBaseContext(), ":(", Toast.LENGTH_LONG).show();

            }

        });


        //esse botao vai para a tela de anamnese
        ImageView btnAnamnese = findViewById(R.id.btnAnamnese);

        btnAnamnese.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(SosActivity.this, ProfileActivity.class);
                startActivity(it);
            }
        });


    }

    public boolean checkUserLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

}
