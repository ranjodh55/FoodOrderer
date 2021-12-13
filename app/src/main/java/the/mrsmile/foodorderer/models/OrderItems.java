package the.mrsmile.foodorderer.models;

public class OrderItems {

    String title,desc,price;
    int image;

    public OrderItems(String title,String desc,String price, int image) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }
}
