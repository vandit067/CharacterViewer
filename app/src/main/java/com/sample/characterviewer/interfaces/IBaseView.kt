package com.sample.characterviewer.interfaces

import android.view.View
import androidx.annotation.StringRes

/**
 * Class which have common ui functions to perform in most of the screen.
 */
interface IBaseView {

    val isNetworkConnected: Boolean
    fun showProgress(progressView: View, contentView: View)

    fun showContent(progressView: View, contentView: View)

    fun showErrorView(progressView: View, contentView: View, errorView: View)

    fun showError(message: String)

    fun showError(@StringRes resId: Int)

    fun showMessage(message: String)

    fun showMessage(@StringRes resId: Int)

    fun hideKeyboard()
}
