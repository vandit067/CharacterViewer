package com.sample.characterviewer.viewmodel

import android.app.Application
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.sample.characterviewer.R
import com.sample.characterviewer.model.CharactersDataApiResponseWrapper
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Class used for handle the business logic for [com.sample.characterviewer.view.fragment.MasterFragment]
 */
class MasterViewModel(application: Application) : BaseViewModel(application) {

    private var mCharactersDataObserver: MutableLiveData<CharactersDataApiResponseWrapper<*>>? = null

    val charactersDataObserver: MutableLiveData<CharactersDataApiResponseWrapper<*>>
        @UiThread
        get() {
            if (mCharactersDataObserver == null) {
                this.mCharactersDataObserver = MutableLiveData()
            }
            return this.mCharactersDataObserver!!
        }

    @WorkerThread
    fun retrieveCharactersData() {
        addDisposable(apiServices!!.apiInterface.getCharactersData(appContext.getString(R.string.api_query_parameter), "json")
                .subscribeOn(appRxSchedulers!!.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ charactersData -> mCharactersDataObserver!!.postValue(CharactersDataApiResponseWrapper(charactersData)) },
                        { throwable -> mCharactersDataObserver!!.postValue(CharactersDataApiResponseWrapper<Throwable>(throwable)) }))

    }

}
