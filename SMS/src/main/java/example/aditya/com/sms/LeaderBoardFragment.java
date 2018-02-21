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
import static example.aditya.com.sms.URLs.URL_LB;


public class LeaderBoardFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private static LeaderBoardAdapter adapter;
    public static ArrayList<User> lb_data;

    String username = "test1";
    String email = "test1@gmail.com";
    static int userNum;
      @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lb_data = new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        getLBData();

        mRecyclerView = rootView.findViewById(R.id.recycler_view_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LeaderBoardAdapter(getContext(),lb_data,userNum);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setAdapter(adapter);
        return  rootView;
        //inflater.inflate(R.layout.fragment_leaderboard, container, false);

    }

    private void getLBData() {
          LeaderBoard(email);
    }
    static void LeaderBoard(final String email) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key",KEY);
                    map.put("email",email);

                    AndroidNetworking.post(URL_LB).addBodyParameter(map).build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        Log.e("response", response.toString());
                                        lb_data.clear();
                                        for (int i = 0; i < response.length() - 1; i++) {
                                            JSONObject jsonobject = response.getJSONObject(i);
                                            String name = jsonobject.getString("name");
                                            int worth = jsonobject.getInt("net_worth");

                                            User user = new User(name, worth, i + 1);
                                            lb_data.add(user);
                                            adapter.notifyDataSetChanged();
                                            Log.d("stock", user.toString());
                                        }

                                        JSONObject jsonobject = response.getJSONObject(response.length() - 1);
                                        int rank = jsonobject.getInt("rank");
                                        String name = jsonobject.getString("name");
                                        int worth = jsonobject.getInt("net_worth");
                                        userNum = rank;
                                        if(rank>10) {
                                            lb_data.add(new User(name, worth, rank));
                                        }
                                        adapter.notifyDataSetChanged();
                                     //   progressDialog.hide();
                                    }catch (Exception e){
                                        e.printStackTrace();
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
