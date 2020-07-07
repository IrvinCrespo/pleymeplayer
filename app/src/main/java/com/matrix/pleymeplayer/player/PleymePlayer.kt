package com.matrix.pleymeplayer.player

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.matrix.pleymeplayer.R
import com.matrix.pleymeplayer.player.util.TimeFormat
import com.matrix.pleymeplayer.player.util.TimeObject
import kotlinx.android.synthetic.main.pleyme_player.view.*



@Suppress("IMPLICIT_CAST_TO_ANY")
class PleymePlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    Player.EventListener, LifecycleObserver, SeekBar.OnSeekBarChangeListener {

    private lateinit var player : SimpleExoPlayer

    var timer:TimeObject? = null
    var controlsListener : PleymePlayerControlsListener? = null
    private var uri : Uri? = null

    var url : String? = null
    get() = field
    set(value){
        field = value
        uri = Uri.parse(value?:"")
    }

    var activityOwner: AppCompatActivity? = null
    get() = field
    set(value){
        field = value
        activityOwner?.lifecycle?.addObserver(this)
    }

    var lifeCyclePlayer:Lifecycle? = null
    get() = field
    set(value) {
        field = value
        field?.addObserver(this)
    }

    var showPlay : Boolean = true
    //get() = field
    set(value){
        field = value
        play.visibility = if (field) View.VISIBLE else View.INVISIBLE
    }

    var showNext : Boolean = true
    get() = field
    set(value){
        field = value
        next.visibility = if (field) View.VISIBLE else View.INVISIBLE
    }

    var showPrev: Boolean = true
    get() = field
    set(value){
        field = value
        prev.visibility = if (field) View.VISIBLE else View.INVISIBLE
    }
    //var url480= "https://playmeweb.s3.us-east-2.amazonaws.com/private/videos/streaming480/Steven_Universe_cap_7_Sub_Espa%C3%B1olstream480.m3u8"
    var url480 = "https://d1anetxzb8te48.cloudfront.net/private/videos/streaming480/425c59d1-8687-414f-8ac5-bb8c4e919631/425c59d1-8687-414f-8ac5-bb8c4e919631.m3u8?Expires=1593975654&Signature=GLbl74atTcHhZ4I7GqzrtliJ6AQx7k~BFQXXQBC5SzGy-qcwPOo5D0BmVKILSqgjI1n2L2n8IyUsmnJu633DO~nKy5EdP8KxSUHzKo9IXZSnHvBku~WiIKpjaKgfHugLAR6G5LmEXNacuSKGDbVrK1QP9DK1612ouQLn5egk5QcRY1xUNGr0GuY~CVCQAYcjk8bsiqcHylhseGIr5svyXOrF~zJ6gp8siiYnh~3h1nI4qrzB6Z6aF2-Dk70keqQf9IuA7~jtMGCj~yWtcGp~YHCyozl1xegj~Helyig5H11moNDxgJRPyjX-cguAjlB8XhC7sldBj9RKbpceg~xlTA__&Key-Pair-Id=APKAJADXE67QLYOKBFZA"
    var url720 = "https://playmeweb.s3.us-east-2.amazonaws.com/private/videos/streaming/Steven_Universe_cap_7_Sub_Espa%C3%B1olstream720.m3u8"

