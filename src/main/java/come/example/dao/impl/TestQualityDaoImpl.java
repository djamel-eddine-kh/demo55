package come.example.dao.impl;

import come.example.dao.TestQualityDao;
import come.example.model.TestQuality;
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
public class TestQualityDaoImpl implements TestQualityDao {

    private static final Logger logger = LoggerFactory.getLogger(TestQualityDaoImpl.class);
    private SessionFactory sessionFactory;
    
    @Autowired
    public TestQualityDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public void addTest(TestQuality testQuality) {
        try {
            getSession().save(testQuality);
            logger.info("TestQuality saved successfully: {}", testQuality);
        } catch (HibernateException e) {
            logger.error("Error in addTestQuality: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void updateTest(TestQuality testQuality) {
        try {
            getSession().update(testQuality);
            logger.info("TestQuality updated successfully: {}", testQuality);
        } catch (HibernateException e) {
            logger.error("Error in updateTestQuality: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteTest(TestQuality testQuality) {
        try {
            getSession().delete(testQuality);
            logger.info("TestQuality deleted successfully: {}", testQuality);
        } catch (HibernateException e) {
            logger.error("Error in deleteTestQuality: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public TestQuality getTestById(Integer id) {
        return getSession().get(TestQuality.class, id);
    }
    
    @Override
    public List<TestQuality> getTestList() {
        return getSession().createQuery("FROM TestQuality", TestQuality.class).list();
    }
}
