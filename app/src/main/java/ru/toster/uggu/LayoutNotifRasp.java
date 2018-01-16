package ru.toster.uggu;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.toster.uggu.url.ObjectPara;

public class LayoutNotifRasp  extends LinearLayout{
    private TextView time;
    private TextView room;      //Комната
    private TextView subject;   //Предмет
    private TextView chief;     //Препод

    public LayoutNotifRasp(Context context, ObjectPara object, String color) {
        super(context);

        LayoutInflater.from(getContext()).inflate(
                R.layout.layout_notifation, this);

        LinearLayout view = (LinearLayout) findViewById(R.id.layout);
//        view.setBackgroundResource();

        time = (TextView) findViewById(R.id.time);
        room = (TextView) findViewById(R.id.number_room);
        subject = (TextView) findViewById(R.id.name_subject);
        chief = (TextView) findViewById(R.id.name_chief);

        time.setText(object.getTime());
        room.setText(object.getNumberRoom());
        subject.setText(object.getNameSubject());
        chief.setText(object.getNameChief());
    }
}
