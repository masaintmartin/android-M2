package com.masaintmartin.memos.Controllers.Memos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.masaintmartin.memos.Models.Database.MemoDatabase;
import com.masaintmartin.memos.Models.Memo;
import com.masaintmartin.memos.R;
import com.masaintmartin.memos.Utils.Constants;

public class Add_Edit_Memo extends AppCompatActivity {

    @Nullable private Memo memo;

    private Boolean isCreation = false;

    private int position;

    private EditText titleTextView;
    private Button submit_memo;
    private Button cancel_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memos_add_edit);

        titleTextView = (EditText) findViewById(R.id.memo_title);
        submit_memo = (Button) findViewById(R.id.submit_memo);
        cancel_memo = (Button) findViewById(R.id.cancel_memo);

        // get the intent that we have passed from ActivityOne
        Intent intent = getIntent();

        // get the extra value
        int memoId = intent.getIntExtra(Constants.EXTRA_CODE_MEMO_ID, -1);
        position = intent.getIntExtra(Constants.UPDATE_POSITION, -1);
        if(memoId != -1 && position != -1) {
            this.memo = MemoDatabase.getInstance(this).memoDao().getMemo(memoId);
        }
        else{
            this.memo = new Memo();
            isCreation = true;
        }

        SetFormData();

        submit_memo.setOnClickListener(endForm(getApplicationContext()));
        cancel_memo.setOnClickListener(cancelForm());
    }

    private void SetFormData() {
        this.titleTextView.setText(this.memo.getTitle());
    }

    private void GetFormData() {
        this.memo.setTitle(this.titleTextView.getText().toString());
    }

    private View.OnClickListener endForm(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFormData();

                Intent intent = new Intent();
                intent.putExtra(Constants.IS_CREATION_KEY, isCreation);

                if(isCreation) MemoDatabase.getInstance(context).memoDao().insertMemo(memo);
                else {
                    MemoDatabase.getInstance(context).memoDao().updateMemo(memo);
                    intent.putExtra(Constants.UPDATE_POSITION, position);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }
    private View.OnClickListener cancelForm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.IS_CREATION_KEY, isCreation);

                setResult(RESULT_CANCELED, intent);
                finish();
            }
        };
    }
}
