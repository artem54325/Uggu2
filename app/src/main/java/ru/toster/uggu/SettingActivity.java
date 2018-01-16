package ru.toster.uggu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import ru.toster.uggu.notification.Notifications;

public class SettingActivity extends AppCompatActivity {
    private String[] group2 = new String[]{"Выберите группу","МД-15-1","МД-15-2"};
    private String[] notPlans = new String[]{"Без звука и вибраций","С вибрацией без звука","Со звуком и вибрацией"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SQlite sQlite = new SQlite(this);
        final Notifications alarm = new Notifications();
        final Context context = this.getApplicationContext();
        setContentView(R.layout.activity_setting);

        Switch start = (Switch) findViewById(R.id.notificationStart);
        Spinner group = (Spinner) findViewById(R.id.group);
        Spinner notificationPlans = (Spinner) findViewById(R.id.notificationPlans);

        start.setChecked(sQlite.isNotif());

        start.setChecked(alarm.isAlarm(context));
        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked) {
                    alarm.setAlarm(context);
                } else {
                    alarm.cancel(context);
                }
                sQlite.setDB(String.valueOf(isChecked), 101);
            }
        });
        ArrayAdapter<String> adapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, group2);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapterGroup);
        group.setPrompt("Выберите группу");
        group.setSelection(0);
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position!=0) sQlite.setDB(group2[position], 102);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        ArrayAdapter<String> adapterPlans = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, notPlans);
        adapterPlans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationPlans.setAdapter(adapterPlans);
        notificationPlans.setPrompt("Выберите тип уведомлений");
        notificationPlans.setSelection(0);
        notificationPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                sQlite.setDB(String.valueOf(position), 103);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }
}
