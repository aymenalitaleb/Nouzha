package esi.siw.nouzha;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    private MapView mapView;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(MapFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        return inflater.inflate(R.layout.fragment_map, container, false);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Activer le marque de la position actuelle
        Double latitude = ActivityDetails.latitude;
        Double longitude = ActivityDetails.longitude;
        String designation = ActivityDetails.designation;
//        Double latitude = 40D;
//        Double longitude = 40D;
//        String designation = "Designation";
        float zoomlevel = 10.0f;
        LatLng activityLocalisation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(activityLocalisation).title(designation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(activityLocalisation, zoomlevel));
    }


}





