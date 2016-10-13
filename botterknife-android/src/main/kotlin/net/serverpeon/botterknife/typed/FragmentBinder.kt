package net.serverpeon.botterknife.typed

import android.app.Fragment
import android.support.annotation.RequiresApi
import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [Fragment].
 */
@RequiresApi(api = 11)
class FragmentBinder : AndroidBinder<Fragment>() {
    override fun findView(source: Fragment, id: Int): View? {
        return source.view.findViewById(id)
    }

    @RequiresApi(api = 11)
    interface Delegate : AndroidBinder.Delegate<Fragment, FragmentBinder>
}