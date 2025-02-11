package come.example.dao;

import java.util.List;
import java.util.Map;

import come.example.model.Data;
public interface DataDao {
	
	public void addData(Data d);
    public List<Data> dataList();
    public void deleteData(Data d);
    public void updateData(Data d);
    List<Object[]> fetchComplexDataWithEntityManager();
    public List<Data> loadLazyData(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters);
	public Data getDataById(int id);
	public int countLazyData(Map<String, Object> filters);
    }
