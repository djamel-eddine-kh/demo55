package come.example.dao;

import java.util.List;

import come.example.model.Data;
import come.example.model.Reference;

public interface ReferenceDao {

	public void add(Reference d);
    public List<Reference> getReferences();
    public Reference getReference(Integer id);
    public void delete(Reference d);
    public void updateData(Reference d);
}
