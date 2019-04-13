package com.example.android.redhatambulance;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    View root_view;
    Location currLocation;


    FusedLocationProviderClient fusedLocationClient;



    public DashboardFragment() {
        // Required empty public constructor
    }

    private void book() {

        JSONObject obj=null;
        try{
            obj=new JSONObject();
            obj.put("action","requestAmbulance");
            obj.put("latitude",currLocation.getLatitude());
            obj.put("longitude",currLocation.getLongitude());
            obj.put("email","a");
            obj.put("forWhom","self");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        UserSingleton.get().ws.send(obj.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view= inflater.inflate(R.layout.fragment_dashboard,container,false);
        Button bookBtn = root_view.findViewById(R.id.bookingUser);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wow","booking");
                book();
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return root_view;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currLocation = location;
                            Toast.makeText(getActivity(), String.valueOf(currLocation.getLatitude()), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        return root_view;


    }







}
