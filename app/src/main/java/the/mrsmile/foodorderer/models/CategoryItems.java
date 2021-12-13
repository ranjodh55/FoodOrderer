package the.mrsmile.foodorderer.models;

public class CategoryItems {
    int image;
    String title , desc , price ;

    public CategoryItems(String title, String desc, String price,int image) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }
}
