package com.example.travelmate.NearByPlaces;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.travelmate.APIS.DistanceApiHitter;
import com.example.travelmate.APIS.NearbyApiHitter;
import com.example.travelmate.Adapter.NearByAtmAdapter;
import com.example.travelmate.Distance.Distance;
import com.example.travelmate.NearByAtm.NearByAtm;
import com.example.travelmate.NearByAtm.Result;
import com.example.travelmate.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetrolStationFragment extends Fragment {
    ProgressDialog progressDialog;
    String KEY = "AIzaSyCOggg7f0D3iWZOQSLOKbo0BWrbQ9Y6ymw";
    String latlong = "30.7046,76.7179";
    String RADIUS = "1000";
    List<Result> atm1;
    RecyclerView rvGasStation;
    String types = "gas_station";

    public PetrolStationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_petrol_station, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvGasStation = view.findViewById(R.id.rvGasStation);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("wait...");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvGasStation.setLayoutManager(manager);


        getDataFromApi();
    }

    private void getDataFromApi() {


        progressDialog.show();
        Call<NearByAtm> getPlaces = NearbyApiHitter.NearbyApiHitter().getPlaces(KEY, latlong, RADIUS, types);
        getPlaces.enqueue(new Callback<NearByAtm>() {
            @Override
            public void onResponse(Call<NearByAtm> call, Response<NearByAtm> response) {
                if (response.isSuccessful()) {
                    atm1 = response.body().getResults();
                    NearByAtmAdapter adapter = new NearByAtmAdapter(getContext(), atm1);
                    rvGasStation.setAdapter(adapter);
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<NearByAtm> call, Throwable t) {
                Log.e("message", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

}
