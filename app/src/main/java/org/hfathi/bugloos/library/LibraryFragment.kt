package org.hfathi.bugloos.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.hfathi.bugloos.R
import org.hfathi.bugloos.databinding.FragmentLibraryBinding
import org.hfathi.bugloos.detail.DetailViewModel
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.music.Album
import org.hfathi.bugloos.music.Artist
import org.hfathi.bugloos.music.Genre
import org.hfathi.bugloos.music.Parent
import org.hfathi.bugloos.music.Song
import org.hfathi.bugloos.recycler.sliceArticle
import org.hfathi.bugloos.ui.getSpans
import org.hfathi.bugloos.ui.newMenu

/**
 * A [Fragment] that shows a custom list of [Genre], [Artist], or [Album] data. Also allows for
 * search functionality.
 * @author hamid fathi
 */
class LibraryFragment : Fragment() {
    private val libraryModel: LibraryViewModel by activityViewModels()
    private val detailModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLibraryBinding.inflate(inflater)

        val libraryAdapter = LibraryAdapter(::navToDetail) { view, data ->
            newMenu(view, data)
        }

        // --- UI SETUP ---

        binding.libraryToolbar.apply {
            menu.findItem(libraryModel.sortMode.toMenuId()).isChecked = true

            setOnMenuItemClickListener { item ->
                if (item.itemId != R.id.submenu_sorting) {
                    libraryModel.updateSortMode(item.itemId)
                    item.isChecked = true
                    true
                } else {
                    false
                }
            }
        }

        binding.libraryRecycler.apply {
            adapter = libraryAdapter
            setHasFixedSize(true)

            val spans = getSpans()
            if (spans != 1) {
                layoutManager = GridLayoutManager(requireContext(), spans)
            }
        }

        binding.libraryFastScroll.setup(binding.libraryRecycler) { pos ->
            val item = libraryModel.libraryData.value!![pos]
            val char = item.displayName.sliceArticle().first().uppercaseChar()

            if (char.isDigit()) '#' else char
        }

        // --- VIEWMODEL SETUP ---

        libraryModel.libraryData.observe(viewLifecycleOwner) { data ->
            libraryAdapter.updateData(data)
        }

        detailModel.navToItem.observe(viewLifecycleOwner) { item ->
            if (item != null) {
                libraryModel.setNavigating(false)

                if (item is Parent) {
                    navToDetail(item)
                } else if (item is Song) {
                    navToDetail(item.album)
                }
            }
        }

        logD("Fragment created.")

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        libraryModel.setNavigating(false)
    }

    /**
     * Navigate to the detail UI for a [parent].
     */
    private fun navToDetail(parent: Parent) {
        requireView().rootView.clearFocus()

        if (!libraryModel.isNavigating) {
            libraryModel.setNavigating(true)

            logD("Navigating to the detail fragment for ${parent.name}")

            findNavController().navigate(
                when (parent) {
                    is Genre -> LibraryFragmentDirections.actionShowGenre(parent.id)
                    is Artist -> LibraryFragmentDirections.actionShowArtist(parent.id)
                    is Album -> LibraryFragmentDirections.actionShowAlbum(parent.id)
                }
            )
        }
    }
}
