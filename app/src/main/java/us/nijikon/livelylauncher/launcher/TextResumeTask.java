package us.nijikon.livelylauncher.launcher;

import android.os.AsyncTask;
import android.widget.TextView;

import us.nijikon.livelylauncher.R;

/**
 * Created by bowang .
 */
public class TextResumeTask extends AsyncTask<Void, Void, Void>{
    private TextView textView;
    TextResumeTask(TextView textView){
        this.textView = textView;
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
        textView.setText("How can I help you?");
    }
}
