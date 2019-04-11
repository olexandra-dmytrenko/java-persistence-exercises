package ua.procamp;

import ua.procamp.model.Account;
import ua.procamp.model.Card;
import ua.procamp.util.TestDataGenerator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//install p6spy for ? to be replaced for values
public class EnityManagerExample {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SingleAccountEntityPostgres");
        EntityManager entityManager = entityManagerFactory.createEntityManager();//session start
        entityManager.getTransaction().begin();
        //потокозахищена фабрика, по суты працюємо в одному потоці
            Account account = TestDataGenerator.generateAccount();
        try {
            System.out.println(account);
            entityManager.persist(account);
            System.out.println(account);

            Card card = new Card();
            card.setHolder(account);
            card.setName("mono");
            card.setAmount(100.0);
            entityManager.persist(card);

            Card card2 = new Card();
            card2.setHolder(account);
            card2.setName("privat");
            card2.setAmount(323.0);
            entityManager.persist(card2);


/*

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

*/
            entityManager.getTransaction().commit();
            System.out.println(card);

//            EntityManagerUtil  emut  = new EntityManagerUtil(entityManagerFactory);
//            Object o = emut.performReturningWithinTx(em -> {
//                em.find(Account.class, account.getId()).getCards().stream().forEach(cards -> System.out.println(cards.getName()));
//                System.out.println("Im' here");
//                entityManagerFactory.close();
//                return "";
//            });

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
