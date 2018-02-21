package example.aditya.com.sms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Post> posts;


    public NewsAdapter(Context context, ArrayList<Post> data) {
        this.context = context;
        posts = new ArrayList();
        this.posts = data;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
      
        TextView title;
        TextView desc;

        private MyViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title_tv);
            this.desc = itemView.findViewById(R.id.body_tv);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(posts.get(position).getTitle());
        holder.desc.setText(String.valueOf(posts.get(position).getDesc()));

    }


    @Override
    public int getItemCount() {
        return posts.size();
    }
}
