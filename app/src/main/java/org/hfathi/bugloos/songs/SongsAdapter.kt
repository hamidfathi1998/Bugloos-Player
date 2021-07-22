package org.hfathi.bugloos.songs

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hfathi.bugloos.music.Song
import org.hfathi.bugloos.recycler.viewholders.SongViewHolder

/**
 * The adapter for [SongsFragment], shows basic songs without durations.
 * @param data List of [Song]s to be shown
 * @param doOnClick What to do on a click action
 * @param doOnLongClick What to do on a long click action
 * @author hamid fathi
 */
class SongsAdapter(
    private val data: List<Song>,
    private val doOnClick: (data: Song) -> Unit,
    private val doOnLongClick: (view: View, data: Song) -> Unit
) : RecyclerView.Adapter<SongViewHolder>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder.from(parent.context, doOnClick, doOnLongClick)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
