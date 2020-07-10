package com.matrix.pleymeplayer

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.matrix.pleymeplayer.player.PleymePlayerControlsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PleymePlayerControlsListener {

    var url480 = "https://d1anetxzb8te48.cloudfront.net/private/videos/streaming480/425c59d1-8687-414f-8ac5-bb8c4e919631/425c59d1-8687-414f-8ac5-bb8c4e919631.m3u8?Expires=1593975654&Signature=GLbl74atTcHhZ4I7GqzrtliJ6AQx7k~BFQXXQBC5SzGy-qcwPOo5D0BmVKILSqgjI1n2L2n8IyUsmnJu633DO~nKy5EdP8KxSUHzKo9IXZSnHvBku~WiIKpjaKgfHugLAR6G5LmEXNacuSKGDbVrK1QP9DK1612ouQLn5egk5QcRY1xUNGr0GuY~CVCQAYcjk8bsiqcHylhseGIr5svyXOrF~zJ6gp8siiYnh~3h1nI4qrzB6Z6aF2-Dk70keqQf9IuA7~jtMGCj~yWtcGp~YHCyozl1xegj~Helyig5H11moNDxgJRPyjX-cguAjlB8XhC7sldBj9RKbpceg~xlTA__&Key-Pair-Id=APKAJADXE67QLYOKBFZA"


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
