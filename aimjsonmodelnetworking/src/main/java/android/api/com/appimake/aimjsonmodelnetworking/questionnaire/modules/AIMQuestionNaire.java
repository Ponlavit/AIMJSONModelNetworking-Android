package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.modules;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.inf.AIMQTNDataInterface;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.QTNModel;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJAnswers;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJQuestions;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJResults;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
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

    private AIMQTNDataInterface callBack = setCallBack();

    private QTNModel listQTN = setQuestion();
    private LinearLayout tray;
    private double score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        this.score = 0.0;

        ScrollView main = new ScrollView(getContext());
        main.setLayoutParams(params);

        this.tray = new LinearLayout(getContext());
        this.tray.setLayoutParams(params);
        this.tray.setBackgroundColor(Color.parseColor("#e5fff6"));
        this.tray.setOrientation(LinearLayout.VERTICAL);

        showQuestion(0);
        main.addView(tray);
        return main;
    }

    private void showQuestion(long questionID) {
        tray.removeAllViews();
        OBJQuestions objQuestion = listQTN.getQuestionByID(questionID);

        LinearLayout llQuestion = new LinearLayout(getContext());
        LinearLayout.LayoutParams paramsllQuestion = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsllQuestion.setMargins(8, 16, 8, 16);
        llQuestion.setLayoutParams(paramsllQuestion);
        llQuestion.setBackgroundColor(Color.parseColor("#f7f574"));
        llQuestion.setOrientation(LinearLayout.VERTICAL);

        TextView newTV = new TextView(getContext());
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
            paramsllQuestion.setMargins(8, 16, 8, 16);
            llAnswer.setLayoutParams(paramsllQuestion);
            llAnswer.setBackgroundColor(Color.parseColor("#f7f6f5"));
            llAnswer.setOrientation(LinearLayout.VERTICAL);

            Button newBT = new Button(getContext());
            newBT.setText(Html.fromHtml(objAnswer.getAnswer_text()));
            newBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    score += objAnswer.getAnswer_score();
                    showNext(objAnswer.getAction());
                }
            });
            llAnswer.addView(newBT);

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
        paramsllResult.setMargins(8, 16, 8, 16);
        llResult.setLayoutParams(paramsllResult);
        llResult.setBackgroundColor(Color.parseColor("#f7f574"));
        llResult.setOrientation(LinearLayout.VERTICAL);

        TextView tvHeader = new TextView(getContext());
        tvHeader.setText(Html.fromHtml(objResult.getResult_header()));
        llResult.addView(tvHeader);

        TextView tvDescription = new TextView(getContext());
        tvDescription.setText(Html.fromHtml(objResult.getResult_description()));
        llResult.addView(tvDescription);

        Button btFinish = new Button(getContext());
        btFinish.setText("Finish");
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.result(score, objResult);
            }
        });
        llResult.addView(btFinish);

        this.tray.addView(llResult);
    }

    public abstract QTNModel setQuestion();
    public abstract AIMQTNDataInterface setCallBack();
}
