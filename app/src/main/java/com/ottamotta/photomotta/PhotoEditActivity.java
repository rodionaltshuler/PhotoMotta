package com.ottamotta.photomotta;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ottamotta.photomotta.filler.Filler;
import com.ottamotta.photomotta.menu.GreenMenuItem;
import com.ottamotta.photomotta.menu.MenuItem;
import com.ottamotta.photomotta.menu.RevealMenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PhotoEditActivity extends Activity {

    private static final String STATE_CURRENT_SCENE = "current_scene";
    private static final String STATE_CURRENT_MODE = "current_mode";

    @InjectView(R.id.image)
    ImageView image;

    @InjectView(R.id.title)
    TextView title;

    BitmapEditor bitmapEditor;

    Scene[] mScenes;


    /** The current index for mScenes. */
    private int mCurrentScene;

    private MenuItem[] modes = new MenuItem[] {
            new RevealMenuItem(),
            new GreenMenuItem()
    };

    /** This is the custom Transition we use in this sample. */
    private TransitionSet mTransition;

    private int currentModeIndex;

    final View.OnClickListener showMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCurrentScene = (mCurrentScene + 1) % mScenes.length;
            // Pass the custom Transition as second argument for TransitionManager.go
            TransitionManager.go(mScenes[mCurrentScene], mTransition);
        }
    };

    final View.OnClickListener switchModeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentModeIndex = ++currentModeIndex % modes.length;
            setupMode(currentModeIndex);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        ButterKnife.inject(this);

        bitmapEditor = BitmapEditor.getInstance(getApplicationContext());

        if (null != savedInstanceState) {
            mCurrentScene = savedInstanceState.getInt(STATE_CURRENT_SCENE);
            currentModeIndex = savedInstanceState.getInt(STATE_CURRENT_MODE);
        }

        setupMode(currentModeIndex);


        setImage();

        image.setOnTouchListener(new Filler.FillTouchListener());
        image.setOnClickListener(switchModeListener);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        mScenes = new Scene[] {
                Scene.getSceneForLayout(container, R.layout.edit_menu_start, this),
                Scene.getSceneForLayout(container, R.layout.edit_menu_end, this)
        };


        mTransition = new TransitionSet();
        //mTransition.addTransition(new ChangeColor());
        mTransition.addTransition(new MoveAccelerateDecelerateTransition());

        // Show the initial Scene.
        TransitionManager.go(mScenes[mCurrentScene % mScenes.length]);

        mTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                initClickListenersForScene();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        initClickListenersForScene();

    }

    private void setImage() {
        Uri photoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        if (photoUri != null) {
            Bitmap bitmap = bitmapEditor.create(photoUri);
            image.setImageBitmap(bitmap);
        } else {
            image.setImageResource(R.drawable.dog);
        }
    }

    private void setupMode(int currentModeIndex) {
        this.currentModeIndex = currentModeIndex;
        MenuItem mode = modes[currentModeIndex];

        ImageView modeImageView = mode.createImageView(this);
        title.setText(mode.getTitle());

        ViewGroupUtils.replaceView(image, modeImageView);
        image = modeImageView;
        setImage();
        image.setOnTouchListener(new Filler.FillTouchListener());
        image.setOnClickListener(switchModeListener);
    }

    private void initClickListenersForScene() {
        View button = findViewById(R.id.toggle_menu);
        //button.setOnTouchListener(new Filler.FillTouchListener());
        button.setOnClickListener(showMenuListener);

        View revealButton = findViewById(R.id.item1);
        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupMode(0);
            }
        });

        View greenButton = findViewById(R.id.item2);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupMode(1);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_SCENE, mCurrentScene);
        outState.putInt(STATE_CURRENT_MODE, currentModeIndex);
    }


}
