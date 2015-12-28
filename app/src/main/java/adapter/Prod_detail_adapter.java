package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ankit.smartprix.R;

import java.util.List;

import app.AppController;
import model.Prod_detail_store;

/**
 * Created by Ankit on 26-Dec-15.
 */
public class Prod_detail_adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Prod_detail_store> storeItems;
    private String store_link;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Prod_detail_adapter(Activity activity, List<Prod_detail_store> storeItems) {
        this.activity = activity;
        this.storeItems = storeItems;
    }


    @Override
    public int getCount() {
        return storeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return storeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.prod_detail_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView store_logo = (NetworkImageView) convertView
                .findViewById(R.id.store_logo);
        TextView store_price = (TextView) convertView.findViewById(R.id.store_price);
        ImageView store_buy = (ImageView) convertView.findViewById(R.id.store_buy);


        Prod_detail_store s = storeItems.get(position);

        store_logo.setImageUrl(s.getStore_url(), imageLoader);
        store_price.setText(s.getStore_price());

        store_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                Prod_detail_store temp = (Prod_detail_store) getItem(position);
                store_link = temp.getLink_to_store();

                Toast.makeText(activity , store_link , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(store_link));
                activity.startActivity(i);
            }
        });

        return convertView;

    }
}
