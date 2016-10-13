package net.serverpeon.botterknife

import android.support.annotation.IdRes
import android.view.View
import kotlin.properties.ReadOnlyProperty

/**
 * Specialization for the Android case. Views all descend from [View] and are found using an
 * [Int]-based Id.
 *
 * Through overrides this also adds the [IdRes] annotation to the relevant methods.
 *
 * See the [net.serverpeon.botterknife.typed] package for implementations for the default Android classes.
 */
abstract class AndroidBinder<in SOURCE> : LookupBinder<SOURCE, View, Int>() {
    final override fun <V : View> bindView(@IdRes id: Int): ReadOnlyProperty<SOURCE, V> {
        return super.bindView(id)
    }

    final override fun <V : View> bindOptionalView(@IdRes id: Int): ReadOnlyProperty<SOURCE, V?> {
        return super.bindOptionalView(id)
    }


    interface Delegate<in SOURCE, out BINDER : LookupBinder<SOURCE, View, Int>>
    : LookupBinder.Delegate<SOURCE, View, Int, BINDER> {
        override fun <V : View> SOURCE.bindView(@IdRes ident: Int)
                = binder.bindView<V>(ident)

        override fun <V : View> SOURCE.bindOptionalView(@IdRes ident: Int)
                = binder.bindOptionalView<V>(ident)
    }
}