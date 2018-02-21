package example.aditya.com.sms;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static example.aditya.com.sms.MainActivity.SellButtonClicked;
import static example.aditya.com.sms.MainActivity.conv_rate;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Stocks> mItems;


    public ProfileAdapter(Context context, ArrayList<Stocks> data) {
        this.context = context;
        mItems = new ArrayList();
        this.mItems = data;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView title;
        LinearLayout view;
        Button sell;
        TextView qty_tv;
        TextView buying_price_tv;
        TextView current_price_tv;
        TextView profit_percent_tv;
        TextView profit_tv;
        TextView profit_text;
        ImageView imageView;


        private MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            this.title = itemView.findViewById(R.id.title_tv);
            this.view = itemView.findViewById(R.id.view);
            this.sell = itemView.findViewById(R.id.sell);
            this.qty_tv = itemView.findViewById(R.id.qty_tv);
            this.buying_price_tv = itemView.findViewById(R.id.buying_price_tv);;
            this.current_price_tv = itemView.findViewById(R.id.current_price_tv);;
            this.profit_percent_tv = itemView.findViewById(R.id.profit_percent_tv);;
            this.profit_tv = itemView.findViewById(R.id.profit_tv);
            this.profit_text = itemView.findViewById(R.id.profit_text);
            this.imageView = itemView.findViewById(R.id.img);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_portfolio, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(mItems.get(position).getName());
         holder.qty_tv.setText(String.valueOf(mItems.get(position).getWithMe()));

        double profit;
        double profit_percent;

        if(mItems.get(position).isRupees()){
            double R_price = (double) mItems.get(position).getCurrentPrice();
            holder.current_price_tv.setText("₹ "+String.format("%.2f", R_price));

            double R_buy_price = (double) mItems.get(position).getPurchase_price();
            holder.buying_price_tv.setText("₹ "+String.format("%.2f", R_buy_price));
            profit = R_price - R_buy_price;
            profit_percent = (profit/R_buy_price)*100;
            holder.profit_percent_tv.setText(String.format("%.2f", profit_percent)+"%");
            holder.profit_tv.setText("₹ "+String.format("%.2f", profit));

        }else {
            double D_Price = (double) mItems.get(position).getCurrentPrice() / (double) conv_rate;
            holder.current_price_tv.setText("$ " + String.format("%.2f", D_Price));

            double D_buy_Price = (double) mItems.get(position).getPurchase_price() / (double) conv_rate;
            holder.buying_price_tv.setText("$ " + String.format("%.2f", D_buy_Price));

            profit = D_Price - D_buy_Price;
            profit_percent = (profit/D_buy_Price)*100;
            holder.profit_percent_tv.setText(String.format("%.2f", profit_percent)+"%");
            holder.profit_tv.setText("$ "+String.format("%.2f", profit));
        }

        if(profit>=0){
            holder.profit_text.setText("Profit");
            holder.imageView.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            holder.profit_tv.setTextColor(Color.GREEN);
            holder.profit_text.setTextColor(Color.GREEN
            );
            holder.profit_percent_tv.setTextColor(Color.GREEN);
            holder.imageView.setColorFilter(Color.GREEN);
        }
        else{
            holder.profit_text.setText("Loss");
            holder.imageView.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            holder.profit_tv.setTextColor(Color.RED);
            holder.profit_text.setTextColor(Color.RED);
            holder.profit_percent_tv.setTextColor(Color.RED);
            holder.imageView.setColorFilter(Color.RED);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.view.getVisibility()==View.VISIBLE) {holder.view.setVisibility(View.GONE);}
                else {holder.view.setVisibility(View.VISIBLE);}
            }
        });

        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellButtonClicked(mItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mItems== null) return 0;
       else  return mItems.size();
    }
}
