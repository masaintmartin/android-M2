package com.masaintmartin.memos.ItemTouchHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.masaintmartin.memos.Adapters.MemosAdapter;

public class MemosItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private MemosAdapter memosAdapter;

    public MemosItemTouchHelperCallback(MemosAdapter adapter){
        this.memosAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlagsUpDown = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int dragLeftRight = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        return makeMovementFlags(dragFlagsUpDown, dragLeftRight);
    }

    @Override
    public boolean isLongPressDragEnabled() { return true; }

    @Override
    public boolean isItemViewSwipeEnabled() { return true; }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        memosAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        memosAdapter.onItemDismiss(viewHolder.getAdapterPosition(), viewHolder.itemView.getContext());
    }
}
