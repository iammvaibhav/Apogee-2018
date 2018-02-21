package example.aditya.com.sms;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static example.aditya.com.sms.URLs.KEY;
import static example.aditya.com.sms.URLs.URL_STOCKS_DATA;


public class IndianStockFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static RecyclerViewAdapter adapter;
    public static ArrayList<Stocks> stocksArrayList;
    Thread t;

      @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    if(t==null) {
    System.out.print("starting thread");
    Thread t = new Thread() {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(30000);  //30 sec
                    AllStockData();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    t.start();
}

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         final View rootView = inflater.inflate(R.layout.fragment_market, container, false);
         mRecyclerView = rootView.findViewById(R.id.recycler_view_recycler_view);
         stocksArrayList = new ArrayList<>();
         adapter = new RecyclerViewAdapter(getContext(),stocksArrayList,0);
         mRecyclerView.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
         DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        AllStockData();
         return rootView;
     }


    static void AllStockData() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    AndroidNetworking.post(URL_STOCKS_DATA).addBodyParameter(map).build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        stocksArrayList.clear();
                                         for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonobject = response.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            int price = jsonobject.getInt("price");
                                            int id = jsonobject.getInt("id");
                                            int pChange_int  = jsonobject.getInt("price_trend");
                                            double pChange = pChange_int/100.0;
                                            String market_type = jsonobject.getString("market_type");
                                            boolean isRupees = false;
                                            if(market_type.equals("BSE") || market_type.equals("Both")) isRupees = true;

                                            Stocks stock = new Stocks(name,price,id,market_type,pChange,isRupees);

                                            if(market_type.equals("BSE")||market_type.equals("Both")){stocksArrayList.add(stock);}
                                            adapter.notifyDataSetChanged();
                                            Log.d("IndianStocks", stock.toString());
                                        }
                                    } catch (Exception e) {
                                        Log.e("error", "error");
                                    }
                                }
                                @Override
                                public void onError(ANError anError) {
                                    Log.e("Login", anError.getErrorDetail());
                                  //  Toast.makeText(, "", Toast.LENGTH_SHORT).show();
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
