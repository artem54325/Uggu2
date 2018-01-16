package ru.toster.uggu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.toster.uggu.url.ObjectPara;


public class LayoutDay extends LinearLayout {
    private TextView room;      //Комната
    private TextView subject;   //Предмет
    private TextView chief;     //Препод

    public LayoutDay(Context context, ObjectPara object) {
        super(context);
        LayoutInflater.from(getContext()).inflate(
                R.layout.layout_day, this);

        room = (TextView) findViewById(R.id.number_room);
        subject = (TextView) findViewById(R.id.name_subject);
        chief = (TextView) findViewById(R.id.name_chief);

        room.setText(object.getNumberRoom());
        subject.setText(object.getNameSubject());
        chief.setText(object.getNameChief());
    }
}
