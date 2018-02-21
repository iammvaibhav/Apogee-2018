package example.aditya.com.sms;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static example.aditya.com.sms.OrganisersFragment.cashInHand;
import static example.aditya.com.sms.OrganisersFragment.conversion;
import static example.aditya.com.sms.ProfileFragment.UserStocksData;
import static example.aditya.com.sms.URLs.KEY;
import static example.aditya.com.sms.URLs.URL_BUY_STOCK;
import static example.aditya.com.sms.URLs.URL_CONV_RATE;
import static example.aditya.com.sms.URLs.URL_LOGIN;
import static example.aditya.com.sms.URLs.URL_SELL_STOCK;
import static example.aditya.com.sms.URLs.URL_USER_DATA;

//import static example.aditya.com.sms.URLs.allStocksList;

public class MainActivity extends AppCompatActivity {

    public static SlidingLayer slidingLayer;
    ImageView close;
    static ImageView plus;
    ImageView minus;
    static TextView itemName;
    static TextView desc;
    static TextView cost;
    static TextView quan;
    static TextView currency;

    static Button buy;


    final static String USERNAME ="";
    final static String EMAIL ="";

    static String Rupees = "\u20B9 ";
    static String Dollar = "$ " ;

    static int quantity = 0;
    static int price;
    int max;
  static  String username = "test1";
  static  String email = "test1@gmail.com";
    TextView money_tv;
    static Stocks current_trading_stock;

   public static int cash_inhand;
   public static double conv_rate = 64.0;
   static boolean buying = true;
   public static boolean marketOpen = false;
    static ProgressBar progressBar;

    private void getData() {
        login(username,email);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("default", MODE_PRIVATE);
        username = prefs.getString("PREF_KEY_CURRENT_USER_USERNAME", "");
        email = prefs.getString("PREF_KEY_CURRENT_USER_USERNAME", "") + "@pilani.bits-pilani.ac.in";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#00aeef"));
        }

        getData();
        close = (ImageView)findViewById(R.id.close);
        plus = (ImageView)findViewById(R.id.plus);
        minus = (ImageView)findViewById(R.id.minus);
        itemName = (TextView) findViewById(R.id.stock_name);
        cost = (TextView) findViewById(R.id.cost_per_stock);
        quan = (TextView) findViewById(R.id.quantity);
        currency = (TextView) findViewById(R.id.currency);
        buy = (Button) findViewById(R.id.add_to_cart2);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingLayer.closeLayer(true);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
       // progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                quan.setText(String.valueOf(quantity));

