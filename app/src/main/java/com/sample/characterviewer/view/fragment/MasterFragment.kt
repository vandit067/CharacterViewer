package com.sample.characterviewer.view.fragment

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sample.characterviewer.R
import com.sample.characterviewer.model.CharactersData
import com.sample.characterviewer.model.CharactersDataApiResponseWrapper
import com.sample.characterviewer.model.RelatedTopic
import com.sample.characterviewer.view.adapter.CharactersAdapter
import com.sample.characterviewer.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.fragment_master.*
import java.util.*

/**
 * This class is used for perform resource search.
 */
class MasterFragment : BaseFragment() {

    private var mMasterViewModel: MasterViewModel? = null
    private var mCharactersAdapter: CharactersAdapter? = null
    private var mIsTabletView = false
    companion object {

        // Create new instance of MasterFragment
        fun newInstance(): MasterFragment {
            return MasterFragment()
        }
    }
    /**
     * [com.sample.characterviewer.model.CharactersDataApiResponseWrapper] observer which will observe event through [androidx.lifecycle.LiveData] and update UI accordingly.
     */
    private val mCharactersDataApiResponseWrapperObserver = Observer<CharactersDataApiResponseWrapper<*>> { charactersDataApiResponseWrapper ->
        //Handle UI
        showContent(fragment_master_progressbar!!, fragment_master_rv_characters!!)
        if (charactersDataApiResponseWrapper == null) {
            showError(R.string.message_no_data_available)
            return@Observer
        }
        if (charactersDataApiResponseWrapper.throwable != null) {
            showError(displayError(charactersDataApiResponseWrapper.throwable!!))
            return@Observer
        }
        val charactersData = charactersDataApiResponseWrapper.response as CharactersData
        if (charactersData.relatedTopics == null || charactersData.relatedTopics!!.isEmpty()) {
            showError(R.string.message_no_data_available)
            return@Observer
        }
        mCharactersAdapter!!.addItems(charactersData.relatedTopics!!)
        if (mIsTabletView) {
            replaceFragmentInViewForTablet(charactersData.relatedTopics!![0])
        }
    }
    private var mIsGridViewEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // We are not creating instance of ViewModel class here.
        this.mMasterViewModel = ViewModelProviders.of(activity!!).get(MasterViewModel::class.java)
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        this.mMasterViewModel!!.charactersDataObserver.observe(this, mCharactersDataApiResponseWrapperObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_master, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.handleConditionalViews(view)
        this.initUI()
        this.retrieveAndSetCharactersList()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        baseActivity?.supportActionBar?.title = getString(R.string.app_name)
    }

    /**
     * Initialize Ui Component
     */
    private fun initUI() {
        val gridLayoutManager = GridLayoutManager(baseActivity, 1)
        fragment_master_rv_characters.layoutManager = gridLayoutManager
        fragment_master_rv_characters.itemAnimator?.changeDuration = 700
        setRecyclerViewItemAnimation(fragment_master_rv_characters, R.anim.layout_animation_from_bottom)
        this.mCharactersAdapter = CharactersAdapter(ArrayList(), this::onCharacterSelected, false)
        fragment_master_rv_characters.adapter = this.mCharactersAdapter
    }

    /**
     * Call when recycler view item click happen.
     *
     * @param relatedTopic Selected [com.sample.characterviewer.model.RelatedTopic] object at position.
     */
    private fun onCharacterSelected(relatedTopic: RelatedTopic) {
        if (this.mIsTabletView) {
            this.replaceFragmentInViewForTablet(relatedTopic)
            return
        }
        addFragment(DetailFragment.newInstance(relatedTopic, false), javaClass.name)
    }

    private fun replaceFragmentInViewForTablet(relatedTopic: RelatedTopic) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragment_detail_container, DetailFragment.newInstance(relatedTopic, this.mIsTabletView)).commit()
    }


    /**
     * Initiate api call and get response.
     */
    private fun retrieveAndSetCharactersList() {
        showProgress(fragment_master_progressbar, fragment_master_rv_characters)
        this.mMasterViewModel!!.retrieveCharactersData()
    }


    // Handle conditional views based on tablet/phone
    private fun handleConditionalViews(view: View) {
        if (view.findViewById<View>(R.id.fragment_detail_container) != null) {
            this.mIsTabletView = true
            return
        }
        val switchViewFab = view.findViewById<FloatingActionButton>(R.id.fragment_master_fab_switch_view)
        switchViewFab.setOnClickListener {
            if (!(switchViewFab.drawable as Animatable).isRunning) {
                mIsGridViewEnable = !mIsGridViewEnable
                switchViewFab.setImageDrawable(if (mIsGridViewEnable)
                    AnimatedVectorDrawableCompat.create(activity!!, R.drawable.avd_grid_to_list)
                else
                    AnimatedVectorDrawableCompat.create(activity!!, R.drawable.avd_list_to_grid))
                mCharactersAdapter!!.switchViews()
                fragment_master_rv_characters.layoutManager = if (mIsGridViewEnable) GridLayoutManager(baseActivity, 4) else LinearLayoutManager(baseActivity)
                (switchViewFab.drawable as Animatable).start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.mMasterViewModel != null) {
            this.mMasterViewModel = null
        }
        if (this.mCharactersAdapter != null) {
            this.mCharactersAdapter = null
        }
    }

}
