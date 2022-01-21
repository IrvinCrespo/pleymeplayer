# Pleyme player

Pleyme player is a custom player for Pleyme app implementation.

## Features

You can play hls, dash or stream mp4 files from url. (Now it works just by url) due to is based on [ExoPlayer](https://github.com/google/ExoPlayer) project

## Player Properties

* activityOwner
* lifeCyclePlayer
* showPlay
* showNext
* showPrev
* showLiveTag
* showProgress
* repeat


## Functions

* onOptionsClicked
* onPlayClicked
* onNextClicked
* onPrevClicked
* onFullScreenClicked(currentOrientation:Int, newOrientation:Int)
* onFullscreenExitLClicked


## Controls

| Name        | Type           | Description  |
| ------------- |:-------------:| -----:|
| activityOwner      | AppCompatActivity | Activity that owns the player |
| lifeCyclePlayer      | Lifecycle      |    |
| showPlay | Boolean      |    show/hide play button |
| showNext | Boolean      |   show/hide next button  |
| showPrev | Boolean      |    show/hide previous button |
| showLiveTag | Boolean      |    show/hide live icon |
| showProgress | Boolean      |    show/hide time and progress bar |
| repeat | Boolean      |    repeat video when finishes |
