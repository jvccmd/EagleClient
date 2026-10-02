package com.eagle.client;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageButton;
import android.graphics.drawable.GradientDrawable;

public class EagleOverlayService extends Service {
    private WindowManager wm;
    private View bubble;
    private WindowManager.LayoutParams params;

    @Override public void onCreate() {
        super.onCreate();
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return; }

        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        ImageButton b = new ImageButton(this);
        b.setImageResource(com.eagle.client.R.drawable.eagle_logo);
        b.setBackground(makeCircle());
        b.setPadding(5,5,5,5);

        params = new WindowManager.LayoutParams(
                68, 68, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 24; params.y = 120;

        final int[] down = new int[2];
        final int[] start = new int[2];
        final boolean[] moved = new boolean[1];

        b.setOnTouchListener((v,e) -> {
            if(e.getAction()==MotionEvent.ACTION_DOWN){
                down[0]=(int)e.getRawX(); down[1]=(int)e.getRawY();
                start[0]=params.x; start[1]=params.y; moved[0]=false; return true;
            }
            if(e.getAction()==MotionEvent.ACTION_MOVE){
                int dx=(int)e.getRawX()-down[0], dy=(int)e.getRawY()-down[1];
                if(Math.abs(dx)+Math.abs(dy)>8) moved[0]=true;
                params.x=start[0]+dx; params.y=start[1]+dy; wm.updateViewLayout(b,params); return true;
            }
            if(e.getAction()==MotionEvent.ACTION_UP){
                if(!moved[0]) showMenu(); return true;
            }
            return true;
        });
        bubble=b; wm.addView(bubble,params);
    }

    private GradientDrawable makeCircle(){
        GradientDrawable g=new GradientDrawable();
        g.setShape(GradientDrawable.OVAL); g.setColor(0xff171717);
        g.setStroke(2,0xffb71919); return g;
    }

    private void showMenu(){
        WebView w=new WebView(this);
        WebSettings s=w.getSettings(); s.setJavaScriptEnabled(true);
        w.loadDataWithBaseURL(null, html(), "text/html", "UTF-8", null);
        WindowManager.LayoutParams p=new WindowManager.LayoutParams(
            760, 900, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT);
        p.gravity=Gravity.CENTER;
        w.setOnLongClickListener(v -> true);
        w.setOnTouchListener((v,e)->false);
        wm.addView(w,p);
        w.setOnClickListener(v -> {});
        w.setTag(p);
    }

    private String html(){
        return "<html><meta name='viewport' content='width=device-width'><style>"+
        "body{margin:0;background:#111;color:#eee;font:16px sans-serif}"+
        ".top{padding:18px;font-weight:bold;border-bottom:1px solid #333;color:#fff}"+
        ".tabs{width:150px;float:left;background:#151515;height:850px;padding:8px}"+
        ".tab{padding:12px;margin:4px;border-radius:8px;color:#aaa;background:#191919}"+
        ".tab:first-child{background:#7e1010;color:white}.main{margin-left:175px;padding:20px}"+
        ".card{background:#1a1a1a;border:1px solid #303030;border-radius:10px;padding:15px;margin:10px 0}"+
        "</style><div class='top'>🦅 EAGLE CLIENT</div><div class='tabs'>"+
        "<div class='tab'>Combat</div><div class='tab'>Movement</div><div class='tab'>Player</div>"+
        "<div class='tab'>Visuals</div><div class='tab'>HUD</div><div class='tab'>World</div>"+
        "<div class='tab'>Misc</div><div class='tab'>Settings</div></div>"+
        "<div class='main'><h2>Combat</h2><div class='card'>Aim Assist &nbsp; OFF</div>"+
        "<div class='card'>Reach &nbsp; 3.0</div><div class='card'>Auto Clicker &nbsp; OFF</div></div></html>";
    }

    @Override public void onDestroy(){
        if(wm!=null && bubble!=null) wm.removeView(bubble);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent i){return null;}
}
