package com.robillo.test_project.view.main.list_fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robillo.test_project.R;
import com.robillo.test_project.network.model.AllDetails;
import com.robillo.test_project.network.retrofit.ApiClient;
import com.robillo.test_project.network.retrofit.ApiInterface;
import com.robillo.test_project.view.main.list_fragment.list_adapter.WorldListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements ListFragmentMvpView {

    private WorldListAdapter mAdapter;
    private ApiInterface mApiService;
    private RecyclerView mRecyclerView;
    private TextView mRetry;
    private ProgressBar mLoading;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        setUp(v);

        return v;
    }

    @Override
    public void setUp(View v) {
        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mRetry = v.findViewById(R.id.retry);
        mLoading = v.findViewById(R.id.progress_bar_loading);
        mRecyclerView = v.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new WorldListAdapter(null, getActivity().getApplicationContext()));
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });
        retry();
    }

    @Override
    public void retry() {
        showLoading();
        callForWorldDetails();
    }

    @Override
    public void callForWorldDetails() {
        Call<AllDetails> call = mApiService.getWorldDetails();
        if (call != null) {
            call.enqueue(new Callback<AllDetails>() {
                @Override
                public void onResponse(@NonNull Call<AllDetails> call, @NonNull Response<AllDetails> response) {
                    if (response.body() != null && response.code() == 200) {
                        AllDetails details = response.body();
                        //noinspection ConstantConditions
                        mAdapter = new WorldListAdapter(details.getWorldpopulation(),
                                ListFragment.this.getActivity());
                        if (mRecyclerView != null) mRecyclerView.setAdapter(mAdapter);
                        if(mAdapter.getItemCount()>0) showRecycler();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllDetails> call, @NonNull Throwable t) {
                    showRetry();
                }
            });
        }
    }

    @Override
    public void showLoading() {
        if(mRecyclerView!=null) mRecyclerView.setVisibility(View.INVISIBLE);
        if(mLoading!=null) mLoading.setVisibility(View.VISIBLE);
        if(mRetry!=null) mRetry.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRetry() {
        if(mRecyclerView!=null) mRecyclerView.setVisibility(View.INVISIBLE);
        if(mLoading!=null) mLoading.setVisibility(View.INVISIBLE);
        if(mRetry!=null) mRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecycler() {
        if(mRecyclerView!=null) mRecyclerView.setVisibility(View.VISIBLE);
        if(mLoading!=null) mLoading.setVisibility(View.INVISIBLE);
        if(mRetry!=null) mRetry.setVisibility(View.INVISIBLE);
    }
}