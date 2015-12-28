package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ankit.smartprix.R;

import java.util.List;

import app.AppController;
import model.Product;

/**
 * Created by Ankit on 25-Dec-15.
 */
public class Prod_list_adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> productItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public Prod_list_adapter(Activity activity, List<Product> productItems) {
        this.activity = activity;
        this.productItems = productItems;
    }

    @Override
    public int getCount() {
        return productItems.size();
    }

    @Override
    public Object getItem(int position) {
        return productItems.get(position);
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
            convertView = inflater.inflate(R.layout.prod_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView prod_pic = (NetworkImageView) convertView
                .findViewById(R.id.prod_pic);
        TextView prod_name = (TextView) convertView.findViewById(R.id.prod_name);
        TextView prod_price = (TextView) convertView.findViewById(R.id.prod_price);

        Product p = productItems.get(position);

        prod_pic.setImageUrl(p.getProd_image_url(), imageLoader);
        prod_name.setText(p.getProd_name());
        prod_price.setText(p.getProd_price());

        return convertView;

    }
}
