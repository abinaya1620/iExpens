package com.example.iexpens.activity;

import androidx.appcompat.app.AppCompatActivity;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iexpens.R;
import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends AppCompatActivity {

    FlipperLayout flipper;
    private Button btLogin;
    private Button btReg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        flipper = findViewById(R.id.flipper);
        btLogin = findViewById(R.id.bt_login);
        btReg = findViewById(R.id.bt_reg);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

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
