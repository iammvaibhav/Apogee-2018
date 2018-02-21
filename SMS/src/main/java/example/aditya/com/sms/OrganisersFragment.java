package example.aditya.com.sms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static example.aditya.com.sms.MainActivity.Rupees;
import static example.aditya.com.sms.MainActivity.cash_inhand;
import static example.aditya.com.sms.MainActivity.conv_rate;

public class OrganisersFragment extends Fragment {

   static TextView cashInHand;
   static TextView market_tv;
   static TextView conversion;
   static TextView cashInHand_D;
   ImageView news_btn;
   ImageView change_btn;
   ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orgenisers, container, false);
        // Setting ViewPager for each Tabs
        market_tv = view.findViewById(R.id.subLabel);
        conversion = view.findViewById(R.id.conversion);
        cashInHand_D = view.findViewById(R.id.decimal);

        news_btn = view.findViewById(R.id.news_btn);
        change_btn = view.findViewById(R.id.change_btn);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0){
                    market_tv.setText("BSE");
                    cashInHand.setText(Rupees + String.valueOf(cash_inhand));
                    cashInHand_D.setText("00");
                }
                else{
                    market_tv.setText("NYM");
                    if(conv_rate!=0){
                        int D_Cash = cash_inhand/conv_rate;
                        int D_Cash_Decimal = ((cash_inhand*100)/conv_rate)%100;

                        cashInHand.setText("$ " + String.valueOf(D_Cash));
                        cashInHand_D.setText(String.valueOf(D_Cash_Decimal));
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cashInHand = view.findViewById(R.id.money);


        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              changeButtonClicked();
            }
        });

        news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsButtonClicked();
            }
        });

        return view;
    }

    private void changeButtonClicked() {
        int pos = viewPager.getCurrentItem();
        if(pos==0) viewPager.setCurrentItem(1);
        else{   viewPager.setCurrentItem(0);}
    }

    private void newsButtonClicked(){
        Intent intent = new Intent(getContext(), NewsActivity.class);
        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new IndianStockFragment(), "");
        adapter.addFragment(new InternationalStockFragment(),"");
        viewPager.setAdapter(adapter);


    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}