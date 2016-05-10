package us.nijikon.livelylauncher.launcher;

import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;

/**
 * Created by bowang .
 */
public class TextResumeTask extends AsyncTask<Void, Void, Void>{
    private TextView textView;
    private ImageButton button;
    TextResumeTask(TextView textView,ImageButton button){
        this.textView = textView;
        this.button = button;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(8000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        if(textView != null) {
            textView.setText("How can I help you?");
        }
        if(button != null){
            button.setAlpha(0.6f);
        }
    }
}
