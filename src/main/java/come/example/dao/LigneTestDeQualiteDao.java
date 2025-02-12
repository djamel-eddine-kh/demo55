package come.example.dao;

import java.util.List;

import come.example.model.LigneTestDeQualite;

public interface LigneTestDeQualiteDao {

	public void addLigne(LigneTestDeQualite d);
    public List<LigneTestDeQualite> getLigneList();
    public LigneTestDeQualite getLigneById(Integer id);
    public void deleteLigne(LigneTestDeQualite d);
    public void updateLigne(LigneTestDeQualite d);
}
