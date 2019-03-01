package com.sample.characterviewer.view.activity

import android.os.Bundle
import com.sample.characterviewer.R
import com.sample.characterviewer.view.fragment.MasterFragment
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : BaseActivity() {

    override fun setUp(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction().add(R.id.activity_master_container,
                MasterFragment.newInstance(), MasterFragment::class.java.name).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

}
