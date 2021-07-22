package org.hfathi.bugloos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.hfathi.bugloos.detail.adapters.GenreDetailAdapter
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.music.Album
import org.hfathi.bugloos.music.Artist
import org.hfathi.bugloos.music.BaseModel
import org.hfathi.bugloos.music.MusicStore
import org.hfathi.bugloos.music.Song
import org.hfathi.bugloos.playback.state.PlaybackMode
import org.hfathi.bugloos.ui.ActionMenu
import org.hfathi.bugloos.ui.newMenu

/**
 * The [DetailFragment] for a genre.
 * @author hamid fathi
 */
class GenreDetailFragment : DetailFragment() {
    private val args: GenreDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // If DetailViewModel isn't already storing the genre, get it from MusicStore
        // using the ID given by the navigation arguments
        if (detailModel.currentGenre.value == null ||
            detailModel.currentGenre.value?.id != args.genreId
        ) {
            detailModel.updateGenre(
                MusicStore.getInstance().genres.find {
                    it.id == args.genreId
                }!!
            )
        }

        val detailAdapter = GenreDetailAdapter(
            detailModel, playbackModel, viewLifecycleOwner,
            doOnClick = { song ->
                playbackModel.playSong(song, PlaybackMode.IN_GENRE)
            },
            doOnLongClick = { view, data ->
                newMenu(view, data, ActionMenu.FLAG_IN_GENRE)
            }
        )

        // --- UI SETUP ---

        binding.lifecycleOwner = this

        setupToolbar()
        setupRecycler(detailAdapter) { pos ->
            pos == 0
        }

        // --- DETAILVIEWMODEL SETUP ---

        detailModel.genreSortMode.observe(viewLifecycleOwner) { mode ->
            logD("Updating sort mode to $mode")

            // Detail header data is included
            val data = mutableListOf<BaseModel>(detailModel.currentGenre.value!!).also {
                it.addAll(mode.getSortedSongList(detailModel.currentGenre.value!!.songs))
            }

            detailAdapter.submitList(data)
        }

        detailModel.navToItem.observe(viewLifecycleOwner) { item ->
            when (item) {
                // All items will launch new detail fragments.
                is Artist -> findNavController().navigate(
                    GenreDetailFragmentDirections.actionShowArtist(item.id)
                )

                is Album -> findNavController().navigate(
                    GenreDetailFragmentDirections.actionShowAlbum(item.id)
                )

                is Song -> findNavController().navigate(
                    GenreDetailFragmentDirections.actionShowAlbum(item.album.id)
                )

                else -> {}
            }
        }

        // --- PLAYBACKVIEWMODEL SETUP ---

        playbackModel.song.observe(viewLifecycleOwner) { song ->
            if (playbackModel.mode.value == PlaybackMode.IN_GENRE &&
                playbackModel.parent.value?.id == detailModel.currentGenre.value!!.id
            ) {
                detailAdapter.highlightSong(song, binding.detailRecycler)
            } else {
                // Clear the viewholders if the mode isn't ALL_SONGS
                detailAdapter.highlightSong(null, binding.detailRecycler)
            }
        }

        playbackModel.isInUserQueue.observe(viewLifecycleOwner) { inUserQueue ->
            if (inUserQueue) {
                detailAdapter.highlightSong(null, binding.detailRecycler)
            }
        }

        logD("Fragment created.")

        return binding.root
    }
}
