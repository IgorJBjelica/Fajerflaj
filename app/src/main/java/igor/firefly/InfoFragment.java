package igor.firefly;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InfoFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private GoogleMap mMap;
    private String provider;
    private Marker home;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Event event;
    private Location startLocation;
    private Location endLocation;
    private Location currentLocation;
    private boolean hasDirection = false;
    private RequestQueue requestQueue;

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
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("InfoFragment", "onLocationChanged");
        currentLocation = location;
        if (startLocation == null) {
            startLocation = location;
        }
        if (mMap != null) {
            addMarker(location);
        }
        showDirection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getActivity());
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        Bundle bundle = this.getActivity().getIntent().getExtras();
        event = bundle.getParcelable("event");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("InfoFragment", "onMapReady");
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
            String locUrl = Util.getLocationUrl(event.getAddress() + ", Belgrade, Serbia");
            Log.d("InfoFragment", "locUrl=" + locUrl);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, locUrl, null, locationResponseListener, locationErrorResponseListener);
            requestQueue.add(jsObjRequest);
        }
    }

    private void addMarker(Location location) {
        Log.d("InfoFragment:addMarker", location + "");
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (home != null) {
            home.remove();
        }

        home = mMap.addMarker(new MarkerOptions()
                .title("This is you")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        home.setFlat(true);
        home.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private Response.Listener<JSONObject> locationResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("InfoFragment", "locationResponseListener");
            LatLng addressLocation = Util.parseLocationResponse(response);
            Log.d("InfoFragment", "addressLocation=" + addressLocation);

            EventsHelper db = new EventsHelper(getContext());
            if (event.getLatitude() == 0 && event.getLongitude() == 0)
                db.updateEventLatLng(event.getId(), addressLocation.latitude, addressLocation.longitude);
            else
                Log.d("LatLng", "Did not update Latitude and Longitude in the database, because values already exist.");

            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().title(event.getName()).position(addressLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 15));
                endLocation = new Location(provider);
                endLocation.setLatitude(addressLocation.latitude);
                endLocation.setLongitude(addressLocation.longitude);
            }
            showDirection();
        }
    };

    private void showDirection() {
        if (startLocation != null && endLocation != null && !hasDirection) {
            // get directions
            String dirUrl = Util.getDirectionsUrl(startLocation, endLocation);
            Log.d("InfoFragment", "dirUrl=" + dirUrl);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, dirUrl, null, directionResponseListener, directionErrorResponseListener);
            requestQueue.add(jsObjRequest);
        }
    }


    private Response.ErrorListener locationErrorResponseListener = new Response.ErrorListener() {
        @Override
        // Handles errors that occur due to Volley
        public void onErrorResponse(VolleyError error) {
            Log.e("InfoFragment", "locationErrorResponseListener" + error);

        }
    };

    private Response.Listener<JSONObject> directionResponseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("InfoFragment", "directionResponseListener");
            if (mMap != null) {
                List<List<HashMap<String, String>>> result = Util.parse(response);
                if (!Util.isNullOrEmpty(result)) {
                    ArrayList<LatLng> points = null;
                    PolylineOptions lineOptions = null;
                    // Traversing through all the routes
                    for (List<HashMap<String, String>> path : result) {
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();
                        for (HashMap<String, String> point : path) {
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }
                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(5);
                        lineOptions.color(Color.RED);
                    }
                    // Drawing polyline in the Google Map for the i-th route
                    mMap.addPolyline(lineOptions);
                }
                hasDirection = true;
            }
        }
    };

    private Response.ErrorListener directionErrorResponseListener = new Response.ErrorListener() {
        @Override
        // Handles errors that occur due to Volley
        public void onErrorResponse(VolleyError error) {
            Log.e("InfoFragment", "directionErrorResponseListener" + error);

        }
    };
}