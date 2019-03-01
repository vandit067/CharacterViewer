package com.sample.characterviewer.api

import android.content.Context

/**
 * Class to retrieve api client and access the retrofit calls.
 */
class ApiServices
/**
 * Constructor to create [ApiInterface]
 *
 * @param appContext application context
 */
private constructor(appContext: Context) {
    /**
     * return instance of [ApiInterface] to access the requests
     *
     * @return instance of [ApiInterface]
     */
    val apiInterface: ApiInterface

    init {
        this.apiInterface = ApiClient.getInstance(appContext).client.create(ApiInterface::class.java!!)
    }

    companion object {
        @Volatile
        private var mApiServices: ApiServices? = null

        /**
         * Create single instance of [ApiServices] class
         *
         * @param appContext application context
         * @return ApiServices instance of [ApiServices]
         */
        fun getInstance(appContext: Context): ApiServices? {
            if (mApiServices == null) {
                synchronized(ApiServices::class.java) {
                    if (mApiServices == null) {
                        mApiServices = ApiServices(appContext)
                    }
                }
            }
            return mApiServices
        }
    }
}
