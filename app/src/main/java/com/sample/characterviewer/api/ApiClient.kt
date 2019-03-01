package com.sample.characterviewer.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.sample.characterviewer.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Class to create retrofit client will be used in [ApiServices]
 */
class ApiClient
/**
 * Constructor to create api client instance only once.
 *
 * @param appContext application context
 */
private constructor(appContext: Context) {
    /**
     * Get instance of [Retrofit] api client
     *
     * @return Retrofit instance of [Retrofit]
     */
    val client: Retrofit

    init {
        this.client = this.createApiClient(appContext)
    }

    /**
     * Create api client to initiate api call.
     *
     * @param appContext application context
     * @return Retrofit instance of [Retrofit]
     */
    private fun createApiClient(appContext: Context): Retrofit {
        return Retrofit.Builder()
                .baseUrl(appContext.getString(R.string.base_url))
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }

    companion object {
        private const val CONNECTION_TIMEOUT = 30
        private const val READ_TIMEOUT = 30
        // Use volatile keyword to ensure that the Singleton is thread safe
        @Volatile
        private var ourInstance: ApiClient? = null

        /**
         * Use Double Check locking method. This means make the Singleton class in the synchronized block
         * only if the instance is null. So, the synchronized block will be executed only when the 'instance' is null
         * and prevent unnecessary synchronization once the instance variable is initialized.
         * Method to create single instance of [ApiClient]
         *
         * @param appContext application context
         * @return ApiClient instance of [ApiClient]
         */
        fun getInstance(appContext: Context): ApiClient {
            if (ourInstance == null) {
                synchronized(ApiClient::class.java) {
                    if (ourInstance == null) {
                        ourInstance = ApiClient(appContext)
                    }
                }
            }
            return ourInstance!!
        }

        /**
         * Create instance of [OkHttpClient] with basic information related to network.
         *
         * @return OkHttpClient instance of [OkHttpClient]
         */
        private val httpClient: OkHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .addInterceptor(LoggingInterceptor())
                    .build()
    }
}
