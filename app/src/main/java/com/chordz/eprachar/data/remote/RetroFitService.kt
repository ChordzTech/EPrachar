package com.chordz.eprachar.data.remote

import com.chordz.eprachar.data.ElectionDataHolder.BASE_URL
import com.chordz.eprachar.data.response.ElectionMessageResponse
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetroFitService {

    @GET("/getMessagebyCode/{a_contactno}/")
    suspend fun getdetailsListByContact(
        @Path(value = "a_contactno") a_contactno: String
    ): Response<ElectionMessageResponse>

//    @GET("adminDetails/{a_id}/")
//    suspend fun getdetailsList(
//        @Path(value = "a_id") a_id: Int
//    ): Response<ElectionMessageResponse>


    companion object {
        var retrofitService: RetroFitService? = null
        fun getInstance(): RetroFitService {

            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.addInterceptor(Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                chain.proceed(requestBuilder.build())
            })

            val gson = GsonBuilder().setLenient().create()
            if (retrofitService == null) {

                //This Below Url Has to be Replaced with Current Url
                // https://dsboxplus.dishaswaraj.in/api/

                val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getLogger())
                    .client(httpClientBuilder.build()).build()
                retrofitService = retrofit.create(RetroFitService::class.java)
            }
            return retrofitService!!
        }

        private fun getHeaders(): OkHttpClient {
            val httpClient = OkHttpClient()
            val interceptors = httpClient.networkInterceptors as ArrayList<Interceptor>
            interceptors.add(Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                chain.proceed(requestBuilder.build())
            })
            return httpClient;
        }

        private fun getLogger(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return OkHttpClient.Builder().addInterceptor(interceptor).build();
        }

    }
}