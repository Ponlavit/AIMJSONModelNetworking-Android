package android.api.com.appimake.aimjsonmodelnetworking.repository.db;


import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;

/**
 * Created by ponlavitlarpeampaisarl on 3/9/15 AD.
 */
public class Column {
    public ColumnType type;
    public String name;
    public String mappingName;
    public int length;
    public boolean isPrimaryKey;
    public boolean isNullable;
    public boolean isReferenceObject = false;
}
