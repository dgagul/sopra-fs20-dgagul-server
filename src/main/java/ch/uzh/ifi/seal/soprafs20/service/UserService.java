package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /***
     * finds all users in the userRepository and returns a List object containing all users
     * @return List<User>
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /***
     * checks if the user already exists, if not it crates a new user with UserStatus.OFFLINE and puts it in the userRepository
     * @param newUser
     * @return User newUser
     */
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString()); // Todo: find out token
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setCreationDate();

        //System.out.println(newUser.getCreationDate());

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     * @param userToBeCreated
     * @throws SopraServiceException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByPassword = userRepository.findByPassword(userToBeCreated.getPassword());

        // Todo: only username not password!!
        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        /*
        if (userByUsername != null && userByPassword != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "username and the password", "are"));
        }
         */
        if (userByUsername != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "username", "is"));
        }
        /*
        else if (userByPassword != null) {
            throw new SopraServiceException(String.format(baseErrorMessage, "password", "is"));
        }
         */
    }

    private User findUserByCredentials(User userToBeFound) {
        User userByUsername = userRepository.findByUsername(userToBeFound.getUsername());

        if (userByUsername != null && userByUsername.getPassword().equals(userToBeFound.getPassword())) {
            return userByUsername;
        }
        else {
            // Todo: return status code 401 UNAUTHORIZED
            String baseErrorMessage = "The login failed because credentials are incorrect!";
            throw new SopraServiceException(baseErrorMessage);
        }
    }

    public User updateUser(User userToBeLoggedIn) {
        User user = findUserByCredentials(userToBeLoggedIn);

        // Todo: if (user.getStatus() == UserStatus.ONLINE) => return 204 NO_CONTENT
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);

        return user;
    }

    /*
    public UserGetDTO getUserById(String id){
        User userWithId = userRepository.findOne();
    }
    */
}
