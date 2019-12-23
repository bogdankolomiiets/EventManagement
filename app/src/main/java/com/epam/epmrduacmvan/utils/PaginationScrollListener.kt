package com.epam.epmrduacmvan.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    var isLoaded = true
    var totalItemCount = 0
    var lastVisibleItem = 0
    var previousTotal = 0

    init {
        this.subscribeToResults()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //if user swipes up
        if (dy > 0) {
            totalItemCount = layoutManager.itemCount
            lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
            if (isLoaded && !isLastPage() && totalItemCount > previousTotal) {
                isLoaded = false
                previousTotal = totalItemCount
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                if (!isLoaded && lastVisibleItem.inc() == totalItemCount && !recyclerView.canScrollVertically(1)) {
                    if (isOnline(recyclerView.context)) {
                        loadMoreItems()
                    }
                }
            }
        }
    }

    abstract fun isLastPage(): Boolean

    abstract fun subscribeToResults()

    abstract fun loadMoreItems()
}