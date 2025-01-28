package come.example.dao;


import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import come.example.model.Data;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Transactional
@Repository
public class DataDaoImpl implements DataDao {

    private static final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addData(Data d) {
        try {
            entityManager.persist(d);
            logger.info("Data saved successfully, Data Details=" + d);
        } catch (Exception e) {
            logger.error("JPA exception: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Data> dataList() {
        long startTime = System.currentTimeMillis(); // Start time
        try {
            Query query = entityManager.createQuery("FROM Data", Data.class);
            List<Data> resultList = query.getResultList();
            long endTime = System.currentTimeMillis(); // End time
            logger.info("JPA Fetched data list successfully. Time taken: {} ms", (endTime - startTime));
            return resultList;
        } catch (Exception e) {
            logger.error("JPA exception: " + e.getMessage(), e);
        }
        return null;
    }
@Override
    public List<Object[]> fetchComplexDataWithEntityManager() {
        long startTime = System.currentTimeMillis();
        
        Query query = entityManager.createQuery(
            "SELECT d.name, COUNT(d.id) AS total_count, AVG(d.age) AS average_age " +
            "FROM Data d " +
            "WHERE d.age > 20 " +
            "GROUP BY d.name " +
            "HAVING COUNT(d.id) > 1 " +
            "ORDER BY average_age DESC"
        );
        
        List<Object[]> results = query.getResultList();
        
        long endTime = System.currentTimeMillis();
        System.out.println("2ND Query EntityManager fetched data successfully. Time taken: " + (endTime - startTime) + " ms");
        
        return results;
    }

    @Override
    public void deleteData(Data d) {
        try {
            Data managedData = entityManager.contains(d) ? d : entityManager.merge(d);
            entityManager.remove(managedData);
            logger.info("Data deleted successfully, Data Details=" + d);
        } catch (Exception e) {
            logger.error("JPA exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateData(Data d) {
        try {
            entityManager.merge(d);
            logger.info("Data updated successfully, Data Details=" + d);
        } catch (Exception e) {
            logger.error("JPA exception: " + e.getMessage(), e);
        }
    }
}
