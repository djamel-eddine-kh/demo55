package come.example.service;
import come.example.model.Data;
import come.example.dao.DataDao;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataServiceImpl implements DataService ,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

	private DataDao dataDao;
	@Autowired
     public DataServiceImpl(DataDao datadao) {
    	 this.dataDao=datadao;
     }
	
	public void setDataDao(DataDao dataDAO) {
	    this.dataDao = dataDAO;
	    if (this.dataDao != null) {
	        logger.info("DataDao successfully injected");
	    } else {
	        logger.error("DataDao injection failed");
	    }
	}

	@Override
	public void addData(Data d) {
		
		dataDao.addData(d);
	}

	@Override
	public List<Data> dataList() {
		
		return dataDao.dataList();
	}

	@Override
	public void deleteData(Data d) {
		dataDao.deleteData(d);
		
	}

	@Override
	public void updateData(Data d) {
		dataDao.updateData(d);
		
	}

	@Override
	public List<Object[]> fetchComplexDataWithEntityManager() {
		// TODO Auto-generated method stub
		return dataDao.fetchComplexDataWithEntityManager();
	}

}
