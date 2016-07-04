package android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public class OBJResults extends AIMModel {
    @JSONVariable
    long result_from;
    @JSONVariable
    long result_to;
    @JSONVariable
    String result_header;
    @JSONVariable
    String result_description;

    public long getResult_from() {
        return result_from;
    }

    public void setResult_from(long result_from) {
        this.result_from = result_from;
    }

    public long getResult_to() {
        return result_to;
    }

    public void setResult_to(long result_to) {
        this.result_to = result_to;
    }

    public String getResult_header() {
        return result_header;
    }

    public void setResult_header(String result_header) {
        this.result_header = result_header;
    }

    public String getResult_description() {
        return result_description;
    }

    public void setResult_description(String result_description) {
        this.result_description = result_description;
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
