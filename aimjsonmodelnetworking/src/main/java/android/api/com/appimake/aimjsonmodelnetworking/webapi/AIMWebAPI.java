package android.api.com.appimake.aimjsonmodelnetworking.webapi;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.converter.UnicodeConverter;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.AIMObjectFactory;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.intf.IWebAPINotification;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Add;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.Transaction;
import android.api.com.appimake.aimjsonmodelnetworking.webapi.models.TransactionResponse;
import android.net.Uri;

import java.net.HttpURLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by ponlavitlarpeampaisarl on 3/30/15 AD.
 */
public class AIMWebAPI {
    public static final String KEY_PARAM_JSON_HTTP_REQUEST = "json";
    private static AIMWebAPI shareInstance;
    public boolean isForceUploadDone = false;
    private boolean ableToRefresh = true;

    private AIMWebAPI() {

    }

    /**
     * get AIMWebAPI Singleton class
     *
     * @return the share AIMWebAPI object
     */
    public synchronized static AIMWebAPI getInstance() {
        if (shareInstance == null) {
            shareInstance = new AIMWebAPI();
        }
        return shareInstance;
    }


    public void registerWebAPIDatabase() {
        AIMObjectFactory.register(Transaction.class);
        AIMObjectFactory.register(TransactionResponse.class);
    }

    public void createNewTransaction(AIMModel objectToSent, String tag, String url, String target, String module, String token) {
        String json = objectToSent.toJSONString();
        Transaction tran = new Transaction();
        tran.setAddedDate(new AIMDateTime());
        tran.setFailedCount(0);
        tran.setTag(tag);
        tran.setJsonMessage(json);
        tran.setTarget(target);
        tran.setModule(module);
        tran.setSenderToken(token);
        tran.setSuccess(false);
        tran.setTargetURL(url);
        tran.updatedate = new AIMDateTime();
        AIMObjectFactory.insert(Transaction.class).asNewItem(tran);
    }

    public void startAllTransaction() {
        AIMArrayList<?> transactionList = AIMObjectFactory.select(Transaction.class).getAll();
        for (Object obj : transactionList) {
            Transaction transaction = (Transaction) obj;
            if (transaction.isSuccess()) continue;
            preUpload(transaction);
        }
    }

    public synchronized void forceUpload(Transaction transaction, IWebAPINotification callback) {

        if (preUpload(transaction)) {
            callback.webAPISuccess("", "Force send done", "", callback.getClass());
        } else {
            callback.webAPISuccess("", "Force send failed", "", callback.getClass());
        }
    }

    private synchronized boolean preUpload(Transaction transaction) {
        String json = UnicodeConverter.unicodeEscapedString(transaction.getJsonMessage());
        Add upload = new Add();
        upload.updatedate = new AIMDateTime();
        upload.setToken(transaction.getSenderToken());
        upload.setData(json);
        upload.setModule(transaction.getModule());
        upload.setTarget(transaction.getTarget());

        return postUpload(transaction, upload, transaction.getTargetURL());
    }

    private synchronized boolean postUpload(Transaction transaction, Add upload, String url) {
        ableToRefresh = false;
        boolean isFailed = false;

        int responseCode = 0;
        String responseString = "";
        String responseStringUnEscape = "";

        HttpURLConnection conn = AIMWebAPIConnection.createHttpConnection("WEB API", url);

        Uri.Builder builder = new Uri.Builder().appendQueryParameter(AIMWebAPI.KEY_PARAM_JSON_HTTP_REQUEST, UnicodeConverter.unicodeEscapedString(upload.toJSONString()));
        String query = builder.build().getEncodedQuery();

        AIMWebAPIConnection.postQueryParameterString(conn, query);

        responseCode = AIMWebAPIConnection.getConnectionResponseCode("WEB API", conn);
        responseString = AIMWebAPIConnection.getConnectionResponseString("WEB API", conn);
        responseStringUnEscape = AIMWebAPIConnection.unEscapeResponseStringWithTrim(responseString);

        if (responseStringUnEscape != null && responseCode != 200) {
            isFailed = true;
        }

        transaction.setSuccess(!isFailed);
        transaction.setFailedCount(transaction.getFailedCount() + (!isFailed ? 0 : 1));

        if (responseStringUnEscape != null) {
            TransactionResponse resp = new TransactionResponse();
            resp.setCode(responseCode + "");
            resp.setAddedDate(new AIMDateTime());
            resp.setRef_transaction(transaction);
            resp.setResponseMessage(responseString);
            resp.updatedate = new AIMDateTime();
            AIMObjectFactory.insert(TransactionResponse.class).asNewItem(resp);
        }
        AIMObjectFactory.update(Transaction.class).asUpdateOrReplaceItem(transaction);

        ableToRefresh = true;
        return !isFailed;
    }

    public synchronized void startService() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    if (ableToRefresh)
                        startAllTransaction();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
