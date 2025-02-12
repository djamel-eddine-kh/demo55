package come.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import come.example.dao.LigneTestDeQualiteDao;
import come.example.model.LigneTestDeQualite;

@Service
public class LigneTestDeQualiteService {

	  private static final long serialVersionUID = 1L;
	    private final LigneTestDeQualiteDao ligneTestDao;
	    
	    @Autowired
	    public LigneTestDeQualiteService(LigneTestDeQualiteDao ligneTestDao) {
	        this.ligneTestDao = ligneTestDao;
	    }
	    
	   
	    public void addLigne(LigneTestDeQualite ligne) {
	        ligneTestDao.addLigne(ligne);
	    }
	    
	    
	    public void updateLigneTest(LigneTestDeQualite ligne) {
	        ligneTestDao.updateLigne(ligne);
	    }
	    
	    
	    public void deleteLigneTest(LigneTestDeQualite ligne) {
	        ligneTestDao.deleteLigne(ligne);
	    }
	    
	    
	    public LigneTestDeQualite getLigneTestById(int id) {
	        return ligneTestDao.getLigneById(id);
	    }
	    
	    
	    public List<LigneTestDeQualite> ligneTestList() {
	        return ligneTestDao.getLigneList();
	    }
	}
