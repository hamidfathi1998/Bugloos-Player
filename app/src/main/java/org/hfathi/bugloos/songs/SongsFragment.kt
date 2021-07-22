package org.hfathi.bugloos.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import org.hfathi.bugloos.R
import org.hfathi.bugloos.databinding.FragmentSongsBinding
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.music.MusicStore
import org.hfathi.bugloos.playback.PlaybackViewModel
import org.hfathi.bugloos.recycler.sliceArticle
import org.hfathi.bugloos.ui.getSpans
import org.hfathi.bugloos.ui.newMenu

/**
 * A [Fragment] that shows a list of all songs on the device.
 * Contains options to search/shuffle them.
 * @author hamid fathi
 */
class SongsFragment : Fragment() {
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val musicStore = MusicStore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSongsBinding.inflate(inflater)
        val songAdapter = SongsAdapter(musicStore.songs, playbackModel::playSong) { view, data ->
            newMenu(view, data)
        }

        // --- UI SETUP ---

        binding.songToolbar.apply {
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_shuffle) {
                    playbackModel.shuffleAll()
                    true
                } else false
            }
        }

        binding.songRecycler.apply {
            adapter = songAdapter
            setHasFixedSize(true)

            val spans = getSpans()
            if (spans != 1) {
                layoutManager = GridLayoutManager(requireContext(), spans)
            }
        }

        binding.songFastScroll.setup(binding.songRecycler) { pos ->
            // Get the first character [respecting articles]
            val char = musicStore.songs[pos].name.sliceArticle().first().uppercaseChar()

            if (char.isDigit()) '#' else char
        }

        logD("Fragment created.")

        return binding.root
    }
}
