package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;
    private Transaction transaction;
    private final String createUsersQuery = "CREATE TABLE IF NOT EXISTS User (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NOT NULL,\n" +
            "  `lastName` VARCHAR(45) NOT NULL,\n" +
            "  `age` TINYINT(3) NOT NULL,\n" +
            "  PRIMARY KEY (`id`))";

    public UserDaoHibernateImpl() {
        sessionFactory = new Util().getSessionFactory();

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(createUsersQuery).executeUpdate();
            transaction.commit();
            System.out.println("Была создана таблица базы данных \"users\"");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            System.out.println("Таблица базы данных \"users\" была удалена");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("Пользователь с именем:" + name + "; с фамилией:" + lastName + "; с возрастом:" + age + " был сохранен");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(session.get(User.class, id));
            session.getTransaction().commit();
            System.out.println("Пользователь с id: \" + id + \" был удален");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    @Override
    public List<User> getAllUsers() {
        List <User> list = null;
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            list = session.createQuery("from User").getResultList();
            transaction.commit();
            System.out.println("Все пользователи таблицы \"users\" были получены");
        } catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
        System.out.println(list);
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
            System.out.println("Таблица \"users\" была очищена");
        }catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }
}
