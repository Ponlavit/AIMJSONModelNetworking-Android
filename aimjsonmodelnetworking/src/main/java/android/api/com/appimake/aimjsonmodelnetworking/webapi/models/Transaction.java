package android.api.com.appimake.aimjsonmodelnetworking.webapi.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.IQueryObject;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.NullableType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by ponlavitlarpeampaisarl on 3/30/15 AD.
 */
@IQueryObject(name = "webapi_transaction")
public class Transaction extends AIMModel {

    @JSONVariable
    @IQueryObject(
            name = "sender_token",
            type = ColumnType.TEXT
    )
    String senderToken;
    @JSONVariable
    @IQueryObject(
            name = "failed_count",
            nullAble = NullableType.NOT_NULL,
            type = ColumnType.INTEGER
    )
    private int failedCount;
    @JSONVariable
    @IQueryObject(
            name = "target_url",
            nullAble = NullableType.NOT_NULL,
            type = ColumnType.TEXT
    )
    private String targetURL;
    @JSONVariable
    @IQueryObject(
            name = "added_date",
            nullAble = NullableType.NOT_NULL,
            type = ColumnType.TEXT
    )
    private AIMDateTime addedDate;
    @JSONVariable
    @IQueryObject(
            name = "is_success",
            nullAble = NullableType.NOT_NULL,
            type = ColumnType.INTEGER
    )
    private boolean isSuccess;
    @JSONVariable
    @IQueryObject(
            name = "json_message",
            nullAble = NullableType.NOT_NULL,
            type = ColumnType.TEXT
    )
    private String jsonMessage;
    @JSONVariable
    @IQueryObject(
            name = "tag",
            type = ColumnType.TEXT
    )
    private String tag;
    @JSONVariable
    @IQueryObject(
            name = "target",
            type = ColumnType.TEXT
    )
    private String target;
    @JSONVariable
    @IQueryObject(
            name = "module",
            type = ColumnType.TEXT
    )
    private String module;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public AIMDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(AIMDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * This function will provide the identification for the
     * object that use for fetch from the server
     *
     * e.g.
     * The mapping service point to www.example.com
     * getKeyForObjectIdentificationRequest = id
     * getObjectIdentificationRequest = 3
     * the final url is
     * www.example.com?id=3
     * for each object
     *
     * @return The identification of this object, null if there is no fetch service support
     */
    @Override
    public String getObjectIdentificationRequest() {
        return null;
    }

    /**
     * This function will provide the key for identification
     * that will send to server to fetch object
     *
     * e.g.
     * The mapping service point to www.example.com
     * getKeyForObjectIdentificationRequest = id
     * getObjectIdentificationRequest = 3
     * the final url is
     * www.example.com?id=3
     * for each object
     *
     * @return The key for identification, null if there is no fetch service support
     */
    @Override
    public String getKeyForObjectIdentificationRequest() {
        return null;
    }
}
