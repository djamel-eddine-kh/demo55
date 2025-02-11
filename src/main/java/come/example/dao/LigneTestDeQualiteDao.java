package come.example.dao;

import java.util.List;

public interface LigneTestDeQualiteDao {

	public void add(LigneTestDeQualiteDao d);
    public List<LigneTestDeQualiteDao> getLignes();
    public LigneTestDeQualiteDao getLigne(Integer id);
    public void delete(LigneTestDeQualiteDao d);
    public void updateData(LigneTestDeQualiteDao d);
}
