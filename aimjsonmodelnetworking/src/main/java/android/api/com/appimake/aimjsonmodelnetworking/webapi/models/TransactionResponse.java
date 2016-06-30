package android.api.com.appimake.aimjsonmodelnetworking.webapi.models;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.IQueryObject;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.NullableType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.Restriction;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.RestrictionType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;

/**
 * Created by ponlavitlarpeampaisarl on 3/30/15 AD.
 */
@IQueryObject(name = "webapi_transaction_response")
public class TransactionResponse extends AIMModel {

    @JSONVariable
    @IQueryObject(
            nullAble = NullableType.NOT_NULL,
            name = "tran_id",
            restriction = @Restriction(type = RestrictionType.OBJECT_REF),
            type = ColumnType.INTEGER
    )
    private Transaction ref_transaction;
    @JSONVariable
    @IQueryObject(
            nullAble = NullableType.NOT_NULL,
            name = "response_message",
            type = ColumnType.TEXT
    )
    private String responseMessage;
    @JSONVariable
    @IQueryObject(
            nullAble = NullableType.NOT_NULL,
            name = "code",
            type = ColumnType.TEXT
    )
    private String code;
    @JSONVariable
    @IQueryObject(
            nullAble = NullableType.NOT_NULL,
            name = "added_date",
            type = ColumnType.TEXT
    )
    private AIMDateTime addedDate;

    public Transaction getRef_transaction() {
        return ref_transaction;
    }

    public void setRef_transaction(Transaction ref_transaction) {
        this.ref_transaction = ref_transaction;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AIMDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(AIMDateTime addedDate) {
        this.addedDate = addedDate;
    }

    /**
     * This function will provide the identification for the
     * object that use for fetch from the server
     * <p/>
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
     * <p/>
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
