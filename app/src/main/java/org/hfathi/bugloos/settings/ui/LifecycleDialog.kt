package org.hfathi.bugloos.settings.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hfathi.bugloos.R
import org.hfathi.bugloos.ui.resolveAttr

/**
 * A wrapper around [DialogFragment] that allows the usage of the standard bugloos_player lifecycle
 * override [onCreateView] and [onDestroyView], but with a proper dialog being created.
 */
abstract class LifecycleDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity(), theme)

        // Setting the background in XML will also apply it to the tooltip for some inane reason
        // so we have to do it programmatically instead.
        builder.background = R.attr.colorSurface.resolveAttr(requireContext()).toDrawable()

        onConfigDialog(builder)

        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireDialog() as AlertDialog).setView(view)
    }

    protected open fun onConfigDialog(builder: AlertDialog.Builder) {}
}
