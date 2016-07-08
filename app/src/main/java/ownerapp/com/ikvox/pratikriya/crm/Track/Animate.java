package ownerapp.com.ikvox.pratikriya.crm.Track;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by MyMac on 23/06/16.
 */
public class Animate {

        public static void animate(View holder , boolean goesDown){


            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder, "translationY", goesDown==true ? 50 : -50, 0);
            animatorTranslateY.setDuration(1000);


            ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder,"translationX",-0,0,-0,0,-0,0,-0,0,0);
            animatorTranslateX.setDuration(1000);

            animatorSet.playTogether(animatorTranslateX,animatorTranslateY);

            //animatorSet.playTogether(animatorTranslateY);
            animatorSet.start();

        }































}
