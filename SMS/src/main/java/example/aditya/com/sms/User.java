package example.aditya.com.sms;

/**
 * Created by aditya on 2/11/2018.
 */

public class User {
    String name;
    int networth;
    int rank;

    public User(String name, int networth, int rank) {
        this.name = name;
        this.networth = networth;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetworth() {
        return networth;
    }

    public void setNetworth(int networth) {
        this.networth = networth;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", networth=" + networth +
                ", rank=" + rank +
                '}';
    }
}
