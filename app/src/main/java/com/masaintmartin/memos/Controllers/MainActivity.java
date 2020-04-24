package com.masaintmartin.memos.Controllers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.masaintmartin.memos.Adapters.MemosAdapter;
import com.masaintmartin.memos.Controllers.Memos.Add_Edit_Memo;
import com.masaintmartin.memos.ItemTouchHelper.MemosItemTouchHelperCallback;
import com.masaintmartin.memos.Models.Database.MemoDatabase;
import com.masaintmartin.memos.Models.Memo;
import com.masaintmartin.memos.R;
import com.masaintmartin.memos.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView memosRecyclerView;
    private MemosAdapter memosAdapter;
    private RecyclerView.LayoutManager memosLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    MemoDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = MemoDatabase.getInstance(this);

        setContentView(R.layout.activity_main);

        List<Memo> memos = database.memoDao().getMemos();

        memosRecyclerView = findViewById(R.id.memosRecyclerView);
        memosRecyclerView.setHasFixedSize(true);

        memosLayoutManager = new LinearLayoutManager(this);
        memosRecyclerView.setLayoutManager(memosLayoutManager);

        memosAdapter = new MemosAdapter(memos);
        memosRecyclerView.setAdapter(memosAdapter);
        memosRecyclerView.addItemDecoration(new DividerItemDecoration(memosRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        itemTouchHelper = new ItemTouchHelper(new MemosItemTouchHelperCallback(memosAdapter));
        itemTouchHelper.attachToRecyclerView(memosRecyclerView);

        super.findViewById(R.id.addMemo).setOnClickListener(StartAddMemo(this));
    }

    private View.OnClickListener StartAddMemo(final Context context){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_Edit_Memo.class);
                startActivityForResult(intent, Constants.ADD_EDIT_MEMO_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.ADD_EDIT_MEMO_REQUEST_CODE)
        {
            if(data != null){
                boolean isCreation = data.getBooleanExtra(Constants.IS_CREATION_KEY, false);

                if(resultCode == RESULT_CANCELED){
                    if (isCreation) {
                        Toast.makeText(getApplicationContext(), "Add canceled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update canceled", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (isCreation) {
                        Toast.makeText(getApplicationContext(), "Memo added", Toast.LENGTH_SHORT).show();
                        this.memosAdapter.onItemInsert(MemoDatabase.getInstance(getApplicationContext()).memoDao().getLastMemo());
                    } else {
                        int position = data.getIntExtra(Constants.UPDATE_POSITION, -1);
                        int id = data.getIntExtra(Constants.UPDATE_ID, -1);

                        if(position != -1 && id != -1){
                            Toast.makeText(getApplicationContext(), "Memo updated", Toast.LENGTH_SHORT).show();
                            Memo memoUpdated = database.memoDao().getMemo(id);
                            if(memoUpdated != null) {
                                this.memosAdapter.onItemUpdate(memoUpdated, position);
                            }
                            else Toast.makeText(getApplicationContext(), "Error fetching updated Memo", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error updating", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            else{
                if(resultCode == RESULT_CANCELED)  Toast.makeText(getApplicationContext(), "Operation canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
