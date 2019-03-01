package com.sample.characterviewer.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sample.characterviewer.R
import com.sample.characterviewer.interfaces.IBaseView
import com.sample.characterviewer.util.NetworkUtils
import com.sample.characterviewer.util.UiUtils

/**
 * Class which will be extend by all [android.app.Activity] of application which required to perform common operation specified here.
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {

    override val isNetworkConnected: Boolean
        @UiThread
        get() = NetworkUtils.isNetworkAvailable(applicationContext)

    /**
     * Get last stacked fragment from the fragment backstack.
     *
     * @return last fragment in backstack.
     */
    private val lastStackedFragment: Fragment?
        @UiThread
        get() {
            val fragmentManager = supportFragmentManager
            if (fragmentManager.backStackEntryCount > 0) {
                val entry = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
                return fragmentManager.findFragmentByTag(entry.name)
            }
            return fragmentManager.findFragmentById(R.id.activity_master_container)
        }

    protected abstract fun setUp(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp(savedInstanceState)
    }

    @UiThread
    private fun showSnackBar(message: String) {
        UiUtils.showSnackBar(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
    }

    @UiThread
    override fun showError(message: String) {
        showSnackBar(message)
    }

    @UiThread
    override fun showError(@StringRes resId: Int) {
        showError(getString(resId))
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @UiThread
    override fun showMessage(@StringRes resId: Int) {
        showMessage(getString(resId))
    }

    @UiThread
    override fun showContent(progressView: View, contentView: View) {
        progressView.visibility = View.GONE
        contentView.visibility = View.VISIBLE
    }

    @UiThread
    override fun showProgress(progressView: View, contentView: View) {
        progressView.visibility = View.VISIBLE
        contentView.visibility = View.GONE
    }

    @UiThread
    override fun showErrorView(progressView: View, contentView: View, errorView: View) {
        progressView.visibility = View.GONE
        contentView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
    }

    @UiThread
    override fun hideKeyboard() {
        val view = this.currentFocus ?: return
        UiUtils.hideKeyBoard(view)
    }

    /**
     * Add fragment in to backstack
     *
     * @param fragment         instance of fragment
     * @param backFragmentName fragment name to add in to backstack
     */
    @UiThread
    fun addFragment(fragment: Fragment, backFragmentName: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val lastStackFragment = lastStackedFragment ?: return
        fragmentTransaction.hide(lastStackFragment)
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.activity_master_container, fragment,
                fragment.javaClass.name).addToBackStack(backFragmentName).commit()
    }

}
