package come.example.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import come.example.dao.LigneTestDeQualiteDao;
import come.example.model.LigneTestDeQualite;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class LigneTestDeQualiteImpl implements LigneTestDeQualiteDao{

	 private static final Logger logger = LoggerFactory.getLogger(LigneTestDeQualiteImpl.class);
	    private SessionFactory sessionFactory;
	    
	    @Autowired
	    public LigneTestDeQualiteImpl(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	    
	    private Session getSession() {
	        return sessionFactory.getCurrentSession();
	    }
	    
	    @Override
	    public void addLigne(LigneTestDeQualite ligne) {
	        try {
	            getSession().save(ligne);
	            logger.info("LigneTestDeQualite saved successfully: {}", ligne);
	        } catch (HibernateException e) {
	            logger.error("Error in addLigneTest: {}", e.getMessage(), e);
	        }
	    }
	    
	    @Override
	    public void updateLigne(LigneTestDeQualite ligne) {
	        try {
	            getSession().update(ligne);
	            logger.info("LigneTestDeQualite updated successfully: {}", ligne);
	        } catch (HibernateException e) {
	            logger.error("Error in updateLigneTest: {}", e.getMessage(), e);
	        }
	    }
	    
	    @Override
	    public void deleteLigne(LigneTestDeQualite ligne) {
	        try {
	            getSession().delete(ligne);
	            logger.info("LigneTestDeQualite deleted successfully: {}", ligne);
	        } catch (HibernateException e) {
	            logger.error("Error in deleteLigneTest: {}", e.getMessage(), e);
	        }
	    }
	    
	    @Override
	    public LigneTestDeQualite getLigneById(Integer id) {
	        return getSession().get(LigneTestDeQualite.class, id);
	    }
	    
	    @Override
	    public List<LigneTestDeQualite> getLigneList() {
	        return getSession().createQuery("FROM LigneTestDeQualite", LigneTestDeQualite.class).list();
	    }
}
