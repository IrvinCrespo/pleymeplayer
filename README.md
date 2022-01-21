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

## Example

You can use properties programatically...

```kotlin

var url480 = "https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8"

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    view = binding.root
    setContentView(view)

    binding.mainTvVersion.text = version

    binding.player.activityOwner = this
    binding.player.lifeCyclePlayer = lifecycle
    binding.player.controlsListener = this
    //player.showPrev = false
    binding.player.repeat = false
    binding.player.url480 = url480
    binding.player.setupHLS()


}
```

or in xml file

```xml
 <com.matrix.pleymeplayer.player.PleymePlayer
        android:id="@+id/player"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        android:layout_width="match_parent"
        app:showLiveTag="false"
        android:layout_height="300dp"/>
```
