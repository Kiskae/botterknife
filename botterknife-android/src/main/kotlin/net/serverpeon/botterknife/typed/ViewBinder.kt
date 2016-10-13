package net.serverpeon.botterknife.typed

import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [View].
 */
class ViewBinder : AndroidBinder<View>() {
    override fun findView(source: View, id: Int): View? {
        return source.findViewById(id)
    }

    interface Delegate : AndroidBinder.Delegate<View, ViewBinder>
}