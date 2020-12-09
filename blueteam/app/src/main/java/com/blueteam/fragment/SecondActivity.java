package com.blueteam.fragment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blueteam.R;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;

// open source code for test
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sencond);

        AlphaTabsIndicator alphaTabsIndicator0 = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);

        alphaTabsIndicator0.getTabView(0).showNumber(8);
        alphaTabsIndicator0.getTabView(1).showNumber(88);
        alphaTabsIndicator0.getTabView(2).showNumber(888);
        alphaTabsIndicator0.getTabView(3).showPoint();

        AlphaTabsIndicator alphaTabsIndicator1 = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator1);

        alphaTabsIndicator1.getTabView(0).showNumber(8);
        alphaTabsIndicator1.getTabView(1).showNumber(88);
        alphaTabsIndicator1.getTabView(2).showNumber(888);
        alphaTabsIndicator1.getTabView(3).showPoint();

        alphaTabsIndicator1.setTabCurrenItem(1);

        AlphaTabsIndicator alphaTabsIndicator2 = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator2);

        alphaTabsIndicator2.getTabView(0).showNumber(8);
        alphaTabsIndicator2.getTabView(1).showNumber(88);
        alphaTabsIndicator2.getTabView(2).showNumber(888);
        alphaTabsIndicator2.getTabView(3).showPoint();

        alphaTabsIndicator2.setTabCurrenItem(2);
        alphaTabsIndicator2.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                System.out.println("select !!! : " + tabNum);
                Toast.makeText(SecondActivity.this, "select~" + tabNum, Toast.LENGTH_SHORT).show();
            }
        });

        AlphaTabsIndicator alphaTabsIndicator3 = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator3);
        alphaTabsIndicator3.getTabView(0).showNumber(8);
        alphaTabsIndicator3.getTabView(1).showNumber(88);
        alphaTabsIndicator3.getTabView(2).showNumber(888);
        alphaTabsIndicator3.getTabView(3).showPoint();

        alphaTabsIndicator3.setTabCurrenItem(3);
    }
}
