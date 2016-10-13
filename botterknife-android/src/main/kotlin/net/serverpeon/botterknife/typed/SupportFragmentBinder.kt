package net.serverpeon.botterknife.typed

import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [Fragment].
 */
@RequiresApi(api = 4)
class SupportFragmentBinder : AndroidBinder<Fragment>() {
    override fun findView(source: Fragment, id: Int): View? {
        return source.view?.findViewById(id)
    }

    @RequiresApi(api = 4)
    interface Delegate : AndroidBinder.Delegate<Fragment, SupportFragmentBinder>
}