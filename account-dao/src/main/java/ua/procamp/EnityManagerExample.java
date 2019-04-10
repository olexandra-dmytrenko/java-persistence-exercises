package ua.procamp;

import ua.procamp.model.Account;
import ua.procamp.util.TestDataGenerator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EnityManagerExample {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SingleAccountEntityH2");
        EntityManager entityManager = entityManagerFactory.createEntityManager();//session start
        entityManager.getTransaction().begin();
        //потокозахищена фабрика, по суты працюємо в одному потоці
            Account account = TestDataGenerator.generateAccount();
        try {
            System.out.println(account);
            entityManager.persist(account);
            System.out.println(account);

            Account foundAcc = entityManager.find(Account.class, account.getId());
            System.out.println(account);


            entityManager.detach(account);
            //come back to having detached account
            entityManager.merge(account);

            entityManager.remove(account);
            Account foundAcc1 = entityManager.find(Account.class, account.getId());
            System.out.println("Doesn't exist" + foundAcc1);

            List<Account> accounts = entityManager.createQuery("select a from Account a where a.email=:email", Account.class).
                    setParameter("email", account.getEmail())
                    .getResultList();
            System.out.println("ACCCC = " + accounts);

            entityManager.getTransaction().commit();


        } catch (Exception e) {
            System.out.println(account);
            entityManager.getTransaction().rollback();
            System.out.println(account);
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();

    }
}
