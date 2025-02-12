package come.example.dao;

import java.util.List;

import come.example.model.Reference;
import come.example.model.TestQuality;

public interface TestQualityDao {
	
	public void addTest(TestQuality d);
    public List<TestQuality> getTestList();
    public TestQuality getTestById(Integer id);
    public void deleteTest(TestQuality d);
    public void updateTest(TestQuality d);

}
