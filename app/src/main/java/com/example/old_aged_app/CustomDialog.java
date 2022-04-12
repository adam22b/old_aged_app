package com.example.old_aged_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button phone, viber;
    String meno,cislo;
    int pos;

    public CustomDialog(Activity a,String cislo) {
        super(a);
        this.c = a;
        this.cislo = cislo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        phone = (Button) findViewById(R.id.btn_phone);
        viber = (Button) findViewById(R.id.btn_viber);
        phone.setOnClickListener(this);
        viber.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone:
                callPhone();
                break;
            case R.id.btn_viber:
                callViber();
                break;
            default:
                break;
        }
        dismiss();
    }
    public void callViber(){
        try {
            Intent i = c.getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.viber.voip");
            Uri uri = (Uri.parse("tel:" + cislo));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
            intent.setData(uri);
            c.startActivity(intent);
        } catch (Exception e) {
            try {
                c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=com.viber.voip")));
            } catch (android.content.ActivityNotFoundException anfe) {
                c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.viber.voip")));
            }
        }

    }

    public void callPhone(){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + cislo));
        c.startActivity(callIntent);
    }
}
