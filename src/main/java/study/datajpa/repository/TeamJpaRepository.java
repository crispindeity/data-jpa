package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import study.datajpa.entity.Team;

@Repository
@Transactional(readOnly = true)
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Team save(Team team) {
        entityManager.persist(team);
        return team;
    }

    @Transactional
    public void delete(Team team) {
        entityManager.remove(team);
    }

    public List<Team> findAll() {
        return entityManager.createQuery(
                "select t from Team t", Team.class
        ).getResultList();
    }

    public Optional<Team> findById(long id) {
        return Optional.ofNullable(entityManager.find(Team.class, id));
    }

    public long count() {
        return entityManager.createQuery(
                "select count(t) from Team t", Long.class
        ).getSingleResult();
    }
}
