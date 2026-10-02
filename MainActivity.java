package com.eagle.client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(40,60,40,40);

        TextView title = new TextView(this);
        title.setText("EAGLE CLIENT");
        title.setTextSize(28);
        title.setTextColor(0xffe01919);
        box.addView(title);

        TextView info = new TextView(this);
        info.setText("\nClient-side Android overlay prototype\n\nGrant overlay permission, then start Eagle Client.");
        info.setTextSize(16);
        box.addView(info);

        Button permission = new Button(this);
        permission.setText("Grant overlay permission");
        permission.setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });
        box.addView(permission);

        Button start = new Button(this);
        start.setText("Start Eagle Client");
        start.setOnClickListener(v -> {
            if (Settings.canDrawOverlays(this)) {
                startService(new Intent(this, EagleOverlayService.class));
                Intent mc = getPackageManager().getLaunchIntentForPackage("com.mojang.minecraftpe");
                if (mc != null) startActivity(mc);
            }
        });
        box.addView(start);

        setContentView(box);
    }
}
