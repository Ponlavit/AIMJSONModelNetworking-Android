package android.api.com.appimake.aimjsonmodelnetworking.repository.injector;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.annotation.ColumnType;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.NotYetImplementedException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.exception.UnsupportedClassException;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseDateTime;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.AIMObjectFactory;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Column;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Table;
import android.api.com.appimake.aimjsonmodelnetworking.repository.intf.IObjectRepository;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ponlavitlarpeampaisarl on 3/9/15 AD.
 */
public class SQLiteRepository extends SQLiteOpenHelper implements IObjectRepository {

    private static final String DATABASE_NAME = "default.db";
    private static final int DATABASE_VERSION = 1;
    String SPACE = " ";
    String NOT_NULL = "NOT NULL";
    String PK = "PRIMARY KEY AUTOINCREMENT";
    String OC_STRING = "\"";
    String INSERT_INTO = "INSERT INTO ";
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private HashMap<Class, Table> allTable;
    private SQLiteDatabase database;
    private File mDatabaseFile;

    private boolean forceToString;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public SQLiteRepository(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mDatabaseFile = context.getDatabasePath(name);
        this.allTable = new HashMap<>();
    }

    protected synchronized String getCreateTableQuery(Table table) {

        String NEXT = "";

        StringBuilder createTableString = new StringBuilder();
        createTableString.append(CREATE_TABLE + table.tableName + " ( ");
        for (Column column : table.columns) {

            createTableString.append(NEXT);
            NEXT = ",";

            createTableString.append(column.name + SPACE);
            if (column.type != ColumnType.NONE)
                createTableString.append(column.type + SPACE);
            if (column.length != 0)
                createTableString.append("(" + column.length + ")" + SPACE);
            if (column.isPrimaryKey)
                createTableString.append(PK + SPACE);
            if (!column.isNullable)
                createTableString.append(NOT_NULL + SPACE);
        }
        createTableString.append(");");
        return createTableString.toString().trim();
    }

    @Override
    public synchronized void registerTable(Class target, Table table) {
        this.allTable.put(target, table);
    }

    @Override
    public synchronized void clearTable(Class target, Table table) {
        this.allTable.remove(target);
    }

