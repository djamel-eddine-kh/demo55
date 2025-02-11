package come.example.dao.impl;

import java.util.List;

import come.example.dao.ReferenceDao;
import come.example.model.Reference;

public class ReferenceDaoImpl implements ReferenceDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void add(Reference reference) {
        entityManager.persist(reference);
    }

    @Override
    public List<Reference> getReferences() {
        return entityManager.createQuery("SELECT r FROM Reference r", Reference.class).getResultList();
    }

    @Transactional
    @Override
    public void delete(Reference reference) {
        Reference ref = entityManager.find(Reference.class, reference.getId());
        if (ref != null) {
            entityManager.remove(ref);
        }
    }

    @Transactional
    @Override
    public void update(Reference reference) {
        entityManager.merge(reference);
    }

    @Override
    public Reference getReference(Integer id) {
        return entityManager.find(Reference.class, id);
    }
}
