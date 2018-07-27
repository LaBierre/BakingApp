package com.example.standard.bakingapp.fragments;


import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.standard.bakingapp.R;
import com.example.standard.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements Player.EventListener {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private TextView description;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private Uri mediaUri;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;

    private Step step;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

    private ImageButton mPreviousVideo, mNextVideo;

    private int mStepIndex;
    private boolean mIsLandscape;

    private List<Step> mStepItems;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        step = new Step();
        //mStepItems = new ArrayList<>();
        Bundle bundle;

        // Todo: Unterscheiden ob die Daten über die Activity kommen oder direkt an das Fragment gesendet werden

        if (getActivity().findViewById(R.id.fragment_tablet_layout) != null){
            bundle = getArguments();
        } else {
            bundle = getActivity().getIntent().getExtras();
        }

        step = bundle.getParcelable("step");
        mStepItems = bundle.getParcelableArrayList("bundle");

        mStepIndex = step.getmIndex();
        mDescription = step.getmDescription();
        mVideoUrl = step.getmVideoURL();
        mThumbnailUrl = step.getmThumbnailUrl();

        Log.d(LOG_TAG, "DetailFragment mDescription" + mDescription);
        Log.d(LOG_TAG, "DetailFragment mVideoUrl" + mVideoUrl);
        Log.d(LOG_TAG, "DetailFragment mStepItems" + mStepItems.size());
        for (int i=0; i<mStepItems.size(); i++){
            Log.d(LOG_TAG, "DetailFragment mStepItems" + mStepItems.get(i).getmVideoURL());
        }
        mIsLandscape = getResources().getBoolean(R.bool.landscape);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("Test", "MasterListFragment");
        Log.d(LOG_TAG, "DetailFragment: onCreateView");

        /*
         * Hier wird die Sache mit dem ExoPlayer und die Darstellung des Zubereitungs-
         * schritts gehandled
         * */

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        mPreviousVideo = (ImageButton) rootView.findViewById(R.id.btn_back);
        mNextVideo = (ImageButton) rootView.findViewById(R.id.btn_next);

//        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
//                R.drawable.question_mark));

        //exoPlayer = (ExoPlayer) rootView.findViewById(R.id.player_view);
        description = (TextView) rootView.findViewById(R.id.tv_description);
        // setMovementMethod macht TextViews scrollbar
        description.setMovementMethod(new ScrollingMovementMethod());
        //description.setText(mDescription);

        if (mIsLandscape){
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPlayerView.setLayoutParams(lp);
            mPreviousVideo.setVisibility(View.GONE);
            mNextVideo.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
        } else {
            mPreviousVideo.setVisibility(View.VISIBLE);
            mNextVideo.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
        }

        initializeDisplay();

        // Initialize the player.
        //initializePlayer(Uri.parse(mVideoUrl));
        mPreviousVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                mStepIndex--;
                mVideoUrl = mStepItems.get(mStepIndex).getmVideoURL();
                mThumbnailUrl = mStepItems.get(mStepIndex).getmThumbnailUrl();
                mDescription = mStepItems.get(mStepIndex).getmDescription();
                initializeDisplay();
            }
        });
        mNextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                mStepIndex++;
                mVideoUrl = mStepItems.get(mStepIndex).getmVideoURL();
                mThumbnailUrl = mStepItems.get(mStepIndex).getmThumbnailUrl();
                mDescription = mStepItems.get(mStepIndex).getmDescription();
                initializeDisplay();
            }
        });

        return rootView;
    }

    public void initializeDisplay(){
        description.setText(mDescription);

        if (!mVideoUrl.equals("")){
            initializePlayer(Uri.parse(mVideoUrl));
        } else if (!mThumbnailUrl.equals("")){
            initializePlayer(Uri.parse(mThumbnailUrl));
        } else {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
                    R.drawable.question_mark));
            //mPlayerView.setBackground(R.drawable.question_mark);
        }
    }

   public void initializePlayer(Uri mediaUri){
        if (mExoPlayer == null){

            String userAgent = Util.getUserAgent(getContext(), "BackingApp");

            // Instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the M ediaSource
            Log.d("Test", "User Agent = " + userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    // Release ExoPlayer
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        // Todo: showNotification(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