    /**
     * Make insert object to database
     *
     * @param object        the object to insert
     * @param tableToInsert the target table
     * @return the id of the object (_id field), return -1 if failed
     */
    @Override
    public synchronized long executeInsert(BaseModel object, Table tableToInsert) {
        // Check for reference object
        for (Column column : tableToInsert.columns) {
            if (column.isReferenceObject) {
                try {
                    Field field = getField(object.getClass(), column.mappingName);
                    if (field == null) continue;
                    field.setAccessible(true);
                    BaseModel baseModel = (BaseModel) field.get(object);
                    boolean needInsertAsNew = false;
                    // id is not -1 mean not new item
                    if (baseModel._id != -1) {
                        // Check if this item is exist in database;
                        if (AIMObjectFactory.select(baseModel.getClass()).getById(baseModel._id) == null) {
                            // Not already existing : insert as new and set new id
                            needInsertAsNew = true;
                        }
                    } else {
                        needInsertAsNew = true;
                    }
                    if (needInsertAsNew)
                        baseModel._id = AIMObjectFactory.insert(baseModel.getClass()).asNewItem(baseModel);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        ContentValues contentValues = contentValueForObject(object, tableToInsert);
        if (!this.database.isOpen())
            openDatabase();
        long id = this.database.insert(tableToInsert.tableName, null, contentValues);
        object._id = id;
        closeDatabase();
        return object._id;
    }

    public synchronized ContentValues contentValueForObject(BaseModel object, Table tableToInsert) {
        ContentValues contentValues = new ContentValues();
        for (Column col : tableToInsert.columns) {
            if (col.isPrimaryKey) continue;
            Field aField = null;
            try {
                aField = getField(object.getClass(), col.mappingName);
                if (aField == null) continue;
                aField.setAccessible(true);
                Object value = aField.get(object);

                if (Boolean.TYPE.isAssignableFrom(aField.getType()))
                    contentValues.put(col.name, ((Boolean) value).booleanValue() ? 1 : 0);
                else if (aField.getType().isPrimitive() || String.class.isAssignableFrom(value.getClass()))
                    contentValues.put(col.name, value.toString().trim());
                else if (BaseDateTime.class.isAssignableFrom(value.getClass()))
                    contentValues.put(col.name, ((BaseDateTime) value)
                            .toUnixTimeString());
                else if (BaseModel.class.isAssignableFrom(value.getClass()))
                    contentValues.put(col.name, ((BaseModel) value)._id);
                else throw new UnsupportedClassException();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }


    /**
     * Make update object to database
     *
     * @param object        the object to insert
     * @param tableToInsert the target table
     * @return success or not
     */
    @Override
    public synchronized boolean executeUpdate(BaseModel object, Table tableToInsert) {
        openDatabase();
        ContentValues contentValues = contentValueForObject(object, tableToInsert);
        if (contentValues == null) return false;
        this.database.update(tableToInsert.tableName, contentValues, "_id = ?", new String[]{"" + object.getObjectId()});
        closeDatabase();

        return true;
    }

    /**
     * Make delete object to database
     *
     * @param object        the object to delete
     * @param tableToDelete the target table
     * @return success or not
     */
    @Override
    public synchronized boolean executeDelete(BaseModel object, Table tableToDelete) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(object.getObjectId())};
        if (!this.database.isOpen())
            openDatabase();

        int deleteTableResult = this.database.delete(tableToDelete.tableName, whereClause, whereArgs);

        if (this.database.isOpen())
            closeDatabase();
        return deleteTableResult > 0;
    }

    @Override
    public synchronized boolean executeDeleteTable(Table tableToDelete) {
        openDatabase();
        int deleteTableResult = this.database.delete(tableToDelete.tableName, null, null);
        closeDatabase();
        return deleteTableResult > 0;
    }

    /**
     * Make select from specific table
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @return the list of object of specific class
     */
    @Override
    public synchronized BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect) {
        openReadOnlyDatabase();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + tableToSelect.tableName, null);
        BaseArrayList<BaseModel> list = prepareListFromSelection(classStruct, tableToSelect, cursor);
        closeDatabase();
        return list;
    }

    /**
     * Make select from specific table
     *
     * @param classStruct    the class that will use for create new object after select
     * @param tableToSelect  the table structure for the class
     * @param columnToSelect the column to select
     * @return the list of object of specific class
     */

    public synchronized BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect, String columnToSelect) {
        openReadOnlyDatabase();
        this.forceToString = true;
        Cursor cursor = this.database.rawQuery("SELECT " + columnToSelect + " FROM " + tableToSelect.tableName, null);
        BaseArrayList<BaseModel> list = prepareListFromSelection(classStruct, tableToSelect, cursor);
        closeDatabase();
        this.forceToString = false;
        return list;
    }


