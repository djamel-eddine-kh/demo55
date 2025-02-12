package come.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import come.example.dao.TestQualityDao;
import come.example.model.TestQuality;

@Service
public class TestQualityService {
	 private static final long serialVersionUID = 1L;
	    private final TestQualityDao testQualityDao;
	    
	    @Autowired
	    public TestQualityService(TestQualityDao testQualityDao) {
	        this.testQualityDao = testQualityDao;
	    }
	    
	    
	    public void addTest(TestQuality testQuality) {
	        testQualityDao.addTest(testQuality);
	    }
	    
	   
	    public void updateTest(TestQuality testQuality) {
	        testQualityDao.updateTest(testQuality);
	    }
	    
	
	    public void deleteTestQuality(TestQuality testQuality) {
	        testQualityDao.deleteTest(testQuality);
	    }
	    
	  
	    public TestQuality getTestQualityById(int id) {
	        return testQualityDao.getTestById(id);
	    }
	    
	   
	    public List<TestQuality> testQualityList() {
	        return testQualityDao.getTestList();
	    }
	}