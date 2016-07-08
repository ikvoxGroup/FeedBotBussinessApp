package ownerapp.com.ikvox.pratikriya.ProgBarAppear;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by MyMac on 30/05/16.
 */
public class AndroidUtils {
    public static void animateView(final View view, ImageView iv, AnimationDrawable animate, final int toVisibility, float toAlpha, int duration) {

        boolean show = toVisibility == View.VISIBLE;

        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
        iv.setVisibility(View.VISIBLE);
        animate.start();
    }
}
