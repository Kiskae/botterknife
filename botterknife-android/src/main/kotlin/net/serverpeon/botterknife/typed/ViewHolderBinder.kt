package net.serverpeon.botterknife.typed

import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.View
import net.serverpeon.botterknife.AndroidBinder

/**
 * View Binder for [RecyclerView.ViewHolder].
 */
@RequiresApi(api = 7)
class ViewHolderBinder : AndroidBinder<RecyclerView.ViewHolder>() {
    override fun findView(source: RecyclerView.ViewHolder, id: Int): View? {
        return source.itemView.findViewById(id)
    }

    interface Delegate : AndroidBinder.Delegate<RecyclerView.ViewHolder, ViewHolderBinder>
}