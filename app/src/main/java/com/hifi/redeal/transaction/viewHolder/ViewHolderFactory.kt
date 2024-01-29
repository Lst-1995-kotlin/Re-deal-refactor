package com.hifi.redeal.transaction.viewHolder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderFactory {
    fun create(parent: ViewGroup): RecyclerView.ViewHolder
}

