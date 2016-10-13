package net.serverpeon.botterknife

import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A factory object that creates lazy lookup delegates through the [bindView] and [bindOptionalView] methods.
 * Through the [unbind] method all created delegates will throw away any value they might hold at that time.
 *
 * This is a base class, it should be subclassed for more specific uses.
 *
 * @param SOURCE The type that holds a reference to the views.
 * @param BASE_VIEW The root view object.
 * @param IDENTIFIER The object by which views are identified.
 */
abstract class LookupBinder<in SOURCE, BASE_VIEW : Any, in IDENTIFIER> {
    private val bindings = mutableListOf<WeakReference<CachedView<*, *, *, *>>>()

    /**
     * Find the view identified by [id] within [source] if present, otherwise return null.
     *
     * @param source The object in which to look for the view.
     * @param id The object by which the required view can be identified.
     * @return The associated view, if present.
     */
    protected abstract fun findView(source: SOURCE, id: IDENTIFIER): BASE_VIEW?

    /**
     * Creates a lazy property that will try to find the view identified by [id] in the
     * object linked to the delegate.
     *
     * If [ReadOnlyProperty.getValue] is called without the view present, it will throw an [IllegalStateException].
     *
     * @param V The required type of the view.
     * @param id The id associated with the view.
     */
    open fun <V : BASE_VIEW> bindView(id: IDENTIFIER): ReadOnlyProperty<SOURCE, V>
            = storeBinding<V>(CachedView(this, id, ViewAbsentBehavior.THROW_ERROR))

    /**
     * Creates a lazy property that will try to find the view identified by [id] in the
     * object linked to the delegate.
     *
     * If [ReadOnlyProperty.getValue] is called without the view present, it will return null.
     *
     * @param V The required type of the view.
     * @param id The id associated with the view.
     */
    open fun <V : BASE_VIEW> bindOptionalView(id: IDENTIFIER): ReadOnlyProperty<SOURCE, V?>
            = storeBinding<V>(CachedView(this, id, ViewAbsentBehavior.RETURN_NULL))

    /**
     * Resets the laziness property of all properties created using [bindView] and [bindOptionalView].
     *
     * This will remove any reference they held to the associated view and force a lookup on the next
     * [ReadOnlyProperty.getValue].
     */
    fun unbind() {
        bindings.forEach {
            val binding = it.get()
            binding?.unbind()
        }
    }

    /**
     * A helper delegate that adds extension methods to the specified [SOURCE] class.
     * These methods will delegate to the provided [binder] object.
     *
     * @param SOURCE See [LookupBinder.SOURCE]
     * @param BASE_VIEW See [LookupBinder.BASE_VIEW]
     * @param IDENTIFIER See [LookupBinder.IDENTIFIER]
     * @param BINDER The implementation of [LookupBinder] that is associated with this delegate.
     * @property binder The binder to use for view lookups and caching.
     */
    interface Delegate<
            in SOURCE,
            BASE_VIEW : Any,
            in IDENTIFIER,
            out BINDER : LookupBinder<SOURCE, BASE_VIEW, IDENTIFIER>> {
        val binder: BINDER

        /**
         * @see [LookupBinder.bindView]
         */
        fun <V : BASE_VIEW> SOURCE.bindView(ident: IDENTIFIER)
                = binder.bindView<V>(ident)

        /**
         * @see [LookupBinder.bindOptionalView]
         */
        fun <V : BASE_VIEW> SOURCE.bindOptionalView(ident: IDENTIFIER)
                = binder.bindOptionalView<V>(ident)
    }

    /**
     * Registers the given cached view for future unbinding
     */
    private fun <VIEW_OUT : BASE_VIEW> storeBinding(
            cachedView: CachedView<SOURCE, BASE_VIEW, IDENTIFIER, VIEW_OUT>
    ): ReadOnlyProperty<SOURCE, VIEW_OUT> {
        bindings += WeakReference<CachedView<*, *, *, *>>(cachedView)
        return cachedView
    }

    /**
     * Controls the behavior of [LookupBinder.findView] in the absence of a view.
     */
    private enum class ViewAbsentBehavior {
        /**
         * The view will be returned as-is
         */
        RETURN_NULL,
        /**
         * An [IllegalStateException] will be thrown with details about the failed request.
         */
        THROW_ERROR
    }

    private class CachedView<in SOURCE, out BASE_VIEW : Any, IDENTIFIER, out VIEW_OUT : BASE_VIEW?>(
            private val lookupBinder: LookupBinder<SOURCE, BASE_VIEW, IDENTIFIER>,
            private val ident: IDENTIFIER,
            private val absentBehavior: ViewAbsentBehavior
    ) : ReadOnlyProperty<SOURCE, VIEW_OUT> {
        private object EMPTY

        private var value: Any? = EMPTY

        override fun getValue(thisRef: SOURCE, property: KProperty<*>): VIEW_OUT {
            if (value === EMPTY) {
                value = lookupBinder.findView(thisRef, ident)

                if (value == null && absentBehavior !== ViewAbsentBehavior.RETURN_NULL) {
                    throw error("View ID $ident for '${property.name}' not found.")
                }
            }

            @Suppress("UNCHECKED_CAST")
            return value as VIEW_OUT
        }

        internal fun unbind() {
            value = EMPTY
        }
    }
}