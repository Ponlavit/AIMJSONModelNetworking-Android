package android.api.com.appimake.aimjsonmodelnetworking.repository.struct;


import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Table;
import android.api.com.appimake.aimjsonmodelnetworking.repository.intf.IObjectRepository;

/**
 * Created by ponlavitlarpeampaisarl on 3/6/15 AD.
 */
public class InsertObjectStruct {
    IObjectRepository repository;
    Class classStruct;

    public InsertObjectStruct(Class classStruct, IObjectRepository repository) {
        this.classStruct = classStruct;
        this.repository = repository;
    }

    public long asNewItem(AIMModel target) {
        return repository.executeInsert(target, Table.Builder(classStruct));
    }

}
