package esi.siw.nouzha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

import io.paperdb.Paper;

public class OnBoardActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        String firstTime = Paper.book().read("firstTime");
        if(firstTime != null) {
            Intent intent = new Intent(OnBoardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setNextText(this.getString(R.string.next));
            setCancelText(this.getString(R.string.cancel));
            setPrevText(this.getString(R.string.previous));
            setFinishText(this.getString(R.string.finish));


            addFragment(new Step.Builder().setTitle("FIND")
                    .setContent("THE NEAREST PLACES TO YOU")
                    .setBackgroundColor(Color.parseColor("#00985f")) // int background color
                    .setDrawable(R.drawable.find) // int top drawable
                    .build());
            addFragment(new Step.Builder().setTitle("SAVE")
                    .setContent("YOUR TIME AND YOUR MONEY")
                    .setBackgroundColor(Color.parseColor("#00985f")) // int background color
                    .setDrawable(R.drawable.save) // int top drawable
                    .build());
            addFragment(new Step.Builder().setTitle("LET")
                    .setContent("YOUR JOURNEY BEGIN")
                    .setBackgroundColor(Color.parseColor("#00985f")) // int background color
                    .setDrawable(R.drawable.begin) // int top drawable
                    .build());
        }



    }


    @Override
    public void finishTutorial() {
        super.finishTutorial();
        Intent intent = new Intent(OnBoardActivity.this, SignIn.class);
        startActivity(intent);
        // Init paper
        Paper.init(this);
        Paper.book().write("firstTime", "no" );
    }


}
