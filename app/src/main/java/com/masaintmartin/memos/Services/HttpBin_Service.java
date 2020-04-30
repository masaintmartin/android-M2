package com.masaintmartin.memos.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.masaintmartin.memos.Helpers.Constants;
import com.masaintmartin.memos.Models.Memo;
import com.masaintmartin.memos.Models.MemoIn;

import cz.msebera.android.httpclient.Header;

public class HttpBin_Service {

    public static void Post(final Context context, final Memo memo) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("memo", memo.getTitle());

        client.post("http://httpbin.org/post", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String retour = new String(response);
                MemoIn memoIn = new Gson().fromJson(retour, MemoIn.class);

                Log.i(Constants.HTTPBIN_SERVICE_TAG, retour);
                Toast.makeText(context, memoIn.form.memo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(Constants.HTTPBIN_SERVICE_TAG, e.toString());
            }

        });
    }
}
