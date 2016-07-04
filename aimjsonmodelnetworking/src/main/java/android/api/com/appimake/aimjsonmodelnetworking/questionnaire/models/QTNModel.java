package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJQuestions;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJResults;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public class QTNModel extends AIMModel {
    @JSONVariable
    String questionair_title;
    @JSONVariable
    String questionair_image;
    @JSONVariable
    AIMArrayList<OBJResults> results = AIMArrayList.Builder(OBJResults.class);
    @JSONVariable
    AIMArrayList<OBJQuestions> questions = AIMArrayList.Builder(OBJQuestions.class);

    public OBJQuestions getQuestionByID(long id) {
        for (Object obj : questions) {
            if (((OBJQuestions) obj).unique_id == id) {
                return (OBJQuestions) obj;
            }
        }
        return new OBJQuestions();
    }

    public OBJResults getResultsByID(long id) {
        for (Object obj : results) {
            if (((OBJResults) obj).unique_id == id) {
                return (OBJResults) obj;
            }
        }
        return new OBJResults();
    }

    public String getQuestionair_title() {
        return questionair_title;
    }

    public void setQuestionair_title(String questionair_title) {
        this.questionair_title = questionair_title;
    }

    public String getQuestionair_image() {
        return questionair_image;
    }

    public void setQuestionair_image(String questionair_image) {
        this.questionair_image = questionair_image;
    }

    public AIMArrayList<OBJResults> getResults() {
        return results;
    }

    public void setResults(AIMArrayList<OBJResults> results) {
        this.results = results;
    }

    public AIMArrayList<OBJQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(AIMArrayList<OBJQuestions> questions) {
        this.questions = questions;
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
