package us.nijikon.livelylauncher.speech;

/**
 * Created by Ray on 2016/4/8.
 */
public interface RecognitionHelper {

    //The entry
    //Handle click event
    public void handleClickEvent(int keepGoing);

    public void setSpeechResultListener(SpeechResultListener speechResultListener);

}
