package com.example.ankit.smartprix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.Prod_detail_adapter;
import app.AppController;
import model.Prod_detail_store;
import model.Product;

public class Prod_detail extends AppCompatActivity {

    String id;
    Intent i;
    private final int kill = 1;
    private final String LOG_TAG = Prod_detail.class.getSimpleName();
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    TextView prod_detail_name;
    NetworkImageView prod_detail_pic;
    TextView prod_detail_price;
    ListView prod_detail_stores;
    TextView prod_detail_info;
    JSONObject jobj;
    JSONArray jarr;
    List<Prod_detail_store> storeItems = new ArrayList<>();
    private Prod_detail_adapter adapter;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        i = getIntent();
        id = i.getStringExtra("product_id");

        Log.e(LOG_TAG,id);

        prod_detail_name = (TextView) findViewById(R.id.prod_detail_name);
        prod_detail_pic = (NetworkImageView) findViewById(R.id.prod_detail_pic);
        prod_detail_price = (TextView) findViewById(R.id.prod_detail_price);
        prod_detail_stores = (ListView) findViewById(R.id.prod_detail_stores);
        prod_detail_info = (TextView) findViewById(R.id.prod_detail_info);
        prod_detail_info.setVisibility(View.GONE);

        adapter = new Prod_detail_adapter(Prod_detail.this , storeItems);
        prod_detail_stores.setAdapter(adapter);

        final String BASE_URL = "http://api.smartprix.com/simple/v1?";
        final String TYPE = "type";
        final String KEY = "key";
        final String ID = "id";
        final String INDENT = "indent";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(TYPE, "product_full")
                .appendQueryParameter(KEY, "NVgien7bb7P5Gsc8DWqc")
                .appendQueryParameter(ID, id)
                .appendQueryParameter(INDENT, "1")
                .build();


        String url = builtUri.toString();
        Log.e(LOG_TAG, "Built URI" + url);

        jsondata(url);

    }

    public void jsondata(String url) {
        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            jobj = response.getJSONObject("request_result");
                            prod_detail_name.setText(jobj.getString("name"));
                            prod_detail_price.setText("Rs "+ jobj.getString("price"));
                            prod_detail_pic.setImageUrl(jobj.getString("img_url"), imageLoader);
                           jarr = jobj.getJSONArray("prices");

                            if(jarr.length() == 0)
                            {
                                prod_detail_info.setVisibility(View.VISIBLE);
                                prod_detail_info.setText("No store currently sells this item. Please look for something else");
                            }

                            Prod_detail_store s;

                            for (int i = 0; i < jarr.length(); i++) {

                                s = new Prod_detail_store();
                                JSONObject Obj = jarr.getJSONObject(i);
                                Log.e(LOG_TAG,Obj.getString("store_name"));
                                s.setStore_price("Rs "+ Obj.getString("price"));
                                s.setStore_url(Obj.getString("logo"));
                                s.setLink_to_store(Obj.getString("link"));

                                storeItems.add(s);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        AppController.getInstance().addToRequestQueue(obj);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.notifyDataSetChanged();
        finishActivity(kill);

    }


    @Override
    protected void onPause() {
        super.onPause();
        adapter.notifyDataSetChanged();
        finishActivity(kill);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            //  mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));
            mSearchAction.setIcon(ContextCompat.getDrawable(Prod_detail.this, R.drawable.ic_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String text = edtSeach.getText().toString();
                        dosearch(text);
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            //  mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));
            mSearchAction.setIcon(ContextCompat.getDrawable(Prod_detail.this, R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    private void dosearch(String text){
        Intent i = new Intent(Prod_detail.this , Search_Activity.class);
        i.putExtra("query", text);
        startActivity(i);
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
    }
}
