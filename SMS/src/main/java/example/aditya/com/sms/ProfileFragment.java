package example.aditya.com.sms;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static example.aditya.com.sms.MainActivity.Rupees;
import static example.aditya.com.sms.MainActivity.UserData;
import static example.aditya.com.sms.MainActivity.cash_inhand;
import static example.aditya.com.sms.URLs.KEY;
import static example.aditya.com.sms.URLs.URL_USER_STOCKS_DATA;


public class ProfileFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private static ProfileAdapter adapter;
    public static ArrayList<Stocks> my_stocks;
    public static TextView worth_tv;
    public static TextView worth_tv1;
    public static TextView worth_tv2;
    int stocks_worth;
    String currency = Rupees;
    TextView cur;
    TextView cur1;
    TextView cur2;
    Context ctx;

      @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mRecyclerView = rootView.findViewById(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
       // data = type1;
        worth_tv = rootView.findViewById(R.id.worth);
        worth_tv1 = rootView.findViewById(R.id.worth1);
        worth_tv2 = rootView.findViewById(R.id.worth2);
        cur = rootView.findViewById(R.id.cur);
      //  worth_tv1.setText(String.valueOf(cash_inhand));
        worth_tv1.setText(String.format("%.2f",(double)cash_inhand));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new ProfileAdapter(getContext(),my_stocks);
        mRecyclerView.setAdapter(adapter);
        UserStocksData(email);
        UserData(email);

      //  calculateStockValue();
        return rootView;
                //inflater.inflate(R.layout.fragment_profile, container, false);
    }


    String username = "test1";
    String email = "test1@gmail.com";

    static void UserStocksData(final String email) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    map.put("email", email);

                    AndroidNetworking.post(URL_USER_STOCKS_DATA).addBodyParameter(map).build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                            my_stocks = new ArrayList<>();
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonobject = response.getJSONObject(i);
                                            Log.e("JsonObj",response.toString());
                                            String name = jsonobject.getString("name");
                                            int price = jsonobject.getInt("price");
                                            int no_stocks = jsonobject.getInt("num");
                                            int purchase_price = jsonobject.getInt("average_price");
                                            String market_type = jsonobject.getString("market_type");
                                            int pChange_int  = jsonobject.getInt("price_trend");
                                            int id = jsonobject.getInt("id");
                                            double pChange = pChange_int/100.0;
                                            boolean isRupees = false;
                                            if(market_type.equals("BSE") || market_type.equals("Both")) isRupees = true;
                                            Stocks stock = new Stocks(name,price,no_stocks,market_type,pChange, isRupees,purchase_price,id);
                                            my_stocks.add(stock);
                                            adapter.notifyDataSetChanged();
                                            Log.d("stock", name+" "+market_type +" "+price+" "+no_stocks);
                                        }

                                    }catch (Exception e) {
                                        Log.e("error", e.getMessage());
                                    }
                                    adapter.notifyDataSetChanged();
                                  //  progressDialog.dismiss();
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());

                                }
                            });

                    calculateStockValue();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private static void calculateStockValue() {
        int m = 0;
        if (my_stocks != null) {
            for (int i = 0; i < my_stocks.size(); i++) {
                m = m + (my_stocks.get(i).getCurrentPrice() * my_stocks.get(i).getWithMe());
            }
            worth_tv2.setText(String.format("%.2f", (double) m));
            worth_tv.setText(String.format("%.2f", (double) m + (double) cash_inhand));

            //  worth_tv.setText(String.valueOf(m+cash_inhand));
        }
    }

}
