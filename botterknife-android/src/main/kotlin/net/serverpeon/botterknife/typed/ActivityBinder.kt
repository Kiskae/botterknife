package net.serverpeon.botterknife.typed

import android.app.Activity
import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [Activity].
 */
class ActivityBinder : AndroidBinder<Activity>() {
    override fun findView(source: Activity, id: Int): View? {
        return source.findViewById(id)
    }

    interface Delegate : AndroidBinder.Delegate<Activity, ActivityBinder>
}