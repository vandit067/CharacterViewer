package com.sample.characterviewer.util.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class AppRxSchedulers : RxSchedulers {

    override fun runOnBackground(): Scheduler {
        return BACKGROUND_SCHEDULERS
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun compute(): Scheduler {
        return Schedulers.computation()
    }

    override fun androidThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun internet(): Scheduler {
        return INTERNET_SCHEDULERS
    }

    companion object {

        var BACKGROUND_SCHEDULERS = Schedulers.from(Executors.newCachedThreadPool())
        var INTERNET_SCHEDULERS = Schedulers.from(Executors.newCachedThreadPool())
    }
}
