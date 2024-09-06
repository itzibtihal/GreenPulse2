package repositories;

import entities.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    User findUserById(UUID id);
    List<User> findAllUsers();
    void saveUser(User user);
    void deleteUser(UUID id);
    List<User> findUsersAboveCarbonLimit(double limit);

}
