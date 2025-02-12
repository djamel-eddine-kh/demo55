package come.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import come.example.dao.ReferenceDao;
import come.example.model.Reference;

@Service
public class ReferenceService implements ReferenceDao {
	  private static final long serialVersionUID = 1L;
	    private final ReferenceDao referenceDao;
	    
	    @Autowired
	    public ReferenceService(ReferenceDao referenceDao) {
	        this.referenceDao = referenceDao;
	    }
	    
	    @Override
	    public void addReference(Reference reference) {
	        System.out.println("Saving reference: " + reference);
	        referenceDao.addReference(reference);
	    }
	    
	    @Override
	    public void updateReference(Reference reference) {
	        referenceDao.updateReference(reference);
	    }
	    
	    @Override
	    public void deleteReference(Reference reference) {
	        referenceDao.deleteReference(reference);
	    }
	    
	    @Override
	    public Reference getReferenceById(Integer id) {
	        return referenceDao.getReferenceById(id);
	    }
	    
	    @Override
	    public List<Reference> getReferenceList() {
	        return referenceDao.getReferenceList();
	    }

}
