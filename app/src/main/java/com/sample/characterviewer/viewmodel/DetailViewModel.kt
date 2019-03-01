package com.sample.characterviewer.viewmodel

import android.app.Application
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import com.sample.characterviewer.model.RelatedTopic

/**
 * Class used for handle the business logic for [com.sample.characterviewer.view.fragment.DetailFragment]
 */
class DetailViewModel(application: Application) : BaseViewModel(application) {

    private var mRelatedTopic: MutableLiveData<RelatedTopic>? = null

    val result: MutableLiveData<RelatedTopic>
        @UiThread
        get() {
            if (this.mRelatedTopic == null) {
                this.mRelatedTopic = MutableLiveData()
            }
            return this.mRelatedTopic!!
        }

    @UiThread
    fun setResult(relatedTopic: RelatedTopic) {
        this.mRelatedTopic!!.value = relatedTopic
    }
}
