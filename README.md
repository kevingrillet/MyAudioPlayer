# MyMusicPlayer
 Test implementation of two java audio players.
 
## Two different audio players

| | javafx.scene.media.MediaPlayer | javax.sound.sampled.Clip |
| ------ | ------ | ------ |
| Audio formats | .aif, .aiff, .aifc, .m4a, **.mp3**, .wav, .WAV | .aif, .aiff, .aifc, .m4a, .wav, .WAV |
| Volume | `mediaPlayer.setVolume(volume)` | Can't? |
| Get time | `mediaPlayer.getCurrentTime()` | `clip.getMicrosecondPosition()` |
| Seek | `mediaPlayer.seek(Duration.millis(time))` | `clip.setMicrosecondPosition((long) (time*1000))` |
| Audio mixer | Windows default output | `AudioSystem.getClip(mixerInfo)` |
| Change media | Create a new MediaPlayer | `clip.open(AudioSystem.getAudioInputStream(musicFile));` |
| Observable | `currentTimeProperty, setOnReady, setOnEndOfMedia, ...` | Nothing? |

## To choose the active one
### javafx.scene.media.MediaPlayer
To use this audio player, set `myAudioPlayer = new MyMediaPlayer(bean)` in `Controller.initialize()`.
### javax.sound.sampled.Clip
To use this audio player, set `myAudioPlayer = new MyClip(bean)` in `Controller.initialize()`.

## ToDo
- [ ] Volume in `AudioPLayer/MyCLip`
- [ ] Fix the lag machine in `AudioPlayer/MyMediaPlayer`