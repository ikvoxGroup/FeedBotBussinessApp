package ownerapp.com.ikvox.pratikriya.Dialogs;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import ownerapp.com.ikvox.pratikriya.R;
import ownerapp.com.ikvox.pratikriya.Settings;


public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {
    CardView cardView1, cardView2, cardView3, cardView4, cardView5, cardView6, cardView7, cardView8, cardView9, cardView10;
    Button buttonDisagree, buttonAgree;
    View view;
    int currentTheme;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ActivityOptions options;
    Intent intent;
    Boolean themeChanged = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Save current theme to use when user press dismiss inside dialog
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("THEME", 0);

        //inflate theme_dialog.xml
        view = inflater.inflate(R.layout.theme_dialog, container);

        // remove title (already defined in theme_dialog.xml)
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Declare buttons and onClick methods
        dialogButtons();

        return view;
    }

    private void dialogButtons() {
        cardView1 = (CardView) view.findViewById(R.id.card_view1);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        cardView3 = (CardView) view.findViewById(R.id.card_view3);
        cardView4 = (CardView) view.findViewById(R.id.card_view4);
        cardView5 = (CardView) view.findViewById(R.id.card_view5);
        cardView6 = (CardView) view.findViewById(R.id.card_view6);
        cardView7 = (CardView) view.findViewById(R.id.card_view7);
        cardView8 = (CardView) view.findViewById(R.id.card_view8);
        cardView9 = (CardView) view.findViewById(R.id.card_view9);
        cardView10 = (CardView) view.findViewById(R.id.card_view10);
        buttonDisagree = (Button) view.findViewById(R.id.buttonDisagree);
        buttonAgree = (Button) view.findViewById(R.id.buttonAgree);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);
        cardView8.setOnClickListener(this);
        cardView9.setOnClickListener(this);
        cardView10.setOnClickListener(this);
        buttonDisagree.setOnClickListener(this);
        buttonAgree.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view1:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(1);
                cardView1.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view2:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(2);
                cardView2.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view3:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(3);
                cardView3.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view4:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(4);
                cardView4.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view5:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(5);
                cardView5.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view6:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(6);
                cardView6.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view7:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(7);
                cardView7.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view8:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(8);
                cardView8.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view9:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(9);
                cardView9.setBackgroundColor(Color.BLACK);
                break;
            case R.id.card_view10:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((Settings) getActivity()).setThemeFragment(10);
                cardView10.setBackgroundColor(Color.BLACK);
                break;
            case R.id.buttonDisagree:
                sharedPreferences.edit().putBoolean("THEMECHANGED", false).apply();
                ((Settings) getActivity()).setThemeFragment(currentTheme);
                getDialog().dismiss();
                break;
            case R.id.buttonAgree:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
                break;
        }
    }
}