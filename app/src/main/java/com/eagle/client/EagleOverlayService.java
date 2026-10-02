package com.eagle.client;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

public class EagleOverlayService extends Service {

    private WindowManager wm;
    private ImageButton bubble;
    private WindowManager.LayoutParams bubbleParams;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        bubble = new ImageButton(this);
        bubble.setImageResource(R.drawable.eagle_logo);
        bubble.setBackground(circleBackground());
        bubble.setPadding(7, 7, 7, 7);

        bubbleParams = new WindowManager.LayoutParams(
                64,
                64,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        bubbleParams.gravity = Gravity.TOP | Gravity.START;
        bubbleParams.x = 20;
        bubbleParams.y = 140;

        bubble.setOnTouchListener(new View.OnTouchListener() {

            int downX;
            int downY;
            int startX;
            int startY;
            boolean moved;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();

                        startX = bubbleParams.x;
                        startY = bubbleParams.y;

                        moved = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:

                        int dx = (int) event.getRawX() - downX;
                        int dy = (int) event.getRawY() - downY;

                        if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                            moved = true;
                        }

                        bubbleParams.x = startX + dx;
                        bubbleParams.y = startY + dy;

                        wm.updateViewLayout(bubble, bubbleParams);
                        return true;

                    case MotionEvent.ACTION_UP:

                        if (!moved) {
                            showClient();
                        }

                        return true;
                }

                return true;
            }
        });

        wm.addView(bubble, bubbleParams);
    }

    private GradientDrawable circleBackground() {

        GradientDrawable bg = new GradientDrawable();

        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(Color.rgb(18, 18, 18));
        bg.setStroke(2, Color.rgb(190, 25, 25));

        return bg;
    }

    private void showClient() {

        final WebView web = new WebView(this);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        web.setBackgroundColor(Color.TRANSPARENT);

        web.loadDataWithBaseURL(
                null,
                getClientHtml(),
                "text/html",
                "UTF-8",
                null
        );

        WindowManager.LayoutParams menuParams =
                new WindowManager.LayoutParams(
                        dp(360),
                        dp(520),
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );

        menuParams.gravity = Gravity.CENTER;

        wm.addView(web, menuParams);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private String getClientHtml() {

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +

                "<meta name='viewport' content='width=device-width,initial-scale=1'>" +

                "<style>" +

                "*{box-sizing:border-box}" +

                "body{" +
                "margin:0;" +
                "background:#0b0b0d;" +
                "color:#eeeeee;" +
                "font-family:Arial,sans-serif;" +
                "overflow:hidden;" +
                "}" +

                ".window{" +
                "width:100%;" +
                "height:100vh;" +
                "display:flex;" +
                "background:#0d0d10;" +
                "border:1px solid #2b2b31;" +
                "border-radius:16px;" +
                "overflow:hidden;" +
                "}" +

                ".sidebar{" +
                "width:105px;" +
                "background:#101014;" +
                "border-right:1px solid #24242a;" +
                "padding:12px 7px;" +
                "}" +

                ".logo{" +
                "height:55px;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "font-size:24px;" +
                "font-weight:bold;" +
                "color:#e21b23;" +
                "}" +

                ".brand{" +
                "font-size:9px;" +
                "text-align:center;" +
                "color:#77777f;" +
                "margin-bottom:14px;" +
                "letter-spacing:1px;" +
                "}" +

                ".nav{" +
                "padding:9px 5px;" +
                "margin:4px 0;" +
                "border-radius:8px;" +
                "font-size:11px;" +
                "color:#8c8c94;" +
                "text-align:center;" +
                "}" +

                ".nav.active{" +
                "background:#351315;" +
                "color:#ff4048;" +
                "border:1px solid #5d2023;" +
                "}" +

                ".content{" +
                "flex:1;" +
                "padding:18px;" +
                "overflow:hidden;" +
                "}" +

                ".top{" +
                "display:flex;" +
                "justify-content:space-between;" +
                "align-items:center;" +
                "margin-bottom:18px;" +
                "}" +

                ".title{" +
                "font-size:21px;" +
                "font-weight:bold;" +
                "}" +

                ".subtitle{" +
                "font-size:10px;" +
                "color:#77777f;" +
                "margin-top:3px;" +
                "}" +

                ".close{" +
                "width:27px;" +
                "height:27px;" +
                "border-radius:8px;" +
                "background:#1b1b20;" +
                "border:1px solid #303038;" +
                "color:#aaa;" +
                "text-align:center;" +
                "line-height:27px;" +
                "}" +

                ".card{" +
                "background:#151519;" +
                "border:1px solid #25252c;" +
                "border-radius:11px;" +
                "padding:13px;" +
                "margin-bottom:9px;" +
                "}" +

                ".row{" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:space-between;" +
                "}" +

                ".feature{" +
                "font-size:13px;" +
                "font-weight:bold;" +
                "}" +

                ".description{" +
                "font-size:9px;" +
                "color:#77777f;" +
                "margin-top:4px;" +
                "}" +

                ".switch{" +
                "width:38px;" +
                "height:21px;" +
                "border-radius:20px;" +
                "background:#29292f;" +
                "position:relative;" +
                "}" +

                ".switch.on{" +
                "background:#c71921;" +
                "}" +

                ".knob{" +
                "width:15px;" +
                "height:15px;" +
                "border-radius:50%;" +
                "background:#eee;" +
                "position:absolute;" +
                "top:3px;" +
                "left:3px;" +
                "}" +

                ".switch.on .knob{" +
                "left:20px;" +
                "}" +

                ".section{" +
                "font-size:10px;" +
                "color:#77777f;" +
                "text-transform:uppercase;" +
                "letter-spacing:1px;" +
                "margin:14px 0 7px;" +
                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                "<div class='window'>" +

                "<div class='sidebar'>" +

                "<div class='logo'>E</div>" +
                "<div class='brand'>EAGLE</div>" +

                "<div class='nav active'>Combat</div>" +
                "<div class='nav'>Movement</div>" +
                "<div class='nav'>Player</div>" +
                "<div class='nav'>Visuals</div>" +
                "<div class='nav'>World</div>" +
                "<div class='nav'>Misc</div>" +

                "</div>" +

                "<div class='content'>" +

                "<div class='top'>" +

                "<div>" +
                "<div class='title'>Combat</div>" +
                "<div class='subtitle'>Configure your combat modules</div>" +
                "</div>" +

                "<div class='close'>×</div>" +

                "</div>" +

                "<div class='section'>Modules</div>" +

                "<div class='card'>" +
                "<div class='row'>" +
                "<div>" +
                "<div class='feature'>Aim Assist</div>" +
                "<div class='description'>Smooth camera assistance</div>" +
                "</div>" +
                "<div class='switch on'><div class='knob'></div></div>" +
                "</div>" +
                "</div>" +

                "<div class='card'>" +
                "<div class='row'>" +
                "<div>" +
                "<div class='feature'>Auto Clicker</div>" +
                "<div class='description'>Automatic clicking</div>" +
                "</div>" +
                "<div class='switch'><div class='knob'></div></div>" +
                "</div>" +
                "</div>" +

                "<div class='card'>" +
                "<div class='row'>" +
                "<div>" +
                "<div class='feature'>Reach</div>" +
                "<div class='description'>Attack distance</div>" +
                "</div>" +
                "<div style='color:#e21b23;font-weight:bold'>3.0</div>" +
                "</div>" +
                "</div>" +

                "</div>" +

                "</div>" +

                "</body>" +
                "</html>";
    }

    @Override
    public void onDestroy() {

        if (wm != null && bubble != null) {
            try {
                wm.removeView(bubble);
            } catch (Exception ignored) {
            }
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
            }
