package ru.volobuev.security.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.volobuev.security.models.User;

import java.util.List;

@Transactional
@Component
public class UserDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

//    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

//    @Transactional(readOnly = true)
    public User findById(long id) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id = :id", User.class);
        User user =  query.setParameter("id", id)
                .getSingleResult();
        System.out.println(user);
        return user;
    }

    public void save(User user) {
        entityManager.persist(user);
    }
    public void update(long id, User user) {
        User userToBeUpdate = findById(id);
        if(userToBeUpdate != null) {
            userToBeUpdate.setFirstName(user.getFirstName());
            userToBeUpdate.setLastName(user.getLastName());
            userToBeUpdate.setEmail(user.getEmail());
            userToBeUpdate.setAge(user.getAge());
            entityManager.merge(userToBeUpdate);
        }
    }
    public void delete(long id) {
        entityManager.remove(findById(id));
    }
}
