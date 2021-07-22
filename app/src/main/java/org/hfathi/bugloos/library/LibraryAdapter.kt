package org.hfathi.bugloos.library

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hfathi.bugloos.music.Album
import org.hfathi.bugloos.music.Artist
import org.hfathi.bugloos.music.Genre
import org.hfathi.bugloos.music.Parent
import org.hfathi.bugloos.recycler.viewholders.AlbumViewHolder
import org.hfathi.bugloos.recycler.viewholders.ArtistViewHolder
import org.hfathi.bugloos.recycler.viewholders.GenreViewHolder

/**
 * An adapter for displaying library items. Supports [Parent]s only.
 * @author hamid fathi
 */
class LibraryAdapter(
    private val doOnClick: (data: Parent) -> Unit,
    private val doOnLongClick: (view: View, data: Parent) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = listOf<Parent>()

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Genre -> GenreViewHolder.ITEM_TYPE
            is Artist -> ArtistViewHolder.ITEM_TYPE
            is Album -> AlbumViewHolder.ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GenreViewHolder.ITEM_TYPE -> GenreViewHolder.from(
                parent.context, doOnClick, doOnLongClick
            )

            ArtistViewHolder.ITEM_TYPE -> ArtistViewHolder.from(
                parent.context, doOnClick, doOnLongClick
            )

            AlbumViewHolder.ITEM_TYPE -> AlbumViewHolder.from(
                parent.context, doOnClick, doOnLongClick
            )

            else -> error("Invalid viewholder item type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = data[position]) {
            is Genre -> (holder as GenreViewHolder).bind(item)
            is Artist -> (holder as ArtistViewHolder).bind(item)
            is Album -> (holder as AlbumViewHolder).bind(item)
        }
    }

    /**
     * Update the data with [newData]. [notifyDataSetChanged] will be called.
     */
    fun updateData(newData: List<Parent>) {
        data = newData

        notifyDataSetChanged()
    }
}
