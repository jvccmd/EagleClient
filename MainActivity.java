package com.eagle.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.net.Uri;
import android.view.Gravity;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(
                android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        );

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(45, 35, 45, 35);
        root.setBackgroundColor(Color.rgb(10, 10, 12));

        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setGravity(Gravity.CENTER_VERTICAL);
        panel.setPadding(35, 25, 35, 25);

        TextView title = new TextView(this);
        title.setText("EAGLE CLIENT");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("Minecraft Bedrock Edition");
        subtitle.setTextColor(Color.GRAY);
        subtitle.setTextSize(15);
        subtitle.setGravity(Gravity.CENTER);

        Button permission = new Button(this);
        permission.setText("Grant Overlay Permission");

        permission.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivity(intent);
        });

        Button launch = new Button(this);
        launch.setText("LAUNCH MINECRAFT");

        launch.setOnClickListener(v -> launchMinecraft());

        panel.addView(title);
        panel.addView(subtitle);

        panel.addView(permission,
                new LinearLayout.LayoutParams(
                        -1,
                        60
                ));

        panel.addView(launch,
                new LinearLayout.LayoutParams(
                        -1,
                        70
                ));

        root.addView(panel,
                new LinearLayout.LayoutParams(
                        520,
                        -1
                ));

        setContentView(root);
    }

    private void launchMinecraft() {

        if (!Settings.canDrawOverlays(this)) {
            Intent settings = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );

            startActivity(settings);
            return;
        }

        Intent service = new Intent(this, EagleOverlayService.class);
        startService(service);

        Intent minecraft =
                getPackageManager().getLaunchIntentForPackage(
                        "com.mojang.minecraftpe"
                );

        if (minecraft != null) {
            minecraft.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(minecraft);
        } else {
            TextView error = new TextView(this);
            error.setText(
                    "Minecraft Bedrock was not found on this device."
            );
            error.setTextColor(Color.RED);
            error.setTextSize(16);
            error.setGravity(Gravity.CENTER);

            setContentView(error);
        }
    }
}
