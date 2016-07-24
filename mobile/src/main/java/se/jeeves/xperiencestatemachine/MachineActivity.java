package se.jeeves.xperiencestatemachine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MachineActivity extends AppCompatActivity implements OnClickListener{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Button startButton;
    private MessageHandler handler;
    private static MachineActivity machineActivity;

    public Messenger mMessenger = new Messenger(new MessageHandler(this));
    private class MessageHandler extends Handler{
        private Context c;

        MessageHandler(Context c){
            this.c = c;
        }
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MachineStates.RUN:
                    Toast.makeText(getApplicationContext(), "RUN", Toast.LENGTH_LONG).show();
                    startMachine();
                    break;
                case MachineStates.STOP:
                    Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_LONG).show();
                    stopMachine();
                    break;
                case MachineStates.DOWNLOADSTARTED:
                    Toast.makeText(getApplicationContext(), "Download Started", Toast.LENGTH_LONG).show();
                    break;
                case MachineStates.DOWNLOADFINISHED:
                    Toast.makeText(getApplicationContext(), "Download Finished", Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        machineActivity = this;

        setContentView(R.layout.activity_machine);
        findViewById(R.id.machine_layout).setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.red));

        mVisible = false;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.imageView);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        final Button button = (Button) findViewById(R.id.machine_button);
        button.setOnClickListener(this);
        toggle();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }
    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public static MachineActivity getMainActivity(){
        return machineActivity;
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent serv = new Intent(MachineActivity.this, LongRunningService.class);
        startService(serv);
    }

    private void startMachine() {

        View v = findViewById(R.id.imageView);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        findViewById(R.id.machine_layout).setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.green));
        if (v != null) {
            v.startAnimation(rotation);
        }
    }

    private void stopMachine() {
        View v = findViewById(R.id.imageView);
        findViewById(R.id.machine_layout).setBackgroundColor(ContextCompat.getColor(this.getApplicationContext(), R.color.red));

        if (v != null) {
            v.clearAnimation();
        }


    }
    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}