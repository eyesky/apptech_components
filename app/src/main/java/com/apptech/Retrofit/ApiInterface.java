package com.apptech.Retrofit;

import com.apptech.ComplexRecyclerView.Model.Model;
import com.apptech.RecyclerviewSearch.RecyclerViewSearchModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("json/movies.json")
    Call<List<Model>> getInbox();

    @GET("json/contacts.json")
    Call<List<RecyclerViewSearchModel>> getSearchData();

}

