package android.api.com.appimake.aimjsonmodelnetworking.base.core.model;

import android.api.com.appimake.aimjsonmodelnetworking.base.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.enumulation.UpdatePolicy;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.NotYetImplementedException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.UnsupportedClassException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.intf.IJSONEnable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.intf.IRestServiceObjectDelegate;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ponlavitlarpeampaisarl on 2/3/15 AD.
 */
public class BaseArrayList<T> extends ArrayList implements IJSONEnable, IRestServiceObjectDelegate {
    private Class<T> genericType;
    private boolean isListFetching;
    private BaseDateTime updatedate;
    private String target;

    public BaseArrayList() {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor is not available please use the builder class instead
     */
    protected BaseArrayList(Class<T> c) {
        this.genericType = c;
    }

    /**
     * Object builder method
     *
     * @return New BaseArrayList object with BaseModel generic type.
     */
    public static BaseArrayList Builder(Class asClass) {
        return new BaseArrayList(asClass);
    }

    @Override
    public void updateTimeStamp() {
        this.updatedate = new BaseDateTime();
    }

    @Override
    public BaseDateTime getUpdateDate() {
        if (this.updatedate == null)
            this.updatedate = new BaseDateTime(0);
        return this.updatedate;
    }

    public boolean getIsListFetching() {
        return this.isListFetching;
    }

    public BaseArrayList<?> self() {
        return this;
    }

    public Class<T> getGenericType() {
        return genericType;
    }

    @Override
    public String toJSONString() {
        return toJSONObject(false).toString().trim().trim();
    }

    @Override
    public String toJSONLocalString() {
        return toJSONObject(true).toString().trim();
    }

    @Override
    public JSONArray toJSONObject(boolean forLocal) {
        JSONArray ary = new JSONArray();
        for (int i = 0; i < size(); i++) {
            Object objItem = get(i);
            if (objItem instanceof BaseModel) {
                BaseModel item = (BaseModel) objItem;
                ary.put(item.toJSONObject(forLocal));
            } else
                throw new UnsupportedClassException();
        }
        return ary;
    }

    public void updateFromJson(String json, UpdatePolicy policy) {

        JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
        Map jsonData = parser.parseJson(json);
        if (jsonData.containsKey(AIMConfig.RESPONSE_ENTRIES)) {
            if (jsonData.get(AIMConfig.RESPONSE_ENTRIES) != null && !jsonData.get(AIMConfig.RESPONSE_ENTRIES).equals("null"))
                updateFromArray((List) jsonData.get(AIMConfig.RESPONSE_ENTRIES), policy);
            else clear();
        } else {
            if (!jsonData.isEmpty())
                updateFromArray((List) jsonData, policy);
            else clear();
        }
    }

    public void updateFromArray(List list, UpdatePolicy policy) {
        switch (policy) {
            case ForceUpdate:
                clear();
                break;
            case UseFromCacheIfAvailable:
                clear();
                break;
            case UseNewest:
                // Check if JSON from server have a new activity or deleted old activity
                if (list.size() <= 0)
                    this.clear();
                else if (this.size() >= 0)
                    for (int i = 0; i < this.size(); i++) {
                        boolean isNoHave = true;
                        for (int j = 0; j < list.size(); j++) {
                            if (Long.parseLong(((HashMap) list.get(j)).get("unique_id").toString().trim()) == ((BaseModel) this.get(i)).unique_id) {
                                isNoHave = false;
                            }
                        }
                        if (isNoHave)
                            this.remove(i);
                    }
                break;
            case MergeToNewest:
                break;
            default:
                break;
        }

        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            if (UpdatePolicy.ForceUpdate == policy || UpdatePolicy.UseFromCacheIfAvailable == policy) {
                try {
                    BaseModel base = (BaseModel) getGenericType().newInstance();
                    if (item instanceof Map) {
                        base.updateFromInfo((Map<String, Object>) item, policy);
                        add(base);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (UpdatePolicy.MergeToNewest == policy)
                throw new NotYetImplementedException();
            else if (UpdatePolicy.UseNewest == policy) {
                try {
                    BaseModel base = (BaseModel) getGenericType().newInstance();
                    if (item instanceof Map) {
                        // loop all old data
                        boolean isHave = false;
                        for (int thisCount = 0; thisCount < this.size(); thisCount++) {
                            // Check if 'id' in this index(old data) is equal to fetching list(new data)
                            // if true, Check timestamp
                            if (((HashMap) list.get(i)).get("unique_id") != null) {
                                if (Long.parseLong(((HashMap) list.get(i)).get("unique_id").toString().trim()) == ((BaseModel) this.get(thisCount)).unique_id) {
                                    // Check if timestamp in list of fetching is updated(new)
                                    // if true, Update data
                                    // if false, do nothing
                                    if (((BaseModel) this.get(thisCount)).updatedate.getTime() < Long.parseLong(((HashMap) list.get(i)).get("updatedate").toString().trim().replace(".", ""))) {
                                        base.updateFromInfo((Map<String, Object>) item, UpdatePolicy.ForceUpdate);
                                        set(thisCount, base);
                                    }
                                    isHave = true;
                                }
                            }
                        }
                        if (!isHave) {
                            //Fetching list is a new data (Add to list)
                            base.updateFromInfo((Map<String, Object>) item, UpdatePolicy.ForceUpdate);
                            add(base);
                        }

                    } else throw new UnsupportedOperationException();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public boolean willMapDataFromString(String stringToMap) {
        return true;
    }

    @Override
    public void didMapDataFromStringSuccess(IJSONEnable object) {

    }

    @Override
    public void didMapDataFromStringFailedWithError(String error) {

    }

    @Override
    public void didFetchFailedWithError(String error, int errorCode) {

    }

    @Override
    public void didPushSuccess() {

    }

    @Override
    public void didPushFailedWithError(String error, int errorCode) {

    }

}
