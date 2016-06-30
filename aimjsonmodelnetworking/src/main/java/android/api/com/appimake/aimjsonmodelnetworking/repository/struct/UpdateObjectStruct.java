package android.api.com.appimake.aimjsonmodelnetworking.repository.struct;


import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.AIMModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Table;
import android.api.com.appimake.aimjsonmodelnetworking.repository.intf.IObjectRepository;

/**
 * Created by ponlavitlarpeampaisarl on 3/10/15 AD.
 */
public class UpdateObjectStruct {
    IObjectRepository repository;
    Class classStruct;

    public UpdateObjectStruct(Class classStruct, IObjectRepository repository) {
        this.classStruct = classStruct;
        this.repository = repository;
    }

    public boolean asUpdateOrReplaceItem(AIMModel target) {
        return repository.executeUpdate(target, Table.Builder(target.getClass()));
    }
}
