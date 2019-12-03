package com.epam.epmrduacmvan.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private var isLoading = true
    private var totalItemCount = 0
    private var lastVisibleItem = 0
    private var previousTotal = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //if user swipes up
        if (dy > 0) {
            totalItemCount = layoutManager.itemCount
            lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

            if (isLoading && !isLastPage() && totalItemCount > previousTotal) {
                isLoading = false
                previousTotal = totalItemCount
            }

//            if (!isLoading && lastVisibleItem.inc() == totalItemCount) {
//                if (isOnline(recyclerView.context)) {
//                    loadMoreItems()
//                    isLoading = true
//                }
//            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                if (!isLoading && lastVisibleItem.inc() == totalItemCount && !recyclerView.canScrollVertically(1)) {
                    if (isOnline(recyclerView.context)) {
                        loadMoreItems()
                        isLoading = true
                    }
                }
            }
        }
    }

    abstract fun isLastPage(): Boolean

    abstract fun loadMoreItems()
}