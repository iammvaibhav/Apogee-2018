package example.aditya.com.sms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<User> users;
    int userRank;


    public LeaderBoardAdapter(Context context, ArrayList<User> data, int userRank) {
        this.context = context;
        users = new ArrayList();
        this.users = data;
        this.userRank = userRank;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView name;
        TextView rank;
        TextView networth;
        ImageView img;



        private MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            this.name = itemView.findViewById(R.id.name_tv);
            this.rank = itemView.findViewById(R.id.rank_tv);
            this.networth = itemView .findViewById(R.id.worth_tv);
            this.img = itemView.findViewById(R.id.user_icon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader_board, parent, false);
        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(users.get(position).getName());
        holder.rank.setText(String.valueOf(users.get(position).getRank()));
        holder.networth.setText(String.valueOf(users.get(position).getNetworth()));
        if(position+1==userRank){
            holder.img.setVisibility(View.VISIBLE);
        }
        else{
            holder.img.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
}
