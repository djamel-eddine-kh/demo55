package come.example.dao;

import java.util.List;

import come.example.model.Data;
import come.example.model.Reference;

public interface ReferenceDao {

	public void addReference(Reference d);
    public List<Reference> getReferenceList();
    public Reference getReferenceById(Integer id);
    public void deleteReference(Reference d);
    public void updateReference(Reference d);
}
 