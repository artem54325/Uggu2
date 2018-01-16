package ru.toster.uggu;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.toster.uggu.notification.Notifications;
import ru.toster.uggu.url.ObjectPara;
import ru.toster.uggu.url.ReadUrl;

public class MainActivity extends AppCompatActivity {
    private TableLayout layout;

    private String[] way = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};//,"Воскресенье"
    @Override
    protected void onStart() {
        super.onStart();
        try {
            new ReadUrl(this).readSite();
            addTable();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Проверьте интернет или проблема с бд! " + e.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        final Context context = this.getApplicationContext();
        SQlite sQlite = new SQlite(this);

        Notifications alarm = new Notifications();

        //101 - работает ли нотификатион
        //102 - выбор группы
        //103 - Тип уведомлений 0-Без звука и вибраций 1-С вибрацией без звука 2-Со звуком и вибрацией

        if (!sQlite.isFirstStart()){
            Map<Integer, String> map = new HashMap<>();
            map.put(101,String.valueOf(true));
            map.put(102,"МД-15-1");
            map.put(103,"0");
            map.put(111, String.valueOf(true));
            sQlite.save(map);
        }

        if (!alarm.isAlarm(context)&&sQlite.isNotif())
            alarm.setAlarm(context);

        addTable();

        if (!sQlite.isRasp()){
            new ReadUrl(this).readSite();
        }


        final Intent intent = new Intent(this, SettingActivity.class);

        ((ImageButton) findViewById(R.id.setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Настройки", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        sQlite.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                try {
                    new ReadUrl(this).readSite();
                    addTable();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Проверьте интернет или проблема с бд! " + e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void addTable(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one

        for (int i=0;i<way.length;i++){
            Fragments fragments = new Fragments();
            Bundle bundle = new Bundle();
            bundle.putInt("way", i);
            fragments.setArguments(bundle);
            adapter.addFragment(fragments, way[i]);
        }

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void saveRasp(String body) {
        SQlite sQlite = new SQlite(this);
        sQlite.saveRasp(body);
        sQlite.close();
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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