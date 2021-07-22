package org.hfathi.bugloos.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hfathi.bugloos.music.Album
import org.hfathi.bugloos.music.Artist
import org.hfathi.bugloos.music.BaseModel
import org.hfathi.bugloos.music.Genre
import org.hfathi.bugloos.music.Header
import org.hfathi.bugloos.music.Song
import org.hfathi.bugloos.recycler.DiffCallback
import org.hfathi.bugloos.recycler.viewholders.AlbumViewHolder
import org.hfathi.bugloos.recycler.viewholders.ArtistViewHolder
import org.hfathi.bugloos.recycler.viewholders.GenreViewHolder
import org.hfathi.bugloos.recycler.viewholders.HeaderViewHolder
import org.hfathi.bugloos.recycler.viewholders.SongViewHolder

/**
 * A Multi-ViewHolder adapter that displays the results of a search query.
 * @author hamid fathi
 */
class SearchAdapter(
    private val doOnClick: (data: BaseModel) -> Unit,
    private val doOnLongClick: (view: View, data: BaseModel) -> Unit
) : ListAdapter<BaseModel, RecyclerView.ViewHolder>(DiffCallback<BaseModel>()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Genre -> GenreViewHolder.ITEM_TYPE
            is Artist -> ArtistViewHolder.ITEM_TYPE
            is Album -> AlbumViewHolder.ITEM_TYPE
            is Song -> SongViewHolder.ITEM_TYPE
            is Header -> HeaderViewHolder.ITEM_TYPE

            else -> -1
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

            SongViewHolder.ITEM_TYPE -> SongViewHolder.from(
                parent.context, doOnClick, doOnLongClick
            )

            HeaderViewHolder.ITEM_TYPE -> HeaderViewHolder.from(parent.context)

            else -> error("Invalid viewholder item type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Genre -> (holder as GenreViewHolder).bind(item)
            is Artist -> (holder as ArtistViewHolder).bind(item)
            is Album -> (holder as AlbumViewHolder).bind(item)
            is Song -> (holder as SongViewHolder).bind(item)
            is Header -> (holder as HeaderViewHolder).bind(item)
        }
    }
}
