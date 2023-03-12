package com.example.calenderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    FrameLayout FrameLayout;
    LinearLayout linear;
    TableLayout month_layout,week_layout;
    ScrollView scrollview;
    Button today;
    TextView text_m, year;
    EditText dialog_title, dialog_start, dialog_end, dialog_place, dialog_memo;
    EditText week_title, week_end, week_place, week_memo;
    int tabler = 6, tablec = 7;
    int gridr = 25, gridc = 7;
    String str, str1, str2, str3, str4, str5;
    String every;
    int day_1_1=5, last, time1, time2, week=0, timer;
    String[] monTosun= {"일","월","화","수","목","금","토"};
    final int[] month = {31,28,31,30,31,30,31,31,30,31,30,31};
    Button monthbtn[][] = new Button[tabler][tablec];
    Button weekbtn[][] = new Button[gridr][gridc];
    TextView text[]=new TextView[7];

    Calendar cal = Calendar.getInstance();
    int tMonth = cal.get(Calendar.MONTH);
    int tDay = cal.get(Calendar.DAY_OF_MONTH);
    int m=tMonth, click=tDay;
    String filename="2021_" +Integer.toString(m+1) +"_"+ Integer.toString(click)+".txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linear=findViewById(R.id.linear);
        FrameLayout = (FrameLayout) findViewById(R.id.FrameLayout);
        scrollview=(ScrollView)findViewById(R.id.Scroll);
        today = (Button) findViewById(R.id.btn_today);
        text_m = (TextView) findViewById(R.id.text_month);
        year=(TextView)findViewById(R.id.year);
        dialog_title = findViewById(R.id.ed1);
        dialog_start = findViewById(R.id.ed2);
        dialog_end = findViewById(R.id.ed3);
        dialog_place = findViewById(R.id.ed4);
        dialog_memo = findViewById(R.id.ed5);
        week_title= findViewById(R.id.w1);
        week_end=findViewById(R.id.w2);
        week_place = findViewById(R.id.w3);
        week_memo=findViewById(R.id.w4);



        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        for(int i=0;i<7;i++) {
            text[i]=new TextView(this);
            text[i].setText(monTosun[i]);
            if(i==0)text[i].setTextColor(Color.RED);
            if(i==6)text[i].setTextColor(Color.BLUE);
            text[i].setLayoutParams(params);
            text[i].setGravity(Gravity.CENTER);
            linear.addView(text[i]);
        }


        registerForContextMenu(text_m);

        month_layout = new TableLayout(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
        TableRow row[] = new TableRow[tabler];

        for (int tr = 0; tr < tabler; tr++) {
            row[tr] = new TableRow(this);
            for (int tc = 0; tc < tablec; tc++) {
                monthbtn[tr][tc] = new Button(this);
                monthbtn[tr][tc].setLayoutParams(rowLayout);
                registerForContextMenu(monthbtn[tr][tc]);
                monthbtn[tr][tc].setBackgroundColor(Color.GRAY);
                row[tr].addView(monthbtn[tr][tc]);
            }
            month_layout.addView(row[tr]);
        } FrameLayout.addView(month_layout);



        week_layout = new TableLayout(this);
        TableRow.LayoutParams rowlayout = new TableRow.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
        TableRow tablerow[] = new TableRow[gridr];
        int j=0;
        for (int gr = 0; gr < gridr; gr++) {
            tablerow[gr] = new TableRow(this);
            for (int gc = 0; gc < gridc; gc++) {
                weekbtn[gr][gc] = new Button(this);
                weekbtn[gr][gc].setLayoutParams(rowlayout);
                registerForContextMenu(weekbtn[gr][gc]);
                weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
                weekbtn[gr][gc].setText(String.valueOf(j));
                weekbtn[gr][gc].setGravity(Gravity.CENTER);
                tablerow[gr].addView(weekbtn[gr][gc]);
            }
            week_layout.addView(tablerow[gr]);
            j++;
        }scrollview.addView(week_layout);



        for (int tr = 0; tr < tabler; tr++) {
            for (int tc = 0; tc < tablec; tc++) {
                int pushr=tr, pushc=tc;
                monthbtn[tr][tc].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click= clickdate(m,pushr,pushc);
                        filename="2021_" +Integer.toString(m+1) +"_"+ Integer.toString(click)+".txt";
                        for (int gr = 0; gr < gridr; gr++) {
                            for (int gc = 0; gc < gridc; gc++) weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
                        }
                        dialog_write(m,pushr,pushc);
                        week=pushr;
                        show_day(m,week);
                        year.setText(String.valueOf(week+1)+"주 2021");
                        r_start(m,pushr);
                    }
                });
            }
        }


        for (int gr = 0; gr < gridr; gr++) {
            for (int gc = 0; gc < gridc; gc++) {
                int pushr=gr, pushc=gc;
                weekbtn[gr][gc].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click= clickdate(m,week,pushc);
                        filename="2021_" +Integer.toString(m+1) +"_"+ Integer.toString(click)+".txt";
                        View dialogView2 = (View) View.inflate(MainActivity.this, R.layout.dialog2, null);
                        AlertDialog.Builder dlg2 = new AlertDialog.Builder(MainActivity.this);
                        dlg2.setTitle(filename+"\n시작 시간:"+String.valueOf(pushr));
                        dlg2.setView(dialogView2);
                        dlg2.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                week_title=dialogView2.findViewById(R.id.w1);
                                week_end=dialogView2.findViewById(R.id.w2);
                                week_place=dialogView2.findViewById(R.id.w3);
                                week_memo=dialogView2.findViewById(R.id.w4);
                                str1 = week_title.getText().toString();
                                str2 = String.valueOf(pushr);
                                str3 = week_end.getText().toString();
                                str4 = week_place.getText().toString();
                                str5 = week_memo.getText().toString();
                                every = "약속시간:" + str1 + "\n시작시간:" + str2 + "\n종료시간:" + str3 + "\n장소:" + str4 + "\n메모:" + str5;
                                for (int gr = 0; gr < gridr; gr++) {
                                    for (int gc = 0; gc < gridc; gc++) weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
                                }
                                try{
                                    FileOutputStream outFs = openFileOutput(filename,MODE_PRIVATE);
                                    outFs.write(every.getBytes());
                                    outFs.close();
                                }catch(IOException e){}
                                plan(m);
                                r_start(m,week);
                            }
                        });
                        dlg2.setNegativeButton("취소", null);
                        dlg2.show();
                    }
                });
            }
        }

        month_layout.setVisibility(View.VISIBLE);
        scrollview.setVisibility(View.INVISIBLE);
        text_m.setText(String.valueOf(tMonth+1)+"월");
        count_date(tMonth);
        plan(tMonth);
        show_day(tMonth,week);
        r_start(tMonth,week);
        year.setText(String.valueOf(week+1)+"주 2021");

        today.setOnClickListener(new View.OnClickListener() {                                       //오늘 날짜 표시
            @Override
            public void onClick(View view) {
                m=tMonth;
                text_m.setText(String.valueOf(tMonth+1)+"월");
                count_date(tMonth);
                int day=day_1_1,r,c;
                int date;
                for(int i=0;i<m;i++) {
                    day += month[i];
                } if(m!=0) day=day%7;
                date=tDay+day;
                if(date%7==0){
                    r=date/7 -1;
                    c=6;
                }else {
                    r = (date / 7);
                    c = date % 7 - 1;
                }
                click=clickdate(m,r,c);
                week=r;
                for (int gr = 0; gr < gridr; gr++) {
                    for (int gc = 0; gc < gridc; gc++) weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
                }
                show_day(m,r);
                year.setText(String.valueOf(r+1)+"주 2021");
                r_start(m,r);
                plan(m);
                monthbtn[r][c].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.today));
            }
        });

    }

    public void show_day(int m, int r){
        int day = day_1_1, start=1, pre=0;
        if(m!=0) pre=month[m-1];
        if(m==0) pre=month[11];

        for (int i = 0; i < m; i++) {
            day += month[i];
        }
        if (m != 0) day = day % 7;
        if(r==0) {
            for (int i = day - 1; i >= 0; i--) {
                text[i].setText(monTosun[i] + "(" + String.valueOf(pre) + "일)");
                pre--;
                for (int gr = 0; gr < gridr; gr++) {
                    weekbtn[gr][i].setEnabled(false);
                }
            }

            for(int i=day;i<7;i++) {
                text[i].setText(monTosun[i] + "(" + String.valueOf(start) + "일)");
                start++;
                for(int gr=0; gr<gridr; gr++){
                    weekbtn[gr][i].setEnabled(true);
                }
            }
        }
        else{
            start=clickdate(m,r,0);
            for(int i=0; i<7;i++){
                if(start<=month[m]) {
                    text[i].setText(monTosun[i] + "(" + String.valueOf(start) + "일)");
                    start++;
                    for(int gr=0; gr<gridr; gr++){
                        weekbtn[gr][i].setEnabled(true);
                    }
                }
                else{
                    int date=1;
                    text[i].setText(monTosun[i] + "(" + String.valueOf(date) + "일)");
                    date++;
                    for(int gr=0; gr<gridr; gr++){
                        weekbtn[gr][i].setEnabled(false);
                    }
                }
            }
        }
    }


    public void dialog_week(int start,int start_c){                                                 //주간 일정 표시
        File f = new File(getFilesDir(), "2021_" + Integer.toString(m+1) + "_" + Integer.toString(start) + ".txt");
        if (f.exists()==true) {
            String name="2021_" + Integer.toString(m+1) + "_" + Integer.toString(start) + ".txt";
            try {
                FileInputStream inFS = openFileInput(name);
                byte[] txt = new byte[100];
                inFS.read(txt);
                str = new String(txt);
                inFS.close();
            } catch (IOException e) {}
            String[] array = str.split("\n");
            String t1=array[1].replaceAll("[^0-9]", "");
            String t2=array[2].replaceAll("[^0-9]", "");
            time1 = Integer.valueOf(t1);
            time2 = Integer.valueOf(t2);
            for (int j = time1; j <= time2; j++)
                weekbtn[j][start_c].setBackgroundColor(Color.YELLOW);
        }

    }


    public void r_start(int m, int r){                                                              //그 주의 시작값 받기
        int day = day_1_1, start;
        for (int i = 0; i < m; i++) {
            day += month[i];
        }
        if (m != 0) day = day % 7;
        int a=7-day;
        if(r==0){
            for(start=1;start<=a;start++){
                dialog_week(start,day);
                day++;
            }
        }
        else{
            int c=0;
            int i= a+((r-1)*7)+1;
            for(start=i;start<i+7;start++){
                dialog_week(start,c);
                c++;
            }
        }
    }
    public void dialog_write(int m,int r, int c){                                                   //다이얼로그 창
        View dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog1, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setTitle(filename);
        dlg.setView(dialogView);
        dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialog_title = dialogView.findViewById(R.id.ed1);
                dialog_start = dialogView.findViewById(R.id.ed2);
                dialog_end = dialogView.findViewById(R.id.ed3);
                dialog_place = dialogView.findViewById(R.id.ed4);
                dialog_memo = dialogView.findViewById(R.id.ed5);
                str1 = dialog_title.getText().toString();
                str2 = dialog_start.getText().toString();
                str3 = dialog_end.getText().toString();
                str4 = dialog_place.getText().toString();
                str5 = dialog_memo.getText().toString();
                every ="약속시간:" + str1 + "\n시작시간:" + str2 + "\n종료시간:" + str3 + "\n장소:" + str4+ "\n메모:"+ str5;
                try{
                    FileOutputStream outFs = openFileOutput(filename,MODE_PRIVATE);
                    outFs.write(every.getBytes());
                    outFs.close();
                }catch(IOException e){}
                plan(m);
                for (int gr = 0; gr < gridr; gr++) {
                    for (int gc = 0; gc < gridc; gc++) weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
                }
                r_start(m,r);
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }
    public void plan(int m){                                                                        //일정 표시
        for (int k = 1; k < 32; k++) {
            File f = new File(getFilesDir(), "2021_" + Integer.toString(m+1) + "_" + Integer.toString(k) + ".txt");
            if (f.exists()==true) {
                int day = day_1_1, date=0, r,c;
                for (int i = 0; i < m; i++) {
                    day += month[i];
                }
                if (m != 0) day = day % 7;
                date =k +day;
                if(date%7==0){
                    r=date/7 -1;
                    c=6;
                }else {
                    r = (date / 7);
                    c = date % 7 - 1;
                }
                monthbtn[r][c].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.plan));
            }
        }
    }

    public int clickdate(int m,int r, int c){                                                       //날짜 반환
        int day= day_1_1;
        int d=1;

        for(int i=0;i<m;i++) day += month[i];
        if(m!=0) day=day%7;
        if(r==0){
            for (int i = day; i < c; i++) d++;
        }
        else {
            d = d + (7 - day);
            for(int i=1; i<(c+7*r-6);i++)d++;
        }
        return d;
    }

    public void count_date(int m){                                                                  //월간 달력 만들기
        int date = 1;
        int day= day_1_1;
        int first=1;
        if(m!=0)last=month[m-1];
        if(m==0)last=month[11];

        for (int tr = 0; tr < tabler; tr++) {
            for (int tc = 0; tc < tablec; tc++) {
                monthbtn[tr][tc].setEnabled(true);
                monthbtn[tr][tc].setBackgroundColor(Color.GRAY);
            }
        }

        for(int i=0;i<m;i++) {
            day += month[i];
        }
        if(m!=0) day=day%7;

        for(int i=(day-1);i>=0;i--){
            monthbtn[0][i].setEnabled(false);
            monthbtn[0][i].setText(String.valueOf(last));
            last--;
            if(i==0)monthbtn[0][i].setTextColor(Color.RED);
            else if(i==6)monthbtn[0][i].setTextColor(Color.BLUE);
            else monthbtn[0][i].setTextColor(Color.BLACK);
        }
        for (int i = day; i < tablec; i++) {
            monthbtn[0][i].setText(String.valueOf(date));
            if(i==0)monthbtn[0][i].setTextColor(Color.RED);
            else if(i==6)monthbtn[0][i].setTextColor(Color.BLUE);
            else monthbtn[0][i].setTextColor(Color.BLACK);
            date++;
        }
        for (int i = 1; i <tabler;i++) {
            for (int k = 0; k < tablec; k++) {
                if (date <= month[m]) {
                    monthbtn[i][k].setText(String.valueOf(date));
                    if(k==0)monthbtn[i][k].setTextColor(Color.RED);
                    else if(k==6)monthbtn[i][k].setTextColor(Color.BLUE);
                    else monthbtn[i][k].setTextColor(Color.BLACK);
                    date++;
                }
                else {
                    monthbtn[i][k].setText(String.valueOf(first));
                    first++;
                    if(k==0)monthbtn[i][k].setTextColor(Color.RED);
                    else if(k==6)monthbtn[i][k].setTextColor(Color.BLUE);
                    else monthbtn[i][k].setTextColor(Color.BLACK);
                    monthbtn[i][k].setEnabled(false);
                }
            }
        }
    }
    public void reset_grid(){                                                                       //주간 일정 초기화
        for (int gr = 0; gr < gridr; gr++) {
            for (int gc = 0; gc < gridc; gc++) weekbtn[gr][gc].setBackgroundColor(Color.GRAY);
        }
        week=0;
        year.setText(String.valueOf(week+1)+"주 2021");
        r_start(m,week);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "주간");
        menu.add(0, 2, 0, "월간");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                show_day(m,week);
                month_layout.setVisibility(View.INVISIBLE);
                scrollview.setVisibility(View.VISIBLE);
                return true;
            case 2:
                show_day(m,0);
                month_layout.setVisibility(View.VISIBLE);
                scrollview.setVisibility(View.INVISIBLE);
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mInflater = getMenuInflater();
        if(v==text_m){
            mInflater.inflate(R.menu.menu1,menu);
        }
        for (int tr = 0; tr < tabler; tr++) {
            for (int tc = 0; tc < tablec; tc++) {
                if (v == monthbtn[tr][tc]) {
                    int longr=tr, longc=tc;
                    click=clickdate(m,longr,longc);
                    filename="2021_" +Integer.toString(m+1) +"_"+ Integer.toString(click)+".txt";
                    menu.add(0, 1, 0, "일정 보기");
                    menu.add(0, 2, 0, "일정 삭제");
                }
            }
        }
        for (int gr = 0; gr < gridr; gr++) {
            for (int gc = 0; gc < gridc; gc++) {
                if (v == weekbtn[gr][gc]) {
                    int longc=gc;
                    click=clickdate(m,week,longc);
                    filename="2021_" +Integer.toString(m+1) +"_"+ Integer.toString(click)+".txt";
                    timer=gr;
                    menu.add(0, 3, 0, "일정 보기");
                    menu.add(0, 4, 0, "일정 삭제");
                }
            }
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.m1:
                text_m.setText("1월");
                m = 0;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m2:
                text_m.setText("2월");
                m = 1;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m3:
                text_m.setText("3월");
                m = 2;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m4:
                text_m.setText("4월");
                m = 3;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m5:
                text_m.setText("5월");
                m = 4;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m6:
                text_m.setText("6월");
                m = 5;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m7:
                text_m.setText("7월");
                m = 6;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m8:
                text_m.setText("8월");
                m = 7;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m9:
                text_m.setText("9월");
                m = 8;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m10:
                text_m.setText("10월");
                m = 9;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m11:
                text_m.setText("11월");
                m = 10;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case R.id.m12:
                text_m.setText("12월");
                m = 11;
                count_date(m);
                plan(m);
                reset_grid();
                show_day(m,0);
                return true;
            case 1:
                try {
                    FileInputStream inFS = openFileInput(filename);
                    byte[] txt = new byte[100];
                    inFS.read(txt);
                    str = new String(txt);
                    Intent intent= new Intent(getApplicationContext(),SecondActivity.class);
                    intent.putExtra("plan",str);
                    startActivity(intent);
                    inFS.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"일정이 없습니다.",Toast.LENGTH_SHORT).show();
                }
                return true;
            case 2:
                int day1=day_1_1, r1,c1;
                for(int i=0;i<m;i++) {
                    day1 += month[i];
                } if(m!=0) day1=day1%7;
                int date1=click+day1;
                if(date1%7==0){
                    r1=date1/7 -1;
                    c1=6;
                } else {
                    r1 = (date1 / 7);
                    c1 = date1 % 7 - 1;
                }
                monthbtn[r1][c1].setBackground(null);
                monthbtn[r1][c1].setBackgroundColor(Color.GRAY);

                File fclose= new File(getFilesDir(),filename);
                if(fclose.exists()) {
                    try {
                        FileInputStream inFS = openFileInput(filename);
                        byte[] txt = new byte[100];
                        inFS.read(txt);
                        str = new String(txt);
                        inFS.close();
                    } catch (IOException e) {}
                    String[] array = str.split("\n");
                    String t1=array[1].replaceAll("[^0-9]", "");
                    String t2=array[2].replaceAll("[^0-9]", "");
                    int start = Integer.valueOf(t1), end= Integer.valueOf(t2);
                    for (int j = start; j <= end; j++)
                        weekbtn[j][c1].setBackgroundColor(Color.GRAY);
                    fclose.delete();
                    Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
                return true;
            case 3 :
                try {
                    FileInputStream inFS = openFileInput(filename);
                    byte[] txt = new byte[100];
                    inFS.read(txt);
                    str = new String(txt);
                    inFS.close();
                    String[] array = str.split("\n");
                    String t1 = array[1].replaceAll("[^0-9]", "");
                    String t2 = array[2].replaceAll("[^0-9]", "");
                    int start = Integer.valueOf(t1), end= Integer.valueOf(t2);
                    if (timer >= start && timer <= end) {
                        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                        intent.putExtra("plan", str);
                        startActivity(intent);
                    } else Toast.makeText(getApplicationContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case 4 :
                int day2=day_1_1, r2,c2;
                for(int i=0;i<m;i++) {
                    day2 += month[i];
                } if(m!=0) day2=day2%7;
                int date=click+day2;
                if(date%7==0){
                    r2=date/7 -1;
                    c2=6;
                } else {
                    r2 = (date / 7);
                    c2 = date % 7 - 1;
                }
                monthbtn[r2][c2].setBackground(null);
                monthbtn[r2][c2].setBackgroundColor(Color.GRAY);
                File gridclose= new File(getFilesDir(),filename);
                if(gridclose.exists()) {
                    try {
                        FileInputStream inFS = openFileInput(filename);
                        byte[] txt = new byte[100];
                        inFS.read(txt);
                        str = new String(txt);
                        inFS.close();
                    } catch (IOException e) {}
                    String[] array = str.split("\n");
                    String t1=array[1].replaceAll("[^0-9]", "");
                    String t2=array[2].replaceAll("[^0-9]", "");
                    int start = Integer.valueOf(t1), end= Integer.valueOf(t2);
                    if (timer >= start && timer <= end) {
                        for (int j = start; j <= end; j++)
                            weekbtn[j][c2].setBackgroundColor(Color.GRAY);
                        gridclose.delete();
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "일정이 없습니다.", Toast.LENGTH_SHORT).show();
                return true;

        } return false;
    }
}