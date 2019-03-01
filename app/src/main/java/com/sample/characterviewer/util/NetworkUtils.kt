package com.sample.characterviewer.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.RemoteException
import com.sample.characterviewer.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * This class is use for perform network related operations.
 */
object NetworkUtils {

    private val SOCKET_TIMEOUT_EXCEPTION_CODE = 3000
    private val UNKNOWN_HOST_EXCEPTION_CODE = 3001
    private val IO_EXCEPTION_CODE = 3002
    private val REMOTE_EXCEPTION_CODE = 3003
    private val CONNECTION_TIMEOUT_EXCEPTION_CODE = 3004
    private val UNKNOWN_ERROR_CODE = 3005

    /**
     * Check network is available or not.
     *
     * @param context application context
     * @return boolean is network available or not.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * @param e throwable instance to find the exception
     * @return error code.
     */
    private fun getErrorCode(e: Throwable): Int {
        if (e is HttpException) {
            return e.code()
        } else if (e is IOException) {
            return if (e is SocketTimeoutException) {
                SOCKET_TIMEOUT_EXCEPTION_CODE
            } else if (e is UnknownHostException) {
                UNKNOWN_HOST_EXCEPTION_CODE
            } else {
                IO_EXCEPTION_CODE
            }
        } else if (e is RemoteException) {
            return REMOTE_EXCEPTION_CODE
        } else if (e is Exception) {
            if (e is TimeoutException) {
                return CONNECTION_TIMEOUT_EXCEPTION_CODE
            }
        }
        return UNKNOWN_ERROR_CODE

    }

    /**
     * Get local message based on the [Throwable]
     *
     * @param context application context
     * @param e       throwable
     * @return error message to display in UI
     */
    fun getApiErrorMessage(context: Context, e: Throwable): String {
        return getErrorBasedOnErrorCode(context, getErrorCode(e))
    }

    /**
     * Get error based on error code.
     *
     * @param context   application context
     * @param errorCode error code.
     * @return error message based on error code.
     */
    private fun getErrorBasedOnErrorCode(context: Context, errorCode: Int): String {
        when (errorCode) {
            SOCKET_TIMEOUT_EXCEPTION_CODE -> return context.getString(R.string.error_request_timeout)
            UNKNOWN_HOST_EXCEPTION_CODE -> return context.getString(R.string.error_network_check)
            IO_EXCEPTION_CODE -> return context.getString(R.string.error_io)
            REMOTE_EXCEPTION_CODE -> return context.getString(R.string.error_remote_exception)
            CONNECTION_TIMEOUT_EXCEPTION_CODE -> return context.getString(R.string.error_connection_timeout)
            UNKNOWN_ERROR_CODE -> return context.resources.getString(R.string.error_went_wrong)
            else -> return getHttpErrorBasedOnErrorCode(context, errorCode)
        }
    }

    /**
     * Retrieve http error message based on error code.
     *
     * @param context   application context
     * @param errorCode error code.
     * @return error message based on http error code
     */
    private fun getHttpErrorBasedOnErrorCode(context: Context, errorCode: Int): String {
        return if (isError4xx(errorCode)) {
            if (errorCode == 404) {
                context.getString(R.string.error_404)
            } else {
                context.resources.getString(R.string.error_unknown)
            }
        } else if (isError5xx(errorCode)) {
            context.getString(R.string.error_server_unable_to_process_request)
        } else {
            context.resources.getString(R.string.error_went_wrong)
        }
    }

    /**
     * Check error code is in the range of 400 to 499.
     *
     * @param errorCode error code
     * @return true if error code in range else return false
     */
    private fun isError4xx(errorCode: Int): Boolean {
        return errorCode >= 400 && errorCode <= 499
    }

    /**
     * Check error code is in the range of 500 to 599.
     *
     * @param errorCode error code
     * @return true if error code in range else return false
     */
    private fun isError5xx(errorCode: Int): Boolean {
        return errorCode >= 500 && errorCode <= 599
    }

}
