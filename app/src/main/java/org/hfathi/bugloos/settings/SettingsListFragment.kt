package org.hfathi.bugloos.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.children
import coil.Coil
import org.hfathi.bugloos.R
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.playback.PlaybackViewModel
import org.hfathi.bugloos.recycler.DisplayMode
import org.hfathi.bugloos.settings.accent.AccentDialog
import org.hfathi.bugloos.settings.blacklist.BlacklistDialog
import org.hfathi.bugloos.settings.ui.IntListPrefDialog
import org.hfathi.bugloos.settings.ui.IntListPreference
import org.hfathi.bugloos.ui.Accent
import org.hfathi.bugloos.ui.isNight
import org.hfathi.bugloos.ui.showToast

/**
 * The actual fragment containing the settings menu. Inherits [PreferenceFragmentCompat].
 * @author hamid fathi
 */
@Suppress("UNUSED")
class SettingsListFragment : PreferenceFragmentCompat() {
    private val playbackModel: PlaybackViewModel by activityViewModels()
    val settingsManager = SettingsManager.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceScreen.children.forEach { pref ->
            recursivelyHandleChildren(pref)
        }

        preferenceManager.onDisplayPreferenceDialogListener = this

        logD("Fragment created.")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs_main, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is IntListPreference) {
            IntListPrefDialog(preference).show(childFragmentManager, IntListPrefDialog.TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
    /**
     * Recursively call [handlePreference] on a preference.
     */
    private fun recursivelyHandleChildren(preference: Preference) {
        if (preference is PreferenceCategory) {
            // If this preference is a category of its own, handle its own children
            preference.children.forEach { pref ->
                recursivelyHandleChildren(pref)
            }
        } else {
            handlePreference(preference)
        }
    }

    /**
     * Handle a preference, doing any specific actions on it.
     */
    private fun handlePreference(pref: Preference) {
        pref.apply {
            when (key) {
                SettingsManager.KEY_THEME -> {
                    setIcon(AppCompatDelegate.getDefaultNightMode().toThemeIcon())

                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                        AppCompatDelegate.setDefaultNightMode(value as Int)
                        setIcon(AppCompatDelegate.getDefaultNightMode().toThemeIcon())
                        true
                    }
                }

                SettingsManager.KEY_BLACK_THEME -> {
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        if (requireContext().isNight()) {
                            requireActivity().recreate()
                        }

                        true
                    }
                }

                SettingsManager.KEY_ACCENT -> {
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        AccentDialog().show(childFragmentManager, AccentDialog.TAG)
                        true
                    }

                    summary = Accent.get().getDetailedSummary(context)
                }

                SettingsManager.KEY_LIB_DISPLAY_MODE -> {
                    setIcon(settingsManager.libraryDisplayMode.iconRes)

                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
                        setIcon(DisplayMode.fromInt(value as Int)!!.iconRes)
                        true
                    }
                }

                SettingsManager.KEY_SHOW_COVERS -> {
                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
                        Coil.imageLoader(requireContext()).apply {
                            bitmapPool.clear()
                            memoryCache.clear()
                        }

                        requireActivity().recreate()

                        true
                    }
                }

                SettingsManager.KEY_QUALITY_COVERS -> {
                    onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
                        // Clear out any cached images, before recreating the activity
                        Coil.imageLoader(requireContext()).apply {
                            bitmapPool.clear()
                            memoryCache.clear()
                        }

                        requireActivity().recreate()

                        true
                    }
                }

                SettingsManager.KEY_SAVE_STATE -> {
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        playbackModel.savePlaybackState(requireContext()) {
                            requireContext().showToast(R.string.label_state_saved)
                        }

                        true
                    }
                }

                SettingsManager.KEY_BLACKLIST -> {
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        BlacklistDialog().show(childFragmentManager, BlacklistDialog.TAG)
                        true
                    }
                }
            }
        }
    }
}
