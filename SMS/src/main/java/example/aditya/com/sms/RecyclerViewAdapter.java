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

import static example.aditya.com.sms.MainActivity.BuyButtonClicked;
import static example.aditya.com.sms.MainActivity.SellButtonClicked;
import static example.aditya.com.sms.MainActivity.conv_rate;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Stocks> mItems;
    String currency;
    int market;
    public RecyclerViewAdapter(Context context, ArrayList<Stocks> data, int market) {
        this.context = context;
        mItems = new ArrayList();
        this.mItems = data;
        this.market = market;
        if(market==0){ currency = "\u20B9";}
       else if(market==1){ currency = "$";}
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView title;
        TextView current_price;
         LinearLayout view;
         Button buy;
        Button sell;
        TextView percent_text;
        ImageView percent_image;

        private MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            this.title = itemView.findViewById(R.id.title_tv);
            this.current_price = itemView.findViewById(R.id.price_tv);
          //  this.price_icon = itemView .findViewById(R.id.icon);
          //  this.percent = itemView.findViewById(R.id.percent_tv);
            this.view = itemView.findViewById(R.id.view);
       //     this.percent_icon = itemView .findViewById(R.id.percent_image);
            this.buy = itemView.findViewById(R.id.buy);
            this.sell = itemView.findViewById(R.id.sell);
            this.percent_text = itemView.findViewById(R.id.percenttext);
            this.percent_image = itemView.findViewById(R.id.percentimage);
         //  this.currency_tv = itemView.findViewById(R.id.currency_tv);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.percent_image.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        holder.title.setText(mItems.get(position).getName());
        if(mItems.get(position).isRupees()){
            double R_price = (double) mItems.get(position).getCurrentPrice();
          //  holder.currency_tv.setText("₹ ");
            holder.current_price.setText("₹ "+String.format("%.2f", R_price));
         }else{
          //  holder.currency_tv.setText("$ ");
            double D_Price = (double)mItems.get(position).getCurrentPrice()/(double)conv_rate;
            holder.current_price.setText("$ "+String.format("%.2f", D_Price));


        }
        double  a =mItems.get(position).getpChange();

        if(a<0){
           holder.percent_image.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            holder.percent_image.setColorFilter(Color.RED);
            holder.percent_text.setTextColor(Color.RED);
            holder.percent_text.setText("-"+String.format("%.2f",a)+"%");

         }
        else if(a>0) {
            holder.percent_image.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            holder.percent_image.setColorFilter(Color.parseColor("#006400"));
            holder.percent_text.setTextColor(Color.parseColor("#006400"));
            holder.percent_text.setText("+"+String.format("%.2f",a)+"%");
        }
        else {
            holder.percent_image.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            holder.percent_text.setTextColor(Color.RED);
            holder.percent_text.setTextColor(Color.GRAY);
            holder.percent_image.setColorFilter(Color.GRAY);
            holder.percent_text.setText("+0.00%");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // closeAll();
               if(holder.view.getVisibility()==View.VISIBLE) holder.view.setVisibility(View.GONE);
               else{holder.view.setVisibility(View.VISIBLE);}
            }
        });

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyButtonClicked(mItems.get(position));
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
