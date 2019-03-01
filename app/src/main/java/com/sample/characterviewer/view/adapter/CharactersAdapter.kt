package com.sample.characterviewer.view.adapter

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.renderscript.RenderScript
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.sample.characterviewer.R
import com.sample.characterviewer.model.RelatedTopic
import kotlinx.android.synthetic.main.item_character.view.*
import kotlinx.android.synthetic.main.item_character_grid.view.*

class CharactersAdapter(
        private val mRelatedTopicsList: MutableList<RelatedTopic>,
        private val onCharacterSelected: (RelatedTopic) -> Unit,  private var mIsGridViewEnable : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_LIST = 0
        private const val VIEW_GRID = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_LIST) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
            CharactersListViewHolder(view, onCharacterSelected)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character_grid, parent, false)
            CharactersGridViewHolder(view, onCharacterSelected)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mIsGridViewEnable) VIEW_GRID else VIEW_LIST
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = mRelatedTopicsList[position]
        if (holder.itemViewType == VIEW_LIST) {
            (holder as CharactersListViewHolder).bind(result)
            return
        }
        (holder as CharactersGridViewHolder).bind(result)
    }

    override fun getItemCount(): Int {
        return this.mRelatedTopicsList.size
    }

    /**
     * Add item at position in list.
     *
     * @param relatedTopicList new list of [RelatedTopic]
     */
    fun addItems(relatedTopicList: List<RelatedTopic>) {
        mRelatedTopicsList.addAll(relatedTopicList)
        notifyDataSetChanged()
    }

    fun switchViews() {
        this.mIsGridViewEnable = !this.mIsGridViewEnable
    }

    // View holder which will display list content
    private class CharactersListViewHolder(itemView: View, private val onCharacterSelected: (RelatedTopic) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(relatedTopic: RelatedTopic){
            with(relatedTopic){
                itemView.item_character_title.text = this.text
                itemView.setOnClickListener { onCharacterSelected(this) }
            }
        }
    }

    // View holder which will display grid content
    private class CharactersGridViewHolder(itemView: View, private val onCharacterSelected: (RelatedTopic) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val mRequestOptions: RequestOptions = RequestOptions().centerCrop()
                .placeholder(R.drawable.ic_default_place_holder)
                .error(R.drawable.ic_default_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .circleCrop()

        private val animationObject = ViewPropertyTransition.Animator {
            it.alpha = 0f
            val fadeAnim = ObjectAnimator.ofFloat(it, "alpha", 0f, 1f)
            fadeAnim.duration = 500
            fadeAnim.start()
        }
        fun bind(relatedTopic: RelatedTopic){
            with(relatedTopic){
                if (this.icon != null && !TextUtils.isEmpty(this.icon!!.url)) {
                    Glide.with(itemView.context.applicationContext).load(this.icon!!.url)
                            .transition(GenericTransitionOptions.with<Drawable>(animationObject))
                            .apply(mRequestOptions)
                            .into(itemView.item_character_grid_iv_icon!!)
                }
                itemView.setOnClickListener { onCharacterSelected(this) }
            }
        }
    }
}
