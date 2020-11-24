# MyAudioPlayer
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Java Development Kit, version 15](https://img.shields.io/badge/Java-JDK15.0.1-red?logo=java)](https://jdk.java.net/15/)
[![JavaFX](https://img.shields.io/badge/JavaFX-15-red)](https://gluonhq.com/products/javafx/)


 Test implementation of two java audio players.
 
## Two different audio players

| | [javafx.scene.media.MediaPlayer](https://docs.oracle.com/javafx/2/api/javafx/scene/media/MediaPlayer.html) | [javax.sound.sampled.Clip](https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/Clip.html) |
| ------ | ------ | ------ |
| Audio formats | :warning: .aif, .aiff, .aifc, .m4a, **.mp3**, .wav, .WAV | :warning: .aif, .aiff, .aifc, .m4a, .wav, .WAV |
| Volume | :heavy_check_mark: `void setVolume(double value)` | :warning: `((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(float newValue)` [:books:](https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/FloatControl.html) [:bookmark:](https://github.com/jenis23/Super-Mario-Bros/blob/master/jig-engine-1.7/src/jig/engine/audio/jsound/ClipPlayback.java)|
| Get time | :heavy_check_mark: `Duration getCurrentTime()` | :heavy_check_mark: `long getMicrosecondPosition()` |
| Seek | :heavy_check_mark: `void	seek(Duration seekTime)` | :heavy_check_mark: `void setMicrosecondPosition(long microseconds)` [:books:](https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/DataLine.html) |
| Audio mixer | :x: *Windows default output* | :heavy_check_mark: `static Clip getClip(Mixer.Info mixerInfo)` [:books:](https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/AudioSystem.html) |
| Change media | :warning: *Create a new MediaPlayer* | :heavy_check_mark: `void open(AudioInputStream stream)` |
| Property | :heavy_check_mark: `currentTimeProperty, ...` | :x: |
| Event | :heavy_check_mark: `setOnReady, setOnEndOfMedia, ...` | :x: |

## To choose the active one
### javafx.scene.media.MediaPlayer
To use this audio player, set `myAudioPlayer = new MyMediaPlayer(bean)` in `Controller.initialize()`.
### javax.sound.sampled.Clip
To use this audio player, set `myAudioPlayer = new MyClip(bean)` in `Controller.initialize()`.

## IntelliJ IDEA
Follow the JavaFX help: <https://www.jetbrains.com/help/idea/javafx.html>

Set VM options : `--module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.media,javafx.fxml`

## ToDo
- [ ] Fix the lag machine
