package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.modules;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.inf.AIMQTNDataInterface;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.QTNModel;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJAnswers;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJQuestions;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJResults;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.style.QTNStyle;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public abstract class AIMQuestionnaire extends Fragment {

    private AIMQTNDataInterface callBack;

    private QTNModel listQTN;
    private LinearLayout tray;
    private double score;

    private int overAllMargin;
    private QTNStyle style;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initValue();

        ScrollView main = new ScrollView(getContext());
        main.setLayoutParams(params);

        this.tray = new LinearLayout(getContext());
        this.tray.setLayoutParams(params);
        this.tray.setBackgroundColor(Color.parseColor(style.getBackgroundHEXColor()));
        this.tray.setOrientation(LinearLayout.VERTICAL);

        showQuestion(0);
        main.addView(tray);
        return main;
    }

    private void initValue() {
        this.score = 0.0;
        this.style = setStyle();
        this.callBack = setCallBack();
        this.listQTN = setQuestion();
        if (setMarginInDP() > 0)
            this.overAllMargin = (int) setMarginInDP();
    }

    private void showQuestion(long questionID) {
        tray.removeAllViews();
        OBJQuestions objQuestion = listQTN.getQuestionByID(questionID);

        LinearLayout llQuestion = new LinearLayout(getContext());
        LinearLayout.LayoutParams paramsllQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsllQuestion.setMargins(overAllMargin * 2, overAllMargin * 2, overAllMargin * 2, overAllMargin * 2);
        llQuestion.setLayoutParams(paramsllQuestion);
        llQuestion.setBackgroundColor(Color.parseColor(style.getBackgroundTitleHEXColor()));
        llQuestion.setOrientation(LinearLayout.VERTICAL);
        llQuestion.setPadding(overAllMargin, overAllMargin, overAllMargin, overAllMargin);

        TextView newTV = new TextView(getContext());
        newTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.getTitleFontSize());
        newTV.setText(Html.fromHtml(objQuestion.getQuestion_text()));
        llQuestion.addView(newTV);

        this.tray.addView(llQuestion);

        showAnswer(objQuestion);
    }

    private void showAnswer(OBJQuestions objQuestion) {
        for (int i = 0; i < objQuestion.getAnswers().size(); i++) {
            final OBJAnswers objAnswer = (OBJAnswers) objQuestion.getAnswers().get(i);

            LinearLayout llAnswer = new LinearLayout(getContext());
            LinearLayout.LayoutParams paramsllQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsllQuestion.setMargins(overAllMargin * 2, overAllMargin, overAllMargin * 2, overAllMargin);
            llAnswer.setLayoutParams(paramsllQuestion);
            llAnswer.setBackgroundColor(Color.parseColor(style.getBackgroundAnswerHEXColor()));
            llAnswer.setOrientation(LinearLayout.VERTICAL);

            final Button btAnswer = new Button(getContext());
            btAnswer.setBackgroundColor(Color.parseColor(style.getBackgroundAnswerHEXColor()));
            btAnswer.setText(Html.fromHtml(objAnswer.getAnswer_text()));
            btAnswer.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.getDetailFontSize());
            btAnswer.setPadding(overAllMargin, overAllMargin, overAllMargin, overAllMargin);
            btAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    score += objAnswer.getAnswer_score();
                    showNext(objAnswer.getAction());
                }
            });
            btAnswer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            btAnswer.getBackground().setColorFilter(0x12000000, PorterDuff.Mode.SRC_ATOP);
                            btAnswer.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            btAnswer.getBackground().clearColorFilter();
                            btAnswer.invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });
            llAnswer.addView(btAnswer);

            this.tray.addView(llAnswer);
        }
    }

    private void showNext(String next){
        String string = next;
        String[] parts = string.split(":");
        String action = parts[0];
        String id = "0";
        if(parts.length>1)
            id = parts[1];

        switch (action){
            case "showquestion" :
                showQuestion(Long.parseLong(id));
                break;
            case "showresult" :
                showResult(Long.parseLong(id));
                break;
            case "checkscore" :
                checkResult();
                break;
            default: break;
        }
    }

    private void checkResult(){
        AIMArrayList<OBJResults> results = listQTN.getResults();
        for (int i = 0; i < results.size(); i++) {
            OBJResults objResult = (OBJResults) results.get(i);
            if(score >= objResult.getResult_from() && score <= objResult.getResult_to()) {
                showResult(objResult.unique_id);
                return;
            }
        }
    }

    private void showResult(long resultID){
        this.tray.removeAllViews();
        final OBJResults objResult = listQTN.getResultsByID(resultID);

        LinearLayout llResult = new LinearLayout(getContext());
        LinearLayout.LayoutParams paramsllResult = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsllResult.setMargins(overAllMargin * 2, overAllMargin, overAllMargin * 2, overAllMargin);
        llResult.setLayoutParams(paramsllResult);
        llResult.setBackgroundColor(Color.parseColor(style.getBackgroundTitleResultHEXColor()));
        llResult.setOrientation(LinearLayout.VERTICAL);
        llResult.setPadding(overAllMargin, overAllMargin, overAllMargin, overAllMargin);

        TextView tvHeader = new TextView(getContext());
        tvHeader.setText(Html.fromHtml(objResult.getResult_header()));
        tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.getTitleFontSize());
        llResult.addView(tvHeader);

        TextView tvDescription = new TextView(getContext());
        tvDescription.setText(Html.fromHtml(objResult.getResult_description()));
        tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.getDetailFontSize());
        llResult.addView(tvDescription);

        final Button btFinish = new Button(getContext());
        btFinish.setBackgroundColor(Color.parseColor(style.getBackgroundAnswerHEXColor()));
        btFinish.setText("Finish");
        btFinish.setPadding(overAllMargin, overAllMargin, overAllMargin, overAllMargin);
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.result(score, objResult);
            }
        });
        btFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        btFinish.getBackground().setColorFilter(0x12000000, PorterDuff.Mode.SRC_ATOP);
                        btFinish.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        btFinish.getBackground().clearColorFilter();
                        btFinish.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        llResult.addView(btFinish);

        this.tray.addView(llResult);
    }

    public abstract QTNModel setQuestion();
    public abstract AIMQTNDataInterface setCallBack();

    public abstract float setMarginInDP();

    public abstract QTNStyle setStyle();
}
