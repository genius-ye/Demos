package com.genius.views.piechartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.genius.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PieChartActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private List<PieChart.IPieElement> mIPieElements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        mPieChart = findViewById(R.id.piechart);
        mIPieElements = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mIPieElements.add(new PieElement(20,getRandColorCode(),"测试:"+i));
        }
        mPieChart.setData(mIPieElements);
    }

    /**
     * 获取十六进制的颜色代码.例如 "#6E36B4" , For HTML ,
     * @return String
     */
    public static String getRandColorCode(){
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;

        return "#"+r+g+b;
    }


}
