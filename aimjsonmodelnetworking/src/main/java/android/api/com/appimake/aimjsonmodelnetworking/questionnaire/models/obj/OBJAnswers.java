package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

import java.util.ArrayList;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public class OBJAnswers extends AIMModel {
    @JSONVariable
    String answer_text;
    @JSONVariable
    double answer_score;
    @JSONVariable
    String action;
    @JSONVariable
    ArrayList contradiction;

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public double getAnswer_score() {
        return answer_score;
    }

    public void setAnswer_score(double answer_score) {
        this.answer_score = answer_score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList getContradiction() {
        return contradiction;
    }

    public void setContradiction(ArrayList contradiction) {
        this.contradiction = contradiction;
    }

    @Override
    public String getObjectIdentificationRequest() {
        return null;
    }

    @Override
    public String getKeyForObjectIdentificationRequest() {
        return null;
    }
}
