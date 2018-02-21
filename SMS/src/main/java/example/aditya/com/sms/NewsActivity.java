package example.aditya.com.sms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    NewsAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news);
        if (posts == null) posts = new ArrayList<>();
        adapter = new NewsAdapter(this, posts);
        recyclerView = findViewById(R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        if(getSupportActionBar()!=null)  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("SMS").child("News");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot single_post : dataSnapshot.getChildren()) {
                    String title = (String) single_post.child("title").getValue();
                    String body = (String) single_post.child("body").getValue();
                    posts.add(new Post(title, body));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}