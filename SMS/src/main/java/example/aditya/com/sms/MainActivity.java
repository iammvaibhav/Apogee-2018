package example.aditya.com.sms;

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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static example.aditya.com.sms.OrganisersFragment.cashInHand;
import static example.aditya.com.sms.OrganisersFragment.conversion;
import static example.aditya.com.sms.URLs.KEY;
import static example.aditya.com.sms.URLs.URL_BUY_STOCK;
import static example.aditya.com.sms.URLs.URL_CONV_RATE;
import static example.aditya.com.sms.URLs.URL_GAME_SWITCH;
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

    LottieAnimationView animationView;

    final static String USERNAME ="";
    final static String EMAIL ="";

    static String Rupees = "\u20B9 ";
    static String Dollar = "$ " ;

    static int quantity = 0;
    static int price;
    int max;
    String username = "test1";
    String email = "test1@gmail.com";
    TextView money_tv;
    static Stocks current_trading_stock;

   public static int cash_inhand;
   public static int conv_rate;
   static boolean buying = true;

    private void getData() {
        login(username,email);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#00aeef"));
        }

        getData();

        close = (ImageView)findViewById(R.id.close);
        plus = (ImageView)findViewById(R.id.plus);
        minus = (ImageView)findViewById(R.id.minus);
        itemName = (TextView) findViewById(R.id.stock_name);
     //   desc = (TextView) findViewById(R.id.description);
        cost = (TextView) findViewById(R.id.cost_per_stock);
        quan = (TextView) findViewById(R.id.quantity);
        currency = (TextView) findViewById(R.id.currency);
        buy = (Button) findViewById(R.id.add_to_cart2);

        UserData(email);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingLayer.closeLayer(true);
            }
        });

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
                if(buying){

                if(cash_inhand >= quantity*current_trading_stock.getCurrentPrice()){
                    BuyStocks(email,quantity,current_trading_stock.getId());
                }
                else{
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

        convRate();
        gameSwitch();
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
                                        if(cashInHand!=null) cashInHand.setText(Rupees + String.valueOf(cash_inhand));

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
                      if(cashInHand!=null) cashInHand.setText(String.valueOf(cash_inhand));
                  }
              });
            }
        });

        thread.start();
    }
    static void gameSwitch() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                  //  map.put("email", email);

                    AndroidNetworking.post(URL_GAME_SWITCH).addBodyParameter(map).build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        Log.d("Response", response.toString());
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
                                        conv_rate = conversion_rate;
                                        conversion.setText("1 $ = "+ String.valueOf(conv_rate) + " "+Rupees);
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
    static void BuyStocks(final String email, final int units, final int stock_id) {

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
                                        closeDrawer();
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
    private static void closeDrawer() {
        if(slidingLayer.isOpened()) slidingLayer.closeLayer(true);
    }
    static void SellStocks(final String email, final int units, final int stock_id) {

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
                                        UserData(email);
                                        closeDrawer();
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
                                        Log.d("Response", response.toString());
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
