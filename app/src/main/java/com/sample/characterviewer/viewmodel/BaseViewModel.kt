package com.sample.characterviewer.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.sample.characterviewer.api.ApiServices
import com.sample.characterviewer.util.rx.AppRxSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel
/**
 * Constructor with application instance and initialize [CompositeDisposable], [ApiServices] and [AppRxSchedulers]
 *
 * @param application instance of application
 */
(application: Application) : AndroidViewModel(application) {

    /**
     * Get instance of [CompositeDisposable]
     *
     * @return CompositeDisposable instance of [CompositeDisposable]
     */
    val compositeDisposable: CompositeDisposable?
    /**
     * Get instance of [AppRxSchedulers]
     *
     * @return AppRxSchedulers instance of [AppRxSchedulers]
     */
    var appRxSchedulers: AppRxSchedulers? = null
        private set
    /**
     * Get api services.
     *
     * @return ApiServices instane of  [ApiServices].
     */
    var apiServices: ApiServices? = null
        private set

    /**
     * Get application context.
     *
     * @return Context application context.
     */
    val appContext: Context
        get() = getApplication<Application>().applicationContext

    init {
        this.compositeDisposable = CompositeDisposable()
        this.appRxSchedulers = AppRxSchedulers()
        this.apiServices = ApiServices.getInstance(this.appContext)
    }

    /**
     * Add disposable to [CompositeDisposable]
     *
     * @param disposable instance of [Disposable]
     */
    fun addDisposable(disposable: Disposable) {
        this.compositeDisposable!!.add(disposable)
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        // Clear everything here.
        if (this.compositeDisposable != null && !this.compositeDisposable.isDisposed) {
            this.compositeDisposable.dispose()
        }
        if (this.appRxSchedulers != null) {
            this.appRxSchedulers = null
        }
        if (this.apiServices != null) {
            this.apiServices = null
        }
    }
}
