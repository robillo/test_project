package com.robillo.test_project.network.retrofit;

import com.robillo.test_project.network.model.AllDetails;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by robinkamboj on 12/11/17.
 */

public interface ApiInterface {

    @GET("jsonparsetutorial.txt")
    Call<AllDetails> getWorldDetails();
}
