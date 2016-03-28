package us.nijikon.livelylauncher.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import us.nijikon.livelylauncher.R;

public class RemindActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        LinearLayout linear = (LinearLayout)findViewById(R.id.linear);
        linear.getBackground().setAlpha(130);
    }
}
