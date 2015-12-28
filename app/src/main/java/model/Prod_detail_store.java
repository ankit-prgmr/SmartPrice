package model;

/**
 * Created by Ankit on 26-Dec-15.
 */
public class Prod_detail_store {

    private String store_url;
    private String store_price;
    private String link_to_store;

    public String  getLink_to_store(){
        return link_to_store;
    }

    public void setLink_to_store(String link_to_store){
        this.link_to_store = link_to_store;
    }

    public String getStore_url() {
        return store_url;
    }

    public void setStore_url(String store_url) {
        this.store_url = store_url;
    }

    public String getStore_price() {
        return store_price;
    }

    public void setStore_price(String store_price) {
        this.store_price = store_price;
    }
}
