package com.ms.assignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shrawangupta on 05/05/2016.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView                mListView;
    private RecyclerView.LayoutManager  mLayoutManager;
    private List<DataModel>             mDataList;
    private boolean                     mUpdateRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (RecyclerView)findViewById(R.id.list);

        final MSEditText input = (MSEditText)findViewById(R.id.input);
        final EditText editText = input.getEditText();
        editText.addTextChangedListener(keywordWatcher);

    }

    private final TextWatcher keywordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            final MSEditText input = (MSEditText)findViewById(R.id.input);

            if (s.length() == 0) {
                input.setRefreshing(false);
                MSNetworkHandler.getInstance(MainActivity.this).cancelPendingRequests(null);
                Toast.makeText(MainActivity.this, "Please enter valid keyword", Toast.LENGTH_SHORT).show();
            } else if (s.length() <= 2) {
                input.setRefreshing(false);
                MSNetworkHandler.getInstance(MainActivity.this).cancelPendingRequests(null);
            } else {

                String keyword = input.getEditText().getText().toString();
                input.setRefreshing(true);
                loadItem(keyword);
            }
        }
    };

    private void loadItem(final String keyword) {

        String encodedKeyword = keyword;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url="https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch="+encodedKeyword;

        MSNetworkHandler.getInstance(this).cancelPendingRequests(null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    //progressDialog.cancel();
                    setRereshState(false);
                    final MSEditText input = (MSEditText)findViewById(R.id.input);
                    input.setRefreshing(false);

                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject queryObj = jsonObject.getJSONObject("query");
                    JSONObject pages = queryObj.getJSONObject("pages");

                    mDataList = jsonToList(pages);
                    pushDataToAdapter(keyword);
                } catch (Exception e) {
                    setRereshState(false);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                final MSEditText input = (MSEditText)findViewById(R.id.input);
                input.setRefreshing(false);
            }
        });


        stringRequest.setShouldCache(true);

        setRereshState(true);
        Log.d("MSAppScreen", "Sending request for keyword = "+keyword);
        MSNetworkHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private synchronized void setRereshState(boolean state) {
        synchronized (this) {
            mUpdateRequested = state;
        }
    }

    void pushDataToAdapter(final String keyword) {
        synchronized (this) {
            if(mUpdateRequested ==false)
            {
                mLayoutManager = new LinearLayoutManager(this);
                mListView.setLayoutManager(mLayoutManager);
                Log.d("MSAppScreen", "Updating list for keyword = "+keyword);
                mListView.setAdapter(new ImageSearchAdapter(this, mDataList));
            }
        }
    }

    public static List<DataModel> jsonToList(JSONObject object) throws JSONException {

        List<DataModel> list = null;
        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            JSONObject value = object.getJSONObject(key);

            if(value instanceof JSONObject) {
                DataModel model = new DataModel();
                model.setTitle(value.getString("title"));

                try {
                    JSONObject thumbnail = value.getJSONObject("thumbnail");
                    if(thumbnail!=null) {
                        model.setLogo(thumbnail.getString("source"));
                    }
                } catch(JSONException e) {
                    Log.e("MSApp", "Thumbnail not found for key = "+key);
                }

                if(list==null)
                    list = new ArrayList<DataModel>(5);

                list.add(model);
            }
        }
        return list;
    }

}
