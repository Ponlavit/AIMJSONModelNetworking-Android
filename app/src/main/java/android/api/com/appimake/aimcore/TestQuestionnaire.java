package android.api.com.appimake.aimcore;

import android.api.com.appimake.aimjsonmodelnetworking.base.AIMFragmentActivity;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.enumulation.UpdatePolicy;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.inf.AIMQTNDataInterface;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.QTNModel;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.models.obj.OBJResults;
import android.api.com.appimake.aimjsonmodelnetworking.questionnaire.modules.AIMQuestionnaire;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.AIMWebAPISearchObject;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.AIMIWebAPINotification;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Nattapongr on 7/4/16 AD.
 */
public class TestQuestionnaire extends AIMFragmentActivity implements AIMIWebAPINotification, AIMQTNDataInterface {

    QTNModel listQTN = new QTNModel();

    LinearLayout llShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_qtn);

        llShow = (LinearLayout) findViewById(R.id.test_qtm__ll_show);

        loadingDialog("Get question list");
        AIMWebAPISearchObject.getInstance().search("https://dl.dropboxusercontent.com/u/7094537/HealthMeMaster/sorethroat.json",
                new AIMDateTime(0),
                "all",
                "list",
                "QTN",
                0,
                0,
                "token",
                this,
                this.getClass());

    }

    private void showView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment newFragment = new AIMQuestionnaire() {
                    @Override
                    public QTNModel setQuestion() {
                        return listQTN;
                    }

                    @Override
                    public AIMQTNDataInterface setCallBack() {
                        return TestQuestionnaire.this;
                    }
                };
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.test_qtm__ll_show, newFragment).commit();


            }
        });
    }

    @Override
    public void webAPISuccess(String code, String message, String jsonData, Class Class) {
        listQTN.updateFromJson(jsonData, UpdatePolicy.ForceUpdate);
        hideLoadingDialog();
        showView();
    }

    @Override
    public void webAPIProgress(float sizeUploaded, float fileSize) {

    }

    @Override
    public void webAPIFailed(String code, String message, Class Class) {
        hideLoadingDialog();
        showToast(message + "");
    }

    @Override
    public void result(double totalScore, OBJResults results) {
        Log.d("QTN-Result", "Score = " + totalScore + "\n" + results.toJSONString());
        finish();
    }
}
