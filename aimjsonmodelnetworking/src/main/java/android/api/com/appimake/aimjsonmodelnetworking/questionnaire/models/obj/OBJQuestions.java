package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public class OBJQuestions extends AIMModel {
    @JSONVariable
    String question_text;
    @JSONVariable
    boolean allow_multiple;
    @JSONVariable
    boolean has_image;
    @JSONVariable
    String images;
    @JSONVariable
    double max_score;
    @JSONVariable
    AIMArrayList<OBJAnswers> answers = AIMArrayList.Builder(OBJAnswers.class);

    public OBJAnswers getAnswersByID(long id) {
        for (Object obj : answers) {
            if (((OBJAnswers) obj).unique_id == id) {
                return (OBJAnswers) obj;
            }
        }
        return new OBJAnswers();
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public boolean isAllow_multiple() {
        return allow_multiple;
    }

    public void setAllow_multiple(boolean allow_multiple) {
        this.allow_multiple = allow_multiple;
    }

    public boolean isHas_image() {
        return has_image;
    }

    public void setHas_image(boolean has_image) {
        this.has_image = has_image;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public double getMax_score() {
        return max_score;
    }

    public void setMax_score(double max_score) {
        this.max_score = max_score;
    }

    public AIMArrayList<OBJAnswers> getAnswers() {
        return answers;
    }

    public void setAnswers(AIMArrayList<OBJAnswers> answers) {
        this.answers = answers;
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
