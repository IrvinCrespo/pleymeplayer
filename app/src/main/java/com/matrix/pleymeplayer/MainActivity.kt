package com.matrix.pleymeplayer

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.matrix.pleymeplayer.player.PleymePlayerControlsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PleymePlayerControlsListener {

    var url480 = "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8"


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        player.activityOwner = this
        player.lifeCyclePlayer = lifecycle
        player.controlsListener = this
        //player.showPrev = false
        player.repeat = false
        player.url480 = url480
        player.setupHLS()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        player.resize(newConfig.orientation)
        /*
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
        }
        else{
            showSystemUI()
        }
        */

    }

    fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
    }

    fun showSystemUI() {
        window.decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }

    override fun onOptionsClicked() {

    }

    override fun onPlayClicked() {

    }

    override fun onNextClicked() {

    }

    override fun onPrevClicked() {

    }

    override fun onFullScreenClicked(currentOrientation: Int, newOrientation: Int) {

    }

    override fun onFullscreenExitLClicked() {

    }
}
