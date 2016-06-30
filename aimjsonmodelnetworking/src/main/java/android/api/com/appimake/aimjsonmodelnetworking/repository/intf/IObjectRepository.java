package android.api.com.appimake.aimjsonmodelnetworking.repository.intf;


import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Table;

/**
 * Created by ponlavitlarpeampaisarl on 3/9/15 AD.
 */
public interface IObjectRepository {

    /**
     * Register the object for database table
     *
     * @param target the class of that object type that use in this table
     * @param table  the table structure
     */
    public void registerTable(Class target, Table table);

    /**
     * Clear the object for database table
     *
     * @param target the class of that object type that use in this table
     * @param table  the table structure
     */
    public void clearTable(Class target, Table table);

    /**
     * Make insert object to database
     *
     * @param object        the object to insert
     * @param tableToInsert the target table
     * @return the id of the object (_id field)
     */
    public long executeInsert(BaseModel object, Table tableToInsert);

    /**
     * Make update object to database
     *
     * @param object        the object to insert
     * @param tableToInsert the target table
     * @return success or not
     */
    public boolean executeUpdate(BaseModel object, Table tableToInsert);

    /**
     * Make delete object to database
     *
     * @param object        the object to delete
     * @param tableToDelete the target table
     * @return success or not
     */
    public boolean executeDelete(BaseModel object, Table tableToDelete);

    public boolean executeDeleteTable(Table tableToDelete);


    /**
     * Make select from specific table
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @return the list of object of specific class
     */
    public BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect);

    /**
     * Make select from specific table
     *
     * @param classStruct    the class that will use for create new object after select
     * @param tableToSelect  the table structure for the class
     * @param columnToSelect the column to select
     * @return the list of object of specific class
     */
    public BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect, String columnToSelect);

    /**
     * Make select from specific table and id
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @param id            the object id (_id)
     * @return the object of specific id, null if not found
     */
    public BaseModel executeSelect(Class classStruct, Table tableToSelect, long id);

    /**
     * Make select from specific table for limited and offset
     *
     * @param classStruct   the class that will use for create new object after select
     * @param tableToSelect the table structure for the class
     * @param offset        the offset of selection
     * @param limit         the limit (count maximum row)
     * @return the list of object of specific parameter
     */
    public BaseArrayList<BaseModel> executeSelect(Class classStruct, Table tableToSelect, int offset, int limit);

    /**
     * Get the table for the class that have registered
     *
     * @param target the target table of that class
     * @return the Table of that class
     */
    public Table getRegisteredTableOfClass(Class target);

    /**
     * Get all table for registered class
     *
     * @return the list of table for all registered class
     */
    public java.util.HashMap<Class, Table> getAllRegisteredTable();

    /**
     * Create table for all registered class
     */
    public void initializeTable();
}

