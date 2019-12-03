package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.TOKEN_HEADER_NAME
import com.epam.epmrduacmvan.UrlConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance private constructor(){

    companion object INSTANCE {
        private val client by lazy {
            OkHttpClient.Builder().addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request = chain.request()
                                                .newBuilder()
                                                .addHeader(TOKEN_HEADER_NAME, AppApplication.token)
                                                .build()
                    return chain.proceed(request)
                }}).build()
        }

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}