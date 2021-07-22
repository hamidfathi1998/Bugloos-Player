package org.hfathi.bugloos.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.common.BuildConfig.VERSION_NAME
import com.google.android.exoplayer2.core.BuildConfig.VERSION_NAME
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hfathi.bugloos.BuildConfig
import org.hfathi.bugloos.R
import org.hfathi.bugloos.databinding.FragmentAboutBinding
import org.hfathi.bugloos.logD
import org.hfathi.bugloos.music.MusicStore
import org.hfathi.bugloos.ui.showToast

/**
 * A [BottomSheetDialogFragment] that shows Bugloos's about screen.
 * @author hamid fathi
 */
class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAboutBinding.inflate(layoutInflater)
        val musicStore = MusicStore.getInstance()

        binding.aboutToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.aboutVersion.text = BuildConfig.VERSION_NAME

        logD("Dialog created.")

        return binding.root
    }

    /**
     * Go through the process of opening a [link] in a browser.
     */
    private fun openLinkInBrowser(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, link.toUri()).setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
        )

        val pkgName = requireContext().packageManager.resolveActivity(
            browserIntent, PackageManager.MATCH_DEFAULT_ONLY
        )?.activityInfo?.packageName

        if (pkgName != null) {
            if (pkgName == "android") {
                // No default browser [Must open app chooser, may not be supported]
                openAppChooser(browserIntent)
            } else {
                try {
                    browserIntent.setPackage(pkgName)
                    startActivity(browserIntent)
                } catch (exception: ActivityNotFoundException) {
                    // Not browser but an app chooser due to OEM garbage
                    browserIntent.setPackage(null)
                    openAppChooser(browserIntent)
                }
            }
        } else {
            // No app installed to open the link
            requireContext().showToast(R.string.error_no_browser)
        }
    }

    private fun openAppChooser(intent: Intent) {
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            .putExtra(Intent.EXTRA_INTENT, intent)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(chooserIntent)
    }

    companion object {
        private const val LINK_CODEBASE = "https://github.com/oxygencobalt/Auxio"
        private const val LINK_FAQ = "$LINK_CODEBASE/blob/master/info/FAQ.md"
        private const val LINK_LICENSES = "$LINK_CODEBASE/blob/master/info/LICENSES.md"
    }
}
