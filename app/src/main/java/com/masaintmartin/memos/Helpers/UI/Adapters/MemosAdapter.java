package com.masaintmartin.memos.Helpers.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.masaintmartin.memos.Controllers.Memos.Add_Edit_Memo;
import com.masaintmartin.memos.Models.Database.MemoDatabase;
import com.masaintmartin.memos.Models.Memo;
import com.masaintmartin.memos.R;
import com.masaintmartin.memos.Helpers.Constants;
import com.masaintmartin.memos.Services.HttpBin_Service;

import java.util.List;

public class MemosAdapter extends RecyclerView.Adapter<MemosAdapter.MemoItemViewHolder>{
    private List<Memo> memos;

    public static class MemoItemViewHolder extends RecyclerView.ViewHolder {

        private Memo memo;

        public TextView textView;

        public MemoItemViewHolder(View v) {
            super(v);

            this.textView = v.findViewById(R.id.memo_title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Touch Memo", Toast.LENGTH_SHORT).show();

                    HttpBin_Service.Post(v.getContext(), memo);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(Constants.POSITION_MEMO, getAdapterPosition());
                    editor.apply();

                    Intent intent = new Intent(v.getContext(), Add_Edit_Memo.class);
                    intent.putExtra(Constants.UPDATE_ID, memo.getId());
                    intent.putExtra(Constants.UPDATE_POSITION, getAdapterPosition());
                    ((Activity) v.getContext()).startActivityForResult(intent, Constants.ADD_EDIT_MEMO_REQUEST_CODE);
                }
            });
        }

        public void setMemo(Memo m) {
            memo = m;
            textView.setText(memo.getTitle());
        }

    }

    public MemosAdapter(List<Memo> myDataset) {
        this.memos = myDataset;
    }

    @Override
    public MemoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memos_list_memoitem, parent, false);

        MemoItemViewHolder vh = new MemoItemViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MemoItemViewHolder holder, int position) {
        holder.setMemo(memos.get(position));
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public void onItemMove(int originalPosition, int newPosition){
        Memo original = this.memos.get(originalPosition);
        Memo newMemo = this.memos.get(newPosition);

        this.memos.set(originalPosition, newMemo);
        this.memos.set(newPosition, original);

        this.notifyItemChanged(originalPosition);
        this.notifyItemChanged(newPosition);
    }

    public void onItemDismiss(int itemPosition, Context context){
        MemoDatabase.getInstance(context).memoDao().deleteMemo(this.memos.get(itemPosition).getId());
        this.memos.remove(itemPosition);
        this.notifyItemRemoved(itemPosition);
    }

    public void onItemInsert(Memo memo){
        this.memos.add(memo);
        this.notifyItemInserted(this.memos.size());
    }

    public void onItemUpdate(Memo memo, int position){
        this.memos.set(position, memo);
        this.notifyItemChanged(position);
    }

}
