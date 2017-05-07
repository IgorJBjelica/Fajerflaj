package igor.firefly;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class InfoFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private GoogleMap mMap;
    private String provider;
    private Marker home;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    public InfoFragment() {
        super();
        getMapAsync(this);
    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        if (home != null) {
            home.remove();
        }
        if (mMap != null) {
            addMarker(location);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("InfoFragment","onMapReady");
        mMap = googleMap;
        if (googleMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Check Permissions Now
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // no permission, request permission
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            LatLng addressMekLatLng = new LatLng(44.810002,20.466493);
            mMap.addMarker(new MarkerOptions().title("Kineski restoran 88").position(addressMekLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressMekLatLng, 12.0f));
        }
    }

//    protected void getLatLng(String address) {
//        String uri = "http://maps.google.com/maps/api/geocode/json?address="
//                + address + "&sensor=false";
//
//        HttpGet httpGet = new HttpGet(uri);
//
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//
//            InputStream stream = entity.getContent();
//
//            int byteData;
//            while ((byteData = stream.read()) != -1) {
//                stringBuilder.append((char) byteData);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        double lat = 0.0, lng = 0.0;
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lng");
//            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lat");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void addMarker(Location location) {
        Log.d("InfoFragment:addMarker", location + "");
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (home != null) {
            home.remove();
        }

        home = mMap.addMarker(new MarkerOptions()
                .title("Nalazite se ovde")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        home.setFlat(true);
        home.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(12.0f).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0	&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
