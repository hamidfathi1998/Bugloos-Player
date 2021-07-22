package org.hfathi.bugloos.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.hfathi.bugloos.MainFragmentDirections
import org.hfathi.bugloos.databinding.FragmentSettingsBinding

/**
 * A container [Fragment] for the settings menu.
 * @author hamid fathi
 */
class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)

        binding.settingsToolbar.setOnMenuItemClickListener {
            parentFragment?.parentFragment?.findNavController()?.navigate(
                MainFragmentDirections.actionShowAbout()
            )

            true
        }

        return binding.root
    }
}
