package com.example.iexpens.Activity;

import androidx.appcompat.app.AppCompatActivity;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.iexpens.R;

public class Welcome extends AppCompatActivity {

    FlipperLayout flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        flipper = (FlipperLayout)findViewById(R.id.flipper);
        setLayout();
    }


    public void setLayout()
    {
        String url[]= new String[]{
                "https://cdn.vox-cdn.com/thumbor/Ps9Tp2cPY2EJn49MVJ_I3IyaLpg=/0x0:3000x2000/1200x675/filters:focal(1299x1432:1779x1912)/cdn.vox-cdn.com/uploads/chorus_image/image/55060661/acastro_170531_1738_0001_v2.0.jpg",
                "https://blog.spendesk.com/hs-fs/hubfs/img-post/business-money-pink-coins.jpg?width=1166&name=business-money-pink-coins.jpg",
                 "https://thumbs.dreamstime.com/z/income-expenses-d-people-man-person-directional-sign-50244371.jpg"
        };

        for( int i=0;i<3;i++)
        {
            FlipperView view = new FlipperView(getBaseContext());
            view.setImageUrl(url[i]);
            flipper.addFlipperView(view);
            view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                @Override
                public void onFlipperClick(FlipperView flipperView) {
                    Toast.makeText(Welcome.this,""+(flipper.getCurrentPagePosition()),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
