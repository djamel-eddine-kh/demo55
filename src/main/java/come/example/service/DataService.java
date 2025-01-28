package come.example.service;

import java.util.List;


import come.example.model.Data;
public interface DataService {
	
	public void addData(Data d);
    public List<Data> dataList();
    public void deleteData(Data d);
    public void updateData(Data d);
    List<Object[]> fetchComplexDataWithEntityManager();
}
