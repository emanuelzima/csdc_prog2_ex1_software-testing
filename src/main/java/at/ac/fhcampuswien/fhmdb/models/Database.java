package at.ac.fhcampuswien.fhmdb.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class Database {
    private static Database instance;
    private final SessionFactory sessionFactory;

    private Database() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    // Generic CRUD operations
    public <T> void save(T entity) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public <T> void update(T entity) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public <T> void delete(T entity) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public <T> Optional<T> findById(Class<T> entityClass, Long id) {
        try (Session session = getSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        try (Session session = getSession()) {
            Query<T> query = session.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
            return query.list();
        }
    }

    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        try (Session session = getSession()) {
            Query<T> query = session.createQuery(
                "FROM " + entityClass.getSimpleName() + " WHERE " + fieldName + " = :value",
                entityClass
            );
            query.setParameter("value", value);
            return query.list();
        }
    }
} 