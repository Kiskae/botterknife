package net.serverpeon.botterknife.typed

import android.app.Dialog
import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [Dialog].
 */
class DialogBinder : AndroidBinder<Dialog>() {
    override fun findView(source: Dialog, id: Int): View? {
        return source.findViewById(id)
    }

    interface Delegate : AndroidBinder.Delegate<Dialog, DialogBinder>
}