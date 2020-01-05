package com.example.proxynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import static maes.tech.intentanim.CustomIntent.customType;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private VideoView videoView;
    private ArrayList<Integer> videolist;
    private int current ;
    private PopupWindow myPopup;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        current = new Random().nextInt(4);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(this,this);
        Field[] fields = R.raw.class.getFields();
        videolist = new ArrayList<>();
        for (int i=0; i<fields.length; i++){
            int resId = getResources().getIdentifier(fields[i].getName(),"raw",getPackageName());
            videolist.add(resId);
        }
        videoView = findViewById(R.id.videoView);
        relativeLayout = findViewById(R.id.layout);
        playVideo(current);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();
        if(Math.abs(diffX) > Math.abs(diffY)){
            // right or left swipe
            if(Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if(diffX > 0) {
                    onSwipeRight();
                }
                else{
                    onSwipeLeft();
                }
                result = true;
            }
        }
        else{
            if(Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if(diffY>0) {
                    onSwipeDown();
                }
                else{
                    onSwipeUp();
                }
                result = true;
            }
        }

        return result;
    }

    private void onSwipeDown() {
//        Toast.makeText(this,"Down",Toast.LENGTH_SHORT).show();
        if(current == 0){
            Toast.makeText(this,"No More Videos",Toast.LENGTH_SHORT).show();
        }
        else{
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_down);
            relativeLayout.startAnimation(animation);
            current = current-1;
            playVideo(current);
        }
    }

    private void onSwipeUp() {
//        Toast.makeText(this,"Up",Toast.LENGTH_SHORT).show();
        if(current == 4){
            Toast.makeText(this,"No More Videos",Toast.LENGTH_SHORT).show();
        }
        else{
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_up);
            relativeLayout.startAnimation(animation);
            current = current+1;
            playVideo(current);
        }
    }
    private void onSwipeLeft() {
        startActivity(new Intent(this,PopUpActivity.class));
        videoView.pause();
        customType(MainActivity.this,"left-to-right");
    }

    private void onSwipeRight() {
        startActivity(new Intent(this,ProfileActivity.class));
        customType(MainActivity.this,"right-to-left");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void playVideo(int position){
        String videoPath = "android.resource://" + getPackageName() + "/" + videolist.get(position);
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

}