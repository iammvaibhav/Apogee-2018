package example.aditya.com.sms;

/**
 * Created by aditya on 2/6/2018.
 */

class Stocks {
    String name;
    int currentPrice;
    int change;// increase = +1, decrease = -1, same =0;
    int withMe;
    String description;
    boolean isRupees;
    int id;
    int market_type ; //  BSE = +1, NYM = -1, both =0;
    double pChange;
    int purchase_price;

    public Stocks(String name, int currentPrice, int no_stocks, String market_type, double pChange, boolean isRupees, int purchase_price,int id) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.withMe = no_stocks;
         this.pChange = pChange;
        this.isRupees = isRupees;
        this.purchase_price = purchase_price;
        this.id = id;
    }


    public boolean isRupees() {
        return isRupees;
    }

    public void setRupees(boolean rupees) {
        isRupees = rupees;
    }

    public int getWithMe() {
        return withMe;
    }

    public void setWithMe(int withMe) {
        this.withMe = withMe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public Stocks(String name, int currentPrice, int change, boolean isRupees,int id) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.change = change;
        this.withMe =0;
        this.isRupees = isRupees;
    }

    public Stocks(String name, int currentPrice, int id, String market, double pChange, boolean isRupees) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.id = id;
        this.pChange = pChange;
        this.isRupees = isRupees;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMarket_type() {
        return market_type;
    }

    public void setMarket_type(int market_type) {
        this.market_type = market_type;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", change=" + change +
                ", withMe=" + withMe +
                ", description='" + description + '\'' +
                ", isRupees=" + isRupees +
                ", id=" + id +
                ", market_type=" + market_type +
                '}';
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public double getpChange() {
        return pChange;
    }

    public void setpChange(double pChange) {
        this.pChange = pChange;
    }
}