    private synchronized Field getField(Class classStruct, String fieldName) {
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

    private synchronized BaseArrayList<BaseModel> prepareListFromSelection(Class classStruct, Table tableToSelect, Cursor cursor) {
        BaseArrayList<BaseModel> list = BaseArrayList.Builder(classStruct);

        if (this.database.isOpen()) {
            if (cursor == null) return list;
            if (!this.database.isOpen()) openDatabase();
            if (cursor.getCount() == 0) return list;
            cursor.moveToFirst();
            do {
                BaseModel obj = null;
                try {
                    Constructor<BaseModel> ct = classStruct.getConstructor();
                    obj = ct.newInstance();
                    for (Column col : tableToSelect.columns) {
                        Field field = getField(classStruct, col.mappingName);
                        if (field == null) continue;
                        field.setAccessible(true);
                        Type fieldType = field.getType();
                        int colIndex = cursor.getColumnIndex(col.name);
                        switch (cursor.getType(colIndex)) {
                            case Cursor.FIELD_TYPE_INTEGER:
                                // Ref_Object, Boolean,long, and int
                                if (fieldType.equals(Long.TYPE) || fieldType.equals(Integer.TYPE)) {
                                    field.set(obj, forceToString ? cursor.getInt(colIndex) + "" : cursor.getInt(colIndex));
                                } else if (fieldType.equals(Boolean.TYPE)) {
                                    field.set(obj, forceToString ? cursor.getInt(colIndex) + "" : cursor.getInt(colIndex) == 1);
                                } else if (BaseModel.class.isAssignableFrom(field.getType())) {
                                    long refId = cursor.getLong(colIndex);
                                    BaseModel item = AIMObjectFactory.select(field.getType()).getById(refId);
                                    field.set(obj, forceToString ? item + "" : item);
                                }
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                // double
                                field.set(obj, forceToString ? cursor.getDouble(colIndex) + "" : cursor.getDouble(colIndex));
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                // BaseDateTime and String
                                if (fieldType.equals(String.class)) {
                                    field.set(obj, forceToString ? cursor.getString(colIndex) + "" : cursor.getString(colIndex));
                                } else if (fieldType.equals(BaseDateTime.class)) {
                                    field.set(obj, forceToString ? (new BaseDateTime(cursor.getString(colIndex))).getTime() + "" : new BaseDateTime(cursor.getString(colIndex)));
                                }
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                throw new NotYetImplementedException();
                            case Cursor.FIELD_TYPE_NULL:
                                break;
                        }

                        field.setAccessible(false);
                    }
                    if (obj != null)
                        list.add(obj);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * Make select from specific table and id
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @param id            the object id (_id)
     * @return the object of specific id, null if not found
     */
    @Override
    public synchronized BaseModel executeSelect(Class classStruct, Table tableToSelect, long id) {
        if (!this.database.isOpen())
            openReadOnlyDatabase();

        Cursor cursor = this.database.rawQuery("SELECT * FROM " + tableToSelect.tableName + " WHERE _id=" + id, null);
        ArrayList<BaseModel> list = prepareListFromSelection(classStruct, tableToSelect, cursor);

        if (this.database.isOpen())
            closeDatabase();

        return list.size() == 1 ? list.get(0) : null;
    }

    /**
     * Make select from specific table for limited and offset
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @param offset        the offset of selection
     * @param limit         the limit (count maximum row)
     * @return the list of object of specific parameter
     */
    @Override
    public synchronized BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect, int offset, int limit) {
        openReadOnlyDatabase();
        Cursor cursor = this.database.rawQuery("SELECT * FROM " + tableToSelect.tableName + " LIMIT " + limit + " OFFSET " + offset, null);
        BaseArrayList<BaseModel> list = prepareListFromSelection(classStruct, tableToSelect, cursor);
        closeDatabase();

        if (this.database.isOpen())
            closeDatabase();

        return list;
    }

    /**
     * Get the table for the class that have registered
     *
     * @param target the target table of that class
     * @return the Table of that class
     */
    @Override
    public synchronized Table getRegisteredTableOfClass(Class target) {
        return this.allTable.get(target);
    }

    /**
     * Get all table for registered class
     *
     * @return the list of table for all registered class
     */
    @Override
    public synchronized HashMap<Class, Table> getAllRegisteredTable() {
        return this.allTable;
    }

    public synchronized void openDatabase() {
        this.database = getWritableDatabase();
    }

    public synchronized void openReadOnlyDatabase() {
        this.database = getReadableDatabase();
    }

    public void closeDatabase() {
        this.database.close();
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param database The database.
     */
    @Override
    public synchronized void onCreate(SQLiteDatabase database) {
        for (Table table : allTable.values()) {
            database.execSQL(getCreateTableQuery(table));
        }
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, search tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you search new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : allTable.values()) {
            db.execSQL("DROP TABLE IF EXISTS " + table.tableName);
        }
    }

    @Override
    public synchronized void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public synchronized void cleanUpDatabase(SQLiteDatabase database) {
        if (database != null) {
            for (Table table : allTable.values()) {
                database.execSQL("DROP TABLE IF EXISTS " + table.tableName);
            }
        }
    }

    @Override
    public synchronized void initializeTable() {
        openDatabase();
        for (Table table : allTable.values()) {
            database.execSQL(getCreateTableQuery(table));
        }
        closeDatabase();
    }
}
