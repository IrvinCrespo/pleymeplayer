package com.matrix.pleymeplayer.player

interface PleymePlayerControlsListener {
    fun onOptionsClicked()
    fun onPlayClicked()
    fun onNextClicked()
    fun onPrevClicked()
    fun onFullScreenClicked(currentOrientation:Int, newOrientation:Int)
    fun onFullscreenExitLClicked()
}