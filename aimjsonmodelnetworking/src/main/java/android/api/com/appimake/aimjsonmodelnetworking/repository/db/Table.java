package android.api.com.appimake.aimjsonmodelnetworking.repository.db;


import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.IQueryObject;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.NullableType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.RestrictionType;

import junit.framework.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ponlavitlarpeampaisarl on 3/9/15 AD.
 */
public class Table {
    public String tableName;
    public ArrayList<Column> columns;

    public static Table Builder(Class target) {
        return generateTableFromClass(target);
    }

    private static Table generateTableFromClass(Class target) {
        Table table = new Table();
        String tableName = null;
        Annotation[] classAnnotationList = target.getDeclaredAnnotations();
        for (int i = 0; i < classAnnotationList.length; i++) {
            if (classAnnotationList[i].annotationType().equals(IQueryObject.class)) {
                tableName = ((IQueryObject) classAnnotationList[i]).name();
            }
        }
        Assert.assertNotNull(tableName);
        table.tableName = tableName;
        table.columns = new ArrayList<>();

        // Get field name and type
        Field[] allField = null;
        Field[] allChildField = target.getDeclaredFields();
        if (target.getSuperclass() != null) {
            Field[] allSuperField = target.getSuperclass().getDeclaredFields();

            allField = Arrays.copyOf(allChildField, allChildField.length + allSuperField.length);
            System.arraycopy(allSuperField, 0, allField, allChildField.length, allSuperField.length);
        } else {
            allField = allChildField;
        }

        for (int i = 0; i < allField.length; i++) {
            Field aField = allField[i];
            if (aField.isAnnotationPresent(IQueryObject.class)) {
                Column column = new Column();
                IQueryObject annotation = aField.getAnnotation(IQueryObject.class);
                column.mappingName = aField.getName();
                column.name = annotation.name();
                column.type = annotation.type();
                column.length = annotation.restriction().type() == RestrictionType.LENGTH ? annotation.restriction().value() : 0;
                column.isPrimaryKey = annotation.name().equalsIgnoreCase("_id");
                column.isReferenceObject = annotation.restriction().type() == RestrictionType.OBJECT_REF;
                column.isNullable = annotation.nullAble() == NullableType.NULLABLE;
                table.columns.add(column);
            }
        }
        return table;
    }
}
