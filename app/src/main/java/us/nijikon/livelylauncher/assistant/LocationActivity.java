package us.nijikon.livelylauncher.assistant;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import us.nijikon.livelylauncher.R;

public class LocationActivity extends FragmentActivity {
    private Location location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Bundle bundle = getIntent().getExtras();
        location = bundle.getParcelable("LOCATION");
        Log.d("Location", location.getLatitude() + "|" + location.getLongitude());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).
                getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                initiateMap(googleMap, location);
            }
        });
        // Initialize map options. For example:
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void initiateMap(GoogleMap googleMap, Location location) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getMyLocation();
        googleMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
    }
}
