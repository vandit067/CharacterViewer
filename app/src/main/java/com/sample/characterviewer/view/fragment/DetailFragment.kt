package com.sample.characterviewer.view.fragment


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.fromHtml
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sample.characterviewer.R
import com.sample.characterviewer.model.RelatedTopic
import com.sample.characterviewer.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * This class will display detail information of resource.
 */
class DetailFragment : BaseFragment() {
    private var mDetailViewModel: DetailViewModel? = null
    companion object {

        private const val KEY_RELATED_TOPIC = "key_related_topic"
        private const val KEY_IS_TABLET_VIEW = "key_is_tablet_view"

        /**
         * Constructor to create instance of [DetailFragment]
         *
         * @param relatedTopic instnce of @[RelatedTopic] from [MasterFragment]
         * @return instance of [DetailFragment]
         */
        internal fun newInstance(relatedTopic: RelatedTopic, isTabletView: Boolean): DetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEY_RELATED_TOPIC, relatedTopic)
            bundle.putBoolean(KEY_IS_TABLET_VIEW, isTabletView)
            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle
            return detailFragment
        }
    }

    /**
     * [RelatedTopic] observer which will observe event through [androidx.lifecycle.LiveData] and update UI accordingly.
     */
    private val mResultObserver = Observer<RelatedTopic> { relatedTopic ->
        if (relatedTopic == null) {
            showMessage(R.string.message_no_data_available)
            popFragment()
            return@Observer
        }
        showContent(fragment_detail_progressbar!!, fragment_detail_rl_content!!)
        setRelatedTopicData(relatedTopic)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        // We are not creating instance of ViewModel class here.
        this.mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        this.mDetailViewModel!!.result.observe(this, mResultObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) {
            popFragment()
            return
        }
        val relatedTopic = arguments!!.getParcelable<RelatedTopic>(KEY_RELATED_TOPIC)
        if (relatedTopic == null) {
            popFragment()
            return
        }
        this.retrieveRelatedTopicDetail(relatedTopic)
    }

    /**
     * Initiate related topic detail api call and get response.
     */
    private fun retrieveRelatedTopicDetail(relatedTopic: RelatedTopic) {
        showProgress(this.fragment_detail_progressbar!!, this.fragment_detail_rl_content!!)
        this.mDetailViewModel!!.setResult(relatedTopic)
    }

    /**
     * Set character details in view from @[RelatedTopic]
     */
    @Suppress("DEPRECATION")
    @UiThread
    private fun setRelatedTopicData(relatedTopic: RelatedTopic) {
        if (relatedTopic.icon != null && !TextUtils.isEmpty(relatedTopic.icon!!.url)) {
            Glide.with(baseActivity!!.applicationContext).load(relatedTopic.icon!!.url)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_default_place_holder)
                            .error(R.drawable.ic_default_place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH))
                    .into(this.fragment_detail_iv_character!!)
        }
        if (arguments != null) {
            if (!arguments!!.getBoolean(KEY_IS_TABLET_VIEW, false)) {
                baseActivity?.supportActionBar?.title = relatedTopic.text
            } else {
                this.fragment_detail_tv_title!!.visibility = View.VISIBLE
                this.fragment_detail_tv_title!!.text = relatedTopic.text
            }
        }
        if (!TextUtils.isEmpty(relatedTopic.firstURL)) {
            this.fragment_detail_cv_first_url!!.visibility = View.VISIBLE
            this.fragment_detail_tv_first_url!!.text = relatedTopic.firstURL
            this.fragment_detail_btn_view!!.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(relatedTopic.firstURL)
                startActivity(i)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragment_detail_tv_result!!.text = fromHtml(relatedTopic.result, Html.FROM_HTML_MODE_COMPACT)
            return
        }
        fragment_detail_tv_result!!.text = fromHtml(relatedTopic.result)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.mDetailViewModel != null) {
            this.mDetailViewModel = null
        }
    }
}
