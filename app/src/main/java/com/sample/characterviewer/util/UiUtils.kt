package com.sample.characterviewer.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

/**
 * This class is use for perform ui related operations.
 */
object UiUtils {

    /**
     * Show snackbar with message detail.
     *
     * @param view    parent view.
     * @param message message to display.
     * @param length  duration to display snackbar.
     */
    @UiThread
    fun showSnackBar(view: View, message: String, length: Int) {
        Snackbar.make(view, message, length).show()
    }

    /**
     * hide keyboard
     *
     * @param view view which is focused.
     */
    @UiThread
    fun hideKeyBoard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Set recyclerview item animation
     *
     * @param recyclerView instance of [RecyclerView]
     * @param animId       animation id.
     */
    @UiThread
    fun setRecyclerViewItemAnimation(recyclerView: RecyclerView, animId: Int) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, animId)
        recyclerView.layoutAnimation = animation
    }

}
