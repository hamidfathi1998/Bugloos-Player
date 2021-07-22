package org.hfathi.bugloos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.hfathi.bugloos.R
import org.hfathi.bugloos.detail.adapters.ArtistDetailAdapter
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.music.Album
import org.hfathi.bugloos.music.Artist
import org.hfathi.bugloos.music.BaseModel
import org.hfathi.bugloos.music.Header
import org.hfathi.bugloos.music.MusicStore
import org.hfathi.bugloos.music.Song
import org.hfathi.bugloos.playback.state.PlaybackMode
import org.hfathi.bugloos.recycler.SortMode
import org.hfathi.bugloos.ui.ActionMenu
import org.hfathi.bugloos.ui.newMenu

/**
 * The [DetailFragment] for an artist.
 * @author hamid fathi
 */
class ArtistDetailFragment : DetailFragment() {
    private val args: ArtistDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // If DetailViewModel isn't already storing the artist, get it from MusicStore
        // using the ID given by the navigation arguments
        if (detailModel.currentArtist.value == null ||
            detailModel.currentArtist.value?.id != args.artistId
        ) {
            detailModel.updateArtist(
                MusicStore.getInstance().artists.find {
                    it.id == args.artistId
                }!!
            )
        }

        val detailAdapter = ArtistDetailAdapter(
            playbackModel, detailModel,
            doOnClick = { data ->
                if (!detailModel.isNavigating) {
                    detailModel.setNavigating(true)

                    findNavController().navigate(
                        ArtistDetailFragmentDirections.actionShowAlbum(data.id)
                    )
                }
            },
            doOnSongClick = { data ->
                playbackModel.playSong(data, PlaybackMode.IN_ARTIST)
            },
            doOnLongClick = { view, data ->
                newMenu(view, data, ActionMenu.FLAG_IN_ARTIST)
            }
        )

        // --- UI SETUP ---

        binding.lifecycleOwner = this

        setupToolbar()
        setupRecycler(detailAdapter) { pos ->
            // If the item is an ActionHeader we need to also make the item full-width
            pos == 0 || detailAdapter.currentList.getOrNull(pos) is Header
        }

        // --- VIEWMODEL SETUP ---

        detailModel.artistSortMode.observe(viewLifecycleOwner) { mode ->
            logD("Updating sort mode to $mode")

            val artist = detailModel.currentArtist.value!!

            val data = mutableListOf<BaseModel>(artist)

            data.addAll(SortMode.NUMERIC_DOWN.getSortedAlbumList(artist.albums))

            data.add(
                Header(
                    id = -2,
                    name = getString(R.string.label_songs),
                    isAction = true
                )
            )

            data.addAll(mode.getSortedArtistSongList(artist.songs))

            detailAdapter.submitList(data)
        }

        detailModel.navToItem.observe(viewLifecycleOwner) { item ->
            when (item) {
                is Artist -> {
                    if (item.id == detailModel.currentArtist.value!!.id) {
                        binding.detailRecycler.scrollToPosition(0)
                        detailModel.doneWithNavToItem()
                    } else {
                        findNavController().navigate(
                            ArtistDetailFragmentDirections.actionShowArtist(item.id)
                        )
                    }
                }

                is Album -> findNavController().navigate(
                    ArtistDetailFragmentDirections.actionShowAlbum(item.id)
                )

                is Song -> findNavController().navigate(
                    ArtistDetailFragmentDirections.actionShowAlbum(item.album.id)
                )

                else -> {}
            }
        }

        // Highlight albums if they are being played
        playbackModel.parent.observe(viewLifecycleOwner) { parent ->
            if (parent is Album?) {
                detailAdapter.highlightAlbum(parent, binding.detailRecycler)
            } else {
                detailAdapter.highlightAlbum(null, binding.detailRecycler)
            }
        }

        // Highlight songs if they are being played
        playbackModel.song.observe(viewLifecycleOwner) { song ->
            if (playbackModel.mode.value == PlaybackMode.IN_ARTIST &&
                playbackModel.parent.value?.id == detailModel.currentArtist.value!!.id
            ) {
                detailAdapter.highlightSong(song, binding.detailRecycler)
            } else {
                // Clear the viewholders if the mode isn't ALL_SONGS
                detailAdapter.highlightSong(null, binding.detailRecycler)
            }
        }

        logD("Fragment created.")

        return binding.root
    }
}
