package come.example.dao.impl;

import org.slf4j.Logger;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import come.example.dao.DataDao;
import come.example.model.Data;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
@Transactional
@Repository
public class DataDaoImpl implements DataDao{

	private static final Logger logger = LoggerFactory.getLogger(DataDao.class);

	private SessionFactory sf;
    
    @Autowired
    public DataDaoImpl(SessionFactory sf) {
    	this.sf=sf;
    }
    
	@Override
	public void addData(Data d) {
		try {
		Session session=this.sf.getCurrentSession();
		session.save(d);
		 logger.info("Car saved successfully, Car Details="+d);
		}
		catch(HibernateException e) {
			logger.error("Hibernate exception: "+e.getMessage());
		}
		
	}
	public List<Object[]> fetchComplexDataWithSession() {
        long startTime = System.currentTimeMillis();
        
        Session session = sf.getCurrentSession();
        Query query = session.createQuery(
            "SELECT d.name, COUNT(d.id) AS total_count, AVG(d.age) AS average_age " +
            "FROM Data d " +
            "WHERE d.age > 20 " +
            "GROUP BY d.name " +
            "HAVING COUNT(d.id) > 1 " +
            "ORDER BY average_age DESC",
            Object[].class
        );
        
        List<Object[]> results = query.getResultList();
        
        long endTime = System.currentTimeMillis();
        System.out.println("2ND Query Session fetched data successfully. Time taken: " + (endTime - startTime) + " ms");
        
        return results;
    }

	@Override
	public List<Data> dataList() {
	    long startTime = System.currentTimeMillis(); // Start time
	    try {
	        Session session = this.sf.getCurrentSession();
	        List<Data> data = session.createQuery("from Data").list();
	        long endTime = System.currentTimeMillis(); // End time
	        logger.info("Session factory Fetched data list successfully. Time taken: {} ms", (endTime - startTime));
	        return data;
	    } catch (HibernateException e) {
	        logger.error("Hibernate exception: " + e.getMessage(), e);
	    }
	    return null;
	}


	@Override
	public void deleteData(Data d) {
		try {
			Session session=this.sf.getCurrentSession();
			session.delete(d);
			 logger.info("Car deleted successfully, Car Details="+d);
			}
			catch(HibernateException e) {
				logger.error("Hibernate exception: "+e.getMessage());
			}
	}

	@Override
	public void updateData(Data d) {
		try {
			Session session=this.sf.getCurrentSession();
			session.update(d);
			 logger.info("Car updated successfully, Car Details="+d);
			}
			catch(HibernateException e) {
				logger.error("Hibernate exception: "+e.getMessage());
			}		
	}
	@Override
	public List<Data> loadLazyData(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters) {
	    Session session = sf.getCurrentSession();
	    String queryString = "FROM Data";
	    
	    // Apply filters (if any)
	    if (!filters.isEmpty()) {
	        queryString += " WHERE ";
	        queryString += filters.entrySet().stream()
	                .map(entry -> entry.getKey() + " LIKE :" + entry.getKey())
	                .collect(Collectors.joining(" AND "));
	    }

	    // Apply sorting (if any)
	    if (sortField != null) {
	        queryString += " ORDER BY " + sortField + (ascending ? " ASC" : " DESC");
	    }

	    Query query = session.createQuery(queryString, Data.class);

	    // Set filter parameters
	    filters.forEach((key, value) -> query.setParameter(key, "%" + value + "%"));

	    // Apply pagination
	    query.setFirstResult(first);
	    query.setMaxResults(pageSize);

	    return query.getResultList();
	}

	@Override
	public int countLazyData(Map<String, Object> filters) {
	    Session session = sf.getCurrentSession();
	    String queryString = "SELECT COUNT(*) FROM Data";

	    // Apply filters (if any)
	    if (!filters.isEmpty()) {
	        queryString += " WHERE ";
	        queryString += filters.entrySet().stream()
	                .map(entry -> entry.getKey() + " LIKE :" + entry.getKey())
	                .collect(Collectors.joining(" AND "));
	    }

	    // Create a typed query for Long
	    TypedQuery<Long> query = session.createQuery(queryString, Long.class);

	    // Set filter parameters
	    filters.forEach((key, value) -> query.setParameter(key, "%" + value + "%"));

	    // Execute the query and handle the result
	    try {
	        Long count = query.getSingleResult(); // Use getSingleResult() for JPA/Hibernate 6+
	        return count != null ? count.intValue() : 0;
	    } catch (NoResultException e) {
	        // Handle case where no results are found
	        return 0;
	    } catch (Exception e) {
	        // Log the error and return 0
	        logger.error("Error executing count query: {}", e.getMessage());
	        return 0;
	    }
	}
	@Override
	public Data getDataById(int id) {
	    Session session = sf.getCurrentSession();
	    return session.get(Data.class, id);
	}

	@Override
	public List<Object[]> fetchComplexDataWithEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}