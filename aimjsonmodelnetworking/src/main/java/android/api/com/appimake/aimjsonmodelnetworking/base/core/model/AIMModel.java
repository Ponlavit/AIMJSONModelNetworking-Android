package android.api.com.appimake.aimjsonmodelnetworking.base.core.model;

import android.api.com.appimake.aimjsonmodelnetworking.AIMConfig;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.IQueryObject;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.JSONVariable;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.NullableType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.enumulation.UpdatePolicy;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.NotYetImplementedException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.UnsupportedClassException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.intf.IJSONEnable;
import android.api.com.appimake.aimjsonmodelnetworking.helper.PrimitiveHelper;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ponlavitlarpeampaisarl on 2/3/15 AD.
 */

public abstract class AIMModel implements Serializable, IJSONEnable {

    @JSONVariable
    @IQueryObject(
            name = "updatedate",
            type = ColumnType.TEXT,
            nullAble = NullableType.NOT_NULL
    )
    public AIMDateTime updatedate;

    @JSONVariable
    public long unique_id;
    @JSONVariable
    public boolean active_flag = true;

    @JSONVariable
    @IQueryObject(
            name = "_id",
            type = ColumnType.INTEGER,
            nullAble = NullableType.NOT_NULL
    )
    public long _id = -1;
    private boolean isObjectFetched;
    private boolean isObjectFetching;
    private boolean isObjectProcessing;


    public AIMModel() {
        this.isObjectFetched = false;
        this.isObjectFetching = false;
        this.isObjectProcessing = false;
    }

    public AIMModel(String json, UpdatePolicy policy) {
        this();
        this.updateFromJson(json, policy);
    }

    public AIMModel(Map<String, Object> json, UpdatePolicy policy) {
        this();
        this.updateFromInfo(json, policy);
    }

    public String forceStringNotNull(String st) {
        if (st == null || st.trim().trim().equalsIgnoreCase("") || st.trim().equalsIgnoreCase("null"))
            st = AIMConfig.DEFAULT_REPLACE_NULL;
        return st.trim();
    }

    public long getObjectId() {
        return this._id;
    }

    protected AIMModel self() {
        return this;
    }

    @Override
    public void updateTimeStamp() {
        this.updatedate = new AIMDateTime();
    }

    @Override
    public AIMDateTime getUpdateDate() {
        return this.updatedate;
    }

    public boolean getIsObjectProcessing() {
        return this.isObjectProcessing;
    }

    public boolean getIsObjectFetching() {
        return this.isObjectFetching;
    }

    public boolean getIsObjectFetched() {
        return this.isObjectFetched;
    }

    @Override
    public String toJSONLocalString() {
        return this.toJSONObject(true).toString().trim();
    }

    @Override
    public String toJSONString() {
        return this.toJSONObject(false).toString().trim();
    }

