package ua.procamp.dao;

import ua.procamp.exception.AccountDaoException;
import ua.procamp.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AccountDaoImpl implements AccountDao {
    private EntityManagerFactory emf;

    public AccountDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Account account) {
        // emf.createEntityManager().persist(account);
        hibernateActionNoResult(em -> em.persist(account));
    }

    @Override
    public Account findById(Long id) {
        return hibernateActionWithResult(em -> em.find(Account.class, id));
//        return emf.createEntityManager().find(Account.class, id);
    }

    @Override
    public Account findByEmail(String email) {
        return hibernateActionWithResult(em -> em.createQuery("select a from Account a where a.email=:email", Account.class)
                .setParameter("email", email).getSingleResult());
    }

    @Override
    public List<Account> findAll() {
        return hibernateActionWithResult(em -> em.createQuery("select a from Account a", Account.class).getResultList());
    }

    @Override
    public void update(Account account) {

        hibernateActionNoResult(em -> {
            Account accMerged = em.merge(account);
            em.persist(accMerged);
        });
    }

    @Override
    public void remove(Account account) {
        hibernateActionNoResult(em -> {
            Account accountMerged = em.merge(account);
            em.remove(accountMerged);
        });
//        emf.createEntityManager().remove(account);
    }

    private void performWithinPersistenceContext(Consumer<EntityManager> entityManagerConsumer) {
        performReturningWithinPersistenceContext(entityManager -> {
            entityManagerConsumer.accept(entityManager);
            return null;
        });
    }

    private void hibernateActionNoResult(Consumer<EntityManager> implement) {
        hibernateActionWithResult(em -> {
            implement.accept(em);
            return null;
        });
    }

    private <T> T performReturningWithinPersistenceContext(Function<EntityManager, T> entityManagerFunction) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        T result;
        try {
            result = entityManagerFunction.apply(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new AccountDaoException("Error performing dao operation. Transaction is rolled back!", e);
        } finally {
            entityManager.close();
        }
        return result;
    }


    private <R> R hibernateActionWithResult(Function<EntityManager, R> implement) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        R result;
        try {
            result = implement.apply(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new AccountDaoException("could not implement transaction", e);
        } finally {
            entityManager.close();
        }
        return result;
    }
}

