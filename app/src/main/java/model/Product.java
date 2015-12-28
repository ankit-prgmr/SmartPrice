package model;

/**
 * Created by Ankit on 25-Dec-15.
 */
public class Product {

    private String prod_name;
    private String prod_image_url;
    private String prod_price;
    private String prod_id;

    public String getProd_id(){ return prod_id;}

    public void setProd_id(String prod_id) { this.prod_id = prod_id; }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_image_url() {
        return prod_image_url;
    }

    public void setProd_image_url(String prod_image_url) {
        this.prod_image_url = prod_image_url;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }
}
