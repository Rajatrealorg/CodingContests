package cf.parks.codingcontests;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import cf.parks.codingcontests.dbdb.ContestDAO;
import cf.parks.codingcontests.network.RKSHttp;
import cf.parks.codingcontests.shared.Globals;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    String RESPONSE = RKSHttp.getOkHttp(Globals.MAIN_IP);

                    JSONObject jsonObject = new JSONObject(RESPONSE).getJSONObject("result");
                    JSONArray ongoing = new JSONArray(jsonObject.getString("ongoing"));
                    JSONArray upcoming = new JSONArray(jsonObject.getString("upcoming"));
                    String currentTime = jsonObject.getString("timestamp");

                    ContestDAO contestDAO = new ContestDAO(MainActivity.this);
                    if(ongoing.length()>0 || upcoming.length()>0)
                        contestDAO.clearDB();

                    for(int i=0;i<ongoing.length();++i)
                        contestDAO.insert(ongoing.getJSONObject(i), "ongoing");
                    for(int i=0;i<upcoming.length();++i)
                        contestDAO.insert(upcoming.getJSONObject(i), "upcoming");
                    contestDAO.close();

                    Log.i("Handler","Done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    handler.postDelayed(this, 120000);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        handler.post(runnable);
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int A_S_N = getArguments().getInt(ARG_SECTION_NUMBER); // 0, 1
            Activity activity = getActivity();
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.list_view);
            ContestDAO contestDAO = new ContestDAO(activity.getApplicationContext());
            if(A_S_N == 1)
                listView.setAdapter(contestDAO.getContestOngoingData(activity));
            else if(A_S_N == 2)
                listView.setAdapter(contestDAO.getContestUpcomingData(activity));
            contestDAO.close();
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
