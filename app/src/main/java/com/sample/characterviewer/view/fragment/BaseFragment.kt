package com.sample.characterviewer.view.fragment

import android.content.Context
import android.view.View
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sample.characterviewer.interfaces.IBaseView
import com.sample.characterviewer.util.NetworkUtils
import com.sample.characterviewer.util.UiUtils
import com.sample.characterviewer.view.activity.BaseActivity

/**
 * Class which will be extend by all [Fragment] of application which required to perform common operation specified here.
 */
open class BaseFragment : Fragment(), IBaseView {

    @get:UiThread
    internal var baseActivity: BaseActivity? = null

    override val isNetworkConnected: Boolean
        @UiThread
        get() = if (this.baseActivity != null) {
            baseActivity!!.isNetworkConnected
        } else false


    @UiThread
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
        }
    }

    @UiThread
    override fun showError(message: String) {
        if (this.baseActivity != null) {
            baseActivity!!.showError(message)
        }
    }

    @UiThread
    override fun showError(resId: Int) {
        if (this.baseActivity != null) {
            baseActivity!!.showError(resId)
        }
    }

    @UiThread
    override fun showMessage(message: String) {
        if (this.baseActivity != null) {
            baseActivity!!.showMessage(message)
        }
    }

    @UiThread
    override fun showMessage(resId: Int) {
        if (this.baseActivity != null) {
            baseActivity!!.showMessage(resId)
        }
    }

    @UiThread
    override fun hideKeyboard() {
        if (this.baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
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
    internal fun addFragment(fragment: Fragment, backFragmentName: String) {
        if (this.baseActivity != null) {
            baseActivity!!.addFragment(fragment, backFragmentName)
        }
    }

    /**
     * Pop fragment from fragment stack
     */
    @UiThread
    internal fun popFragment() {
        if (fragmentManager == null) {
            return
        }
        fragmentManager!!.popBackStack()
    }


    /**
     * Set recyclerview item animation
     *
     * @param recyclerView instance of [RecyclerView]
     * @param animId       animation id.
     */
    @UiThread
    internal fun setRecyclerViewItemAnimation(recyclerView: RecyclerView, animId: Int) {
        UiUtils.setRecyclerViewItemAnimation(recyclerView, animId)
    }

    /**
     * Display error message in screen based on [Throwable]
     *
     * @param e instance of [Throwable]
     * @return error message to display in Ui
     */
    @UiThread
    internal fun displayError(e: Throwable): String {
        return NetworkUtils.getApiErrorMessage(baseActivity!!.applicationContext, e)
    }
}