                 if(current_trading_stock.isRupees()) {
                     double totalprice = (double) quantity*price;
                     cost.setText("₹ "+String.format("%.2f", totalprice));
                }
                else{
                     double totalprice = (double) quantity*price/conv_rate;
                     cost.setText("$ "+String.format("%.2f", totalprice));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>1){  quantity--;

                    quan.setText(String.valueOf(quantity));
                    if(current_trading_stock.isRupees()) {
                        double totalprice = (double) quantity*price;
                        cost.setText("₹ "+String.format("%.2f", totalprice));
                    }
                    else{
                        double totalprice = (double) quantity*price/conv_rate;
                        cost.setText("$ "+String.format("%.2f", totalprice));
                    }
                }}
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                if(buying){

                if(cash_inhand >= quantity*current_trading_stock.getCurrentPrice()){
                    BuyStocks(email,quantity,current_trading_stock.getId());
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Not enough money available.", Toast.LENGTH_SHORT).show();
                }
                Log.e("Buying",current_trading_stock.toString());
            }
            else{
                    Log.e("Selling", current_trading_stock.toString());
                    SellStocks(email,quantity,current_trading_stock.getId());
            }}
        });


        slidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);
        slidingLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_home)
                    selectedFragment = new ProfileFragment();
                else if (item.getItemId() == R.id.navigation_dashboard)
                    selectedFragment = new OrganisersFragment();
                else if (item.getItemId() == R.id.navigation_notifications)
                    selectedFragment = new LeaderBoardFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.commit();
                return true;
        }
    };


   static void BuyButtonClicked(Stocks stock){
        progressBar.setVisibility(View.INVISIBLE);

        current_trading_stock = stock;
        buying = true;
        buy.setEnabled(true);
        if(slidingLayer.isClosed())
        {
            price = stock.getCurrentPrice();
           quantity = 0;
           itemName.setText(stock.getName());
           buy.setText("BUY");
           plus.performClick();
           slidingLayer.openLayer(true);
        }
        else{slidingLayer.closeLayer(true);
        BuyButtonClicked(stock);
        }
    }
    static void SellButtonClicked(Stocks stock){
        progressBar.setVisibility(View.INVISIBLE);
        price = stock.getCurrentPrice();
        current_trading_stock = stock;
        buy.setEnabled(true);
        buying = false;
        if(slidingLayer.isClosed())
        {
            buy.setText("SELL");
            quantity = 0;
            itemName.setText(stock.getName());
            plus.performClick();
            slidingLayer.openLayer(true);

        }
        else{slidingLayer.closeLayer(true);
            BuyButtonClicked(stock);}

    }
    static void UserData(final String email) {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    map.put("email", email);

                    AndroidNetworking.post(URL_USER_DATA).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String email = response.getString("email-id");
                                        int balance = response.getInt("user_balance");
                                        String username = response.getString("username");

                                        cash_inhand = (int)balance;
                                        if(cashInHand!=null)
                                        {
                                            cashInHand.setText("₹ "+String.format("%.2f", (double)cash_inhand));
                                        }
                                        convRate();

                                        Log.d("Response", email+" "+balance+" "+username);
                                    } catch (Exception e) {
                                        Log.e("error", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      try {
                          if (cashInHand != null)
                              cashInHand.setText("₹ " + String.format("%.2f", (double) cash_inhand));

                      }
                      catch (Exception e){}
                  }
              });
            }
        });

        thread.start();
    }
    static void convRate() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    //  map.put("email", email);

                    AndroidNetworking.post(URL_CONV_RATE).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                       int conversion_rate = response.getInt("conversion_rate");
                                        double conv_rate = conversion_rate/100.0;
                                        conversion.setText("1 $ = "+ String.format("%.2f", (double) conv_rate) + " "+Rupees);
                                        Log.d("Response", response.toString());
                                    } catch (Exception e) {
                                        Log.e("error in conv_rate", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
     void BuyStocks(final String email, final int units, final int stock_id) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    map.put("email",email);
                    map.put("units",String.valueOf(units));

                    AndroidNetworking.post(URL_BUY_STOCK+stock_id).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("Response", response.toString());
//                                        showToast(response.get("message"))
                                        if(response.toString().contains("SUCCESS"))
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(MainActivity.this, "Stock successfully purchased.", Toast.LENGTH_SHORT).show();
                                        }

                                        closeDrawer();
                                        UserData(email);
                                        UserStocksData(email);
                                    } catch (Exception e) {
                                        Log.e("error", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private static void closeDrawer() {
        if(slidingLayer.isOpened()) slidingLayer.closeLayer(true);
    }
    void SellStocks(final String email, final int units, final int stock_id) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    map.put("email",email);
                    map.put("units",String.valueOf(units));

                    AndroidNetworking.post(URL_SELL_STOCK+stock_id).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("Response", response.toString());
                                        //TODO
                                        if(response.toString().contains("SUCCESS")) {
                                            progressBar.setVisibility(View.INVISIBLE);

                                            Toast.makeText(MainActivity.this, "Stock successfully sold. Please refresh the page.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "Not enough Stocks available.", Toast.LENGTH_SHORT).show();

                                        }
                                        UserData(email);
                                        closeDrawer();
                                        UserStocksData(email);

                                    } catch (Exception e) {
                                        Log.e("error", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    static void login(final String username, final String email) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", username);
                    map.put("email", email);

                    AndroidNetworking.post(URL_LOGIN).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        UserData(email);

                                    } catch (Exception e) {
                                        Log.e("error", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