    /**
     * @param forLocal determine either generate json for local only
     * @return JSONObject that represent the properties in class that define JSONVariable annotation.
     */
    public JSONObject toJSONObject(boolean forLocal) {
        JSONObject root = new JSONObject();
        Field[] allField = null;
        Field[] allChildField = getClass().getDeclaredFields();
        if (getClass().getSuperclass() != null) {
            Field[] allSuperField = getClass().getSuperclass().getDeclaredFields();
            allField = Arrays.copyOf(allChildField, allChildField.length + allSuperField.length);
            System.arraycopy(allSuperField, 0, allField, allChildField.length, allSuperField.length);
        } else {
            allField = allChildField;
        }
        for (Field aField : allField) {
            if (aField.isAnnotationPresent(JSONVariable.class)) {
                try {
                    aField.setAccessible(true);
                    String name = aField.getName();
                    Object value = aField.get(this);
                    JSONVariable aFieldAnnotation = aField.getAnnotation(JSONVariable.class);
                    // Check the caller is for local
                    if (!forLocal && aFieldAnnotation.localOnly()) {
                        continue;
                    }
                    if (value != null) {
                        if (Boolean.TYPE.isAssignableFrom(aField.getType()))
                            root.put(name, ((Boolean) value).booleanValue() ? "1" : "0");
                        else if (aField.getType().isPrimitive() || String.class.isAssignableFrom(value.getClass())) {
                            root.put(name, value.toString().trim());
                        } else if (AIMDateTime.class.isAssignableFrom(value.getClass()))
                            root.put(name, ((AIMDateTime) value).toUnixTimeString());
                        else if (IJSONEnable.class.isAssignableFrom(value.getClass()))
                            root.put(name, ((IJSONEnable) value).toJSONObject(forLocal));
                        else if (aField.getType().equals(Object.class)) {
                            root.put(name, value.toString().trim());
                        } else if (aField.getType().equals(ArrayList.class)) {
                            JSONArray array = new JSONArray();
                            for (int i = 0; i < ((ArrayList) value).size(); i++) {
                                array.put(((ArrayList) value).get(i));
                            }
                            root.put(name, array);
                        } else if (aField.getType().equals(HashMap.class)) {
                            root.put(name, value.toString().trim());
                        } else throw new UnsupportedClassException();
                    }
                    aField.setAccessible(false);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return root;
    }

    private Field getField(Class classStruct, String fieldName) {
        try {
            return classStruct.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                return classStruct.getSuperclass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Use to replace the current data from input value Map to this object
     *
     * @param info   field value map
     * @param policy update policy
     */
    public void updateFromInfo(Map<String, Object> info, UpdatePolicy policy) {

        for (String key : info.keySet()) {
            try {
                Field aField = getField(getClass(), key);
                if (aField == null) continue;
                aField.setAccessible(true);
                Object value = aField.get(this);
                Object newValue = info.get(key);

                if (newValue != null && !newValue.equals("null"))
                    switch (policy) {
                        case ForceUpdate: {
                            if (aField.getType().equals(Boolean.TYPE))
                                aField.set(this, PrimitiveHelper.boolFromString(newValue.toString().trim()));
                            else if (aField.getType().isPrimitive()) {
                                String tmpValue = newValue.toString().trim();
                                if (tmpValue.equalsIgnoreCase("null")) tmpValue = "-1";
                                if (aField.getType() == Integer.TYPE)
                                    aField.set(this, Integer.parseInt(tmpValue));
                                else if (aField.getType() == Float.TYPE)
                                    aField.set(this, Float.parseFloat(tmpValue));
                                else if (aField.getType() == Double.TYPE)
                                    aField.set(this, Double.parseDouble(tmpValue));
                                else if (aField.getType() == Long.TYPE)
                                    aField.set(this, Long.parseLong(tmpValue));
                                else throw new UnsupportedClassException();
                            } else if (aField.getType().equals(HashMap.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(Object.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(ArrayList.class)) {
                                aField.set(this, newValue);
                            } else if (String.class.isAssignableFrom(aField.getType()))
                                aField.set(this, newValue.toString().trim());
                            else if (AIMDateTime.class.isAssignableFrom(aField.getType())) {
                                long uTime = Long.parseLong(newValue.toString().trim().replace(".", ""));
                                if (newValue.toString().trim().length() > 10)
                                    uTime = Long.parseLong(newValue.toString().trim().substring(0, 10));
                                aField.set(this, new AIMDateTime(uTime));
                            } else if (IJSONEnable.class.isAssignableFrom(aField.getType())) {
                                if (AIMArrayList.class.isAssignableFrom(aField.getType())) {
                                    if (newValue != null && !newValue.toString().trim().equalsIgnoreCase("null")) {
                                        ((AIMArrayList) value).updateFromArray((List) newValue, policy);
                                        aField.set(this, value);
                                    }
                                } else if (AIMModel.class.isAssignableFrom(aField.getType())) {
                                    if (value == null)
                                        try {
                                            value = aField.getType().newInstance();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    if (newValue != null && !newValue.toString().equalsIgnoreCase("null")) {
                                        ((AIMModel) value).updateFromInfo((Map<String, Object>) newValue, policy);
                                        aField.set(this, value);
                                    }
                                }
                            } else throw new UnsupportedClassException();
                        }
                        break;
                        case UseFromCacheIfAvailable: {
                            if (aField.getType().equals(Boolean.TYPE))
                                aField.set(this, PrimitiveHelper.boolFromString(newValue.toString().trim()));
                            else if (aField.getType().isPrimitive()) {
                                String tmpValue = newValue.toString().trim();
                                if (tmpValue.equalsIgnoreCase("null")) tmpValue = "-1";
                                if (aField.getType() == Integer.TYPE)
                                    aField.set(this, Integer.parseInt(tmpValue));
                                else if (aField.getType() == Float.TYPE)
                                    aField.set(this, Float.parseFloat(tmpValue));
                                else if (aField.getType() == Double.TYPE)
                                    aField.set(this, Double.parseDouble(tmpValue));
                                else if (aField.getType() == Long.TYPE)
                                    aField.set(this, Long.parseLong(tmpValue));
                                else throw new UnsupportedClassException();
                            } else if (aField.getType().equals(HashMap.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(Object.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(ArrayList.class)) {
                                aField.set(this, newValue);
                            } else if (String.class.isAssignableFrom(aField.getType()))
                                aField.set(this, newValue.toString().trim());
                            else if (AIMDateTime.class.isAssignableFrom(aField.getType())) {
                                long uTime = Long.parseLong(newValue.toString().trim().replace(".", ""));
                                if (newValue.toString().trim().length() > 10)
                                    uTime = Long.parseLong(newValue.toString().trim().substring(0, 10));
                                aField.set(this, new AIMDateTime(uTime));
                            } else if (IJSONEnable.class.isAssignableFrom(aField.getType())) {
                                if (AIMArrayList.class.isAssignableFrom(aField.getType())) {
                                    if (newValue != null && !newValue.toString().trim().equalsIgnoreCase("null")) {
                                        ((AIMArrayList) value).updateFromArray((List) newValue, policy);
                                        aField.set(this, value);
                                    }
                                } else if (AIMModel.class.isAssignableFrom(aField.getType())) {
                                    if (value == null)
                                        try {
                                            value = aField.getType().newInstance();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    ((AIMModel) value).updateFromInfo((Map<String, Object>) newValue, policy);
                                    aField.set(this, value);
                                }
                            } else throw new UnsupportedClassException();
                        }
                        break;
                        case UseNewest: {
                            if (aField.getType().equals(Boolean.TYPE))
                                aField.set(this, PrimitiveHelper.boolFromString(newValue.toString().trim()));
                            else if (aField.getType().isPrimitive()) {
                                String tmpValue = newValue.toString().trim();
                                if (tmpValue.equalsIgnoreCase("null")) tmpValue = "-1";
                                if (aField.getType() == Integer.TYPE)
                                    aField.set(this, Integer.parseInt(tmpValue));
                                else if (aField.getType() == Float.TYPE)
                                    aField.set(this, Float.parseFloat(tmpValue));
                                else if (aField.getType() == Double.TYPE)
                                    aField.set(this, Double.parseDouble(tmpValue));
                                else if (aField.getType() == Long.TYPE)
                                    aField.set(this, Long.parseLong(tmpValue));
                                else throw new UnsupportedClassException();
                            } else if (aField.getType().equals(HashMap.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(ArrayList.class)) {
                                aField.set(this, newValue);
                            } else if (aField.getType().equals(Object.class)) {
                                aField.set(this, newValue);
                            } else if (String.class.isAssignableFrom(aField.getType()))
                                aField.set(this, newValue.toString().trim());
                            else if (AIMDateTime.class.isAssignableFrom(aField.getType())) {
                                long uTime = Long.parseLong(newValue.toString().trim().replace(".", ""));
                                if (newValue.toString().trim().length() > 10)
                                    uTime = Long.parseLong(newValue.toString().trim().substring(0, 10));
                                aField.set(this, new AIMDateTime(uTime));
                            } else if (IJSONEnable.class.isAssignableFrom(aField.getType())) {
                                if (AIMArrayList.class.isAssignableFrom(aField.getType())) {
                                    if (newValue != null && !newValue.toString().trim().equalsIgnoreCase("null")) {
                                        ((AIMArrayList) value).updateFromArray((List) newValue, policy);
                                        aField.set(this, value);
                                    }
                                } else if (AIMModel.class.isAssignableFrom(aField.getType())) {
                                    if (value == null)
                                        try {
                                            value = aField.getType().newInstance();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    ((AIMModel) value).updateFromInfo((Map<String, Object>) newValue, policy);
                                    aField.set(this, value);
                                }
                            } else throw new UnsupportedClassException();
                        }
                        case MergeToNewest: {
                            throw new NotYetImplementedException();
                        }
                    }
                aField.setAccessible(false);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Update value from json string to this object with policy option.
     *
     * @param json   The string represent the class.
     * @param policy
     */
    public void updateFromJson(String json, UpdatePolicy policy) {
        JSONParser parser = JsonParserFactory.getInstance().newJsonParser();
        Map<String, Object> jsonData = parser.parseJson(json);
        if (jsonData.containsKey(AIMConfig.RESPONSE_ENTRIES))
            updateFromInfo((Map<String, Object>) jsonData.get(AIMConfig.RESPONSE_ENTRIES), policy);
        else
            updateFromInfo(jsonData, policy);
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
    public abstract String getObjectIdentificationRequest();

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
    public abstract String getKeyForObjectIdentificationRequest();

}
