package come.example.dao.impl;

import come.example.dao.ReferenceDao;

import come.example.model.Reference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Transactional
@Repository
public class ReferenceDaoImpl implements ReferenceDao {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceDaoImpl.class);
    private SessionFactory sessionFactory;
    
    @Autowired
    public ReferenceDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public void addReference(Reference reference) {
        try {
            getSession().save(reference);
            logger.info("Reference saved successfully: {}", reference);
        } catch (HibernateException e) {
            logger.error("Error in addReference: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void updateReference(Reference reference) {
        try {
            getSession().update(reference);
            logger.info("Reference updated successfully: {}", reference);
        } catch (HibernateException e) {
            logger.error("Error in updateReference: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteReference(Reference reference) {
        try {
            getSession().delete(reference);
            logger.info("Reference deleted successfully: {}", reference);
        } catch (HibernateException e) {
            logger.error("Error in deleteReference: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public Reference getReferenceById(Integer id) {
        return getSession().get(Reference.class, id);
    }
    
    @Override
    public List<Reference> getReferenceList() {
        return getSession().createQuery("FROM Reference", Reference.class).list();
    }
}
