package nyc.c4q.capstone.finder;


import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nyc.c4q.capstone.R;
import nyc.c4q.capstone.models.DBReturnCampaignModel;

import static nyc.c4q.capstone.MainActivity.firebaseDataHelper;


/**
 * A simple {@link Fragment} subclass.
 */

public class FinderFragment extends Fragment implements OnMapReadyCallback, ViewPager.OnPageChangeListener, ValueEventListener, LocationSource {

    private View rootView;
    private MapView mapView;
    private GoogleMap myGoogleMap;
    private HashMap<MarkerOptions, DBReturnCampaignModel> campaignHashMap = new HashMap<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private static final String TAG = "FUSEDLOCATIONPROVIDER?";

    public FinderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.finder_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        centerMapOnMyLocation();
        firebaseDataHelper.getCampaignDatbaseRefrence().addValueEventListener(this);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    private void centerMapOnMyLocation() {
//        myGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = rootView.findViewById(R.id.finder_map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;
        myGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        myGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        myGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        myGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        myGoogleMap.setMyLocationEnabled(true);
        myGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        getDeviceLocation();
        for (Map.Entry<MarkerOptions, DBReturnCampaignModel> entry : campaignHashMap.entrySet()) {
            MarkerOptions marker = entry.getKey();
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Marker mapMarker = myGoogleMap.addMarker(marker);
                if(lastKnownLocation != null) {
                    Log.d(TAG, "onComplete: " + lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (SphericalUtil.computeDistanceBetween(marker.getPosition(), latLng) <= 5000) {
                    mapMarker.setVisible(true);
                } else {
                    mapMarker.setVisible(false);
                }
            }

        }
    }

    private void getDeviceLocation() {
        final Task locationResult = fusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    lastKnownLocation = (Location) task.getResult();
                    Log.d(TAG, "onComplete: " + lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());
                    myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 13));
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        setMapMarkers(firebaseDataHelper.getCampaignsList(dataSnapshot, ""));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setMapMarkers(List<DBReturnCampaignModel> campaignModels) {
        for (DBReturnCampaignModel dbReturnCampaignModel : campaignModels) {
            LatLng currentLocation = LocationHelper.getLocationFromAddress(getActivity(), dbReturnCampaignModel.getAddress());
            MarkerOptions marker = new MarkerOptions().position(new LatLng(currentLocation.latitude, currentLocation.longitude));
            campaignHashMap.put(marker, dbReturnCampaignModel);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
