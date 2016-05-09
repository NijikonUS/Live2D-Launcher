package us.nijikon.livelylauncher.speech;

/**
 * Created by Ray on 2016/4/17.
 */
public interface SpeechResultListener {

    public void handleResult(String query, String content, String response);
}
