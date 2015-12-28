package com.example.ankit.smartprix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.Prod_list_adapter;
import app.AppController;
import model.Product;

public class Prod_list extends AppCompatActivity {

    private final String LOG_TAG = Prod_list.class.getSimpleName();
    JSONObject jobj;
    JSONArray jarr;
    TextView category;
    private final int kill = 1;
    List<Product> products = new ArrayList<>();
    ListView prod_list;
    private Prod_list_adapter adapter;
    Intent i;
    String item;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        i = getIntent();
        item = i.getStringExtra("category");
        Log.e(LOG_TAG, item);

        category = (TextView) findViewById(R.id.category_label);
        category.setText(item);

        prod_list = (ListView) findViewById(R.id.prod_list);
        adapter = new Prod_list_adapter(Prod_list.this, products);
        prod_list.setAdapter(adapter);
        prod_list.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int start, int totalItemsCount) {

                String s = "" + start;

                final String BASE_URL = "http://api.smartprix.com/simple/v1?";
                final String TYPE = "type";
                final String KEY = "key";
                final String CATEGORY = "category";
                final String SEARCH_QUERY = "q";
                final String START = "start";
                final String INDENT = "indent";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(TYPE, "search")
                        .appendQueryParameter(KEY, "NVgien7bb7P5Gsc8DWqc")
                        .appendQueryParameter(CATEGORY, item)
                        .appendQueryParameter(SEARCH_QUERY, "3g")
                        .appendQueryParameter(START, s)
                        .appendQueryParameter(INDENT, "1")
                        .build();


                String url = builtUri.toString();
                Log.e(LOG_TAG, "Built URI" + url);

                jsondata(url);


            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });

        String init_url = build_init_url(item);
        jsondata(init_url);


    }

    @Override
    protected void onStop() {
        super.onStop();
        finishActivity(kill);
    }



    @Override
    protected void onPause() {
        super.onPause();
        finishActivity(kill);
    }

        public void jsondata(String url) {
            JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                jobj = response.getJSONObject("request_result");
                                jarr = jobj.getJSONArray("results");

                                Product p;

                                for (int i = 0; i < jarr.length(); i++) {

                                    p = new Product();
                                    JSONObject Obj = jarr.getJSONObject(i);
                                    p.setProd_name(Obj.getString("name"));
                                    p.setProd_price("Rs "+ Obj.getString("price"));
                                    p.setProd_image_url(Obj.getString("img_url"));
                                    p.setProd_id(Obj.getString("id"));

                                    products.add(p);

                                }


                                prod_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Product temp = products.get(position);
                                        String product_id = temp.getProd_id();

                                        Intent i  = new Intent(Prod_list.this , Prod_detail.class);
                                        i.putExtra("product_id",product_id);
                                        startActivity(i);
                                    }
                                });
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

        public String build_init_url(String item)
        {
            String s = ""+0 ;
            final String BASE_URL = "http://api.smartprix.com/simple/v1?";
            final String TYPE = "type";
            final String KEY = "key";
            final String CATEGORY = "category";
            final String SEARCH_QUERY = "q";
            final String START = "start";
            final String INDENT = "indent";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(TYPE, "search")
                    .appendQueryParameter(KEY, "NVgien7bb7P5Gsc8DWqc")
                    .appendQueryParameter(CATEGORY, item)
                    .appendQueryParameter(SEARCH_QUERY, "3g")
                    .appendQueryParameter(START, s)
                    .appendQueryParameter(INDENT, "1")
                    .build();


            String url = builtUri.toString();

            return url;
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
            mSearchAction.setIcon(ContextCompat.getDrawable(Prod_list.this, R.drawable.ic_search));

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
            mSearchAction.setIcon(ContextCompat.getDrawable(Prod_list.this, R.drawable.ic_close_search));

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
        Intent i = new Intent(Prod_list.this , Search_Activity.class);
        i.putExtra("query", text);
        startActivity(i);
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
    }
}