    private var handlerProgress = Handler(Looper.getMainLooper())
    private var runnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            val seconds = (player.currentPosition / 1000).toInt()
            val currentMinutes = (seconds / 60 ).toInt()
            val currentSeconds = seconds % 60
            currentTime.text = "${
                if(currentMinutes< 10)
                    "0$currentMinutes"
                else
                    currentMinutes
            }:${
                if(currentSeconds < 10)
                    "0$currentSeconds"
                else
                    currentSeconds
            }"
            seek.progress = (player.currentPosition * 100 / player.duration).toInt()
            seek.secondaryProgress = (player.contentBufferedPosition * 100 / player.duration).toInt()
            handlerProgress.postDelayed(this,1000)
        }
    }

    init {
        View.inflate(context, R.layout.pleyme_player,this)
        setupAttrs(attrs)
        setupHLS()
        seek.setOnSeekBarChangeListener(this)

        options.setOnClickListener { controlsListener?.onOptionsClicked() }
        next.setOnClickListener { controlsListener?.onNextClicked() }
        prev.setOnClickListener { controlsListener?.onPrevClicked() }
        play.setOnClickListener { changeState() }
        fullscreenSetup()

        playerlayout.setOnClickListener {
            controls_layout.animate().setListener(null)
            controls_layout.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(200)
            }
        }

        player_view.setOnClickListener {
            controls_layout.animate().alpha(1f).setDuration(10000)
        }

        controls_layout.setOnClickListener {
            it.apply {
                animate().alpha(0f).setDuration(200)
                .setListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                    override fun onAnimationEnd(animation: Animator?) {
                        it.visibility = View.GONE
                    }
                    override fun onAnimationCancel(animation: Animator?) {

                    }
                    override fun onAnimationStart(animation: Animator?) {

                    }
                })
            }
        }
    }

    fun onClickPlay(view:View){
        changeState()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun fullscreenSetup(){

        fullscreen.setOnClickListener {

            var currentOrientation = activityOwner?.getResources()?.configuration?.orientation
            if(currentOrientation == null){
                currentOrientation = activityOwner?.requestedOrientation
            }
            if(currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
                currentOrientation == ActivityInfo.SCREEN_ORIENTATION_USER)
                activityOwner?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            else if(currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                activityOwner?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            Handler().postDelayed(Runnable {
                activityOwner?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            },5000)

        }
    }

    private fun setupAttrs(attrs: AttributeSet?){
        val attributes = context.obtainStyledAttributes(attrs,R.styleable.PleymePlayer)

        val currentTimeAttr = attributes.getBoolean(R.styleable.PleymePlayer_showCurrentTime,true)
        val totalTimeAttr = attributes.getBoolean(R.styleable.PleymePlayer_showTotalTime,true)
        val optionsAttr = attributes.getBoolean(R.styleable.PleymePlayer_showOptions,true)

        val nextAttr = attributes.getBoolean(R.styleable.PleymePlayer_showNextButton,true)
        val prevAttr = attributes.getBoolean(R.styleable.PleymePlayer_showPrevButton,true)

        currentTime.visibility = if(currentTimeAttr) View.VISIBLE else View.INVISIBLE
        durationTime.visibility = if(totalTimeAttr) View.VISIBLE else View.INVISIBLE
        options.visibility = if(optionsAttr) View.VISIBLE else View.INVISIBLE
        next.visibility = if(optionsAttr) View.VISIBLE else View.INVISIBLE
        prev.visibility = if(prevAttr) View.VISIBLE else View.INVISIBLE

        attributes.recycle()
    }

    fun resize(orientation : Int){
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
            linearLayout.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            player_view.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
            fullscreen.setImageResource(R.drawable.ic_fullscreen_exit_white)
        }
        else{
            showSystemUI()
            fullscreen.setImageResource(R.drawable.ic_fullscreen_white)
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            linearLayout.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            player_view.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
    }

    private fun changeState(){

        if(player.playWhenReady){
            play.setImageResource(R.drawable.ic_play_white)
            player.playWhenReady = false
        }
        else{
            play.setImageResource(R.drawable.ic_pause_white)
            player.playWhenReady = true
        }
    }

    fun setupMedia(){
        /*
        var dataSourceFactory = DefaultDataSourceFactory(
            context,Util.getUserAgent(context,"peyme"))

        var mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://playmeweb.s3.us-east-2.amazonaws.com/private/videos/streaming/Steven_Universe_cap_7_Sub_Espa%C3%B1olstream720.m3u8"))
        */
    }

    fun setupHLS(){
        player = SimpleExoPlayer.Builder(context).build()
        player_view.player = player

        val dataSourceFactory =
             DefaultHttpDataSourceFactory(Util.getUserAgent(context, "pleyme"))

        val hlsMediaSource =
             HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url480))

        player.prepare(hlsMediaSource)
        player_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        player.playWhenReady = true
        player.addListener(this)
    }

    fun stopPlayback(){
        player.stop()
    }

    @SuppressLint("SetTextI18n")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if(playbackState == Player.STATE_READY){
            val time = TimeFormat.timeFromMilis(player.duration)
            timer = time
            durationTime.text = "${
                if(time.hours > 0){
                    if(time.hours < 10)
                        "0" + time.hours +":"
                    else
                        "" + time.hours + ":"
                }else ""
            }${
            if(time.minutes < 10)
                "0${time.minutes}"
            else
                time.minutes
            }:${
            if(time.seconds < 10)
                "0${time.seconds}"
            else
                time.seconds
            }"

            handlerProgress.postDelayed(runnable,0)
        }
        if(playbackState == Player.STATE_BUFFERING){
            seek.secondaryProgress = player.bufferedPercentage / 100
        }

    }

    private fun hideSystemUI() { // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        activityOwner?.window?.decorView?.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
    }

    private fun showSystemUI() {
        activityOwner?.window?.decorView?.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val newCurrent = progress * player.duration / 100
        if(fromUser){
            player.seekTo(newCurrent)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume(){
        //play.setImageResource(R.drawable.ic_pause_white)
        //player.playWhenReady = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause(){
        Log.d("lifecycle","from player")
        play.setImageResource(R.drawable.ic_play_white)
        player.playWhenReady = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy(){

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onStop(){

    }



}