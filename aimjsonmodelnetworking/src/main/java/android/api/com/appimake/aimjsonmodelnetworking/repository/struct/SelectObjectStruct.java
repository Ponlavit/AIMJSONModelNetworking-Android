package android.api.com.appimake.aimjsonmodelnetworking.repository.struct;

import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseArrayList;
import android.api.com.appimake.aimjsonmodelnetworking.base.core.model.BaseModel;
import android.api.com.appimake.aimjsonmodelnetworking.repository.db.Table;
import android.api.com.appimake.aimjsonmodelnetworking.repository.intf.IObjectRepository;

import java.util.ArrayList;

/**
 * Created by ponlavitlarpeampaisarl on 3/6/15 AD.
 */
public class SelectObjectStruct {
    IObjectRepository repository;
    Class target;

    public SelectObjectStruct(Class target, IObjectRepository repository) {
        this.repository = repository;
        this.target = target;
    }

    public SelectObjectStruct(IObjectRepository repository) {
        this.repository = repository;
    }

    public BaseModel getById(long id) {
        return repository.executeSelect(target, Table.Builder(target), id);
    }

    public BaseArrayList<BaseModel> getAll() {
        return repository.executeSelect(target, Table.Builder(target));
    }

    public BaseArrayList<BaseModel> getAll(String columnToSelect) {
        return repository.executeSelect(target, Table.Builder(target), columnToSelect);
    }

    public BaseArrayList<BaseModel> get(int limit, int offset) {
        return repository.executeSelect(target, Table.Builder(target), offset, limit);
    }

    public ArrayList<BaseModel> get(int limit) {
        return repository.executeSelect(target, Table.Builder(target), 0, limit);
    }
}
