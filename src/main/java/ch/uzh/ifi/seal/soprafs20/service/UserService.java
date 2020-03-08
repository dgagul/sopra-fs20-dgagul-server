package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        checkIfUserExists(newUser);

        newUser.setStatus(UserStatus.OFFLINE);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String now = formatter.format(date);
        newUser.setCreationDate(now);

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

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The username provided is not unique!");
        }
    }

    private User findUserByCredentials(User userToBeFound) {
        User userByUsername = userRepository.findByUsername(userToBeFound.getUsername());

        if (userByUsername != null && userByUsername.getPassword().equals(userToBeFound.getPassword())) {
            return userByUsername;
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The login failed because credentials are incorrect!");
        }
    }

    public User login(User userToBeLoggedIn) {
        User userFound = findUserByCredentials(userToBeLoggedIn);

        if(userFound.getStatus() == UserStatus.ONLINE) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Already logged in!");
        }

        userFound.setToken(UUID.randomUUID().toString());
        userFound.setStatus(UserStatus.ONLINE);

        userRepository.save(userFound);
        return userFound;
    }


    public User getUserById(Long id){
        User userById = userRepository.getOne(id);
        if (userById != null){
            return userById;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public User logout(User userToLogOut){
        User userByToken = userRepository.findByToken(userToLogOut.getToken());

        userByToken.setStatus(UserStatus.OFFLINE);
        userByToken.setToken(null);

        userRepository.save(userByToken);
        return userByToken;
    }

    public User edit(User userToEdit) {
        User userById = userRepository.getOne(userToEdit.getId());

        if (userById != null){
            // Todo: regular expression
            if(userToEdit.getUsername() != null){
                userById.setUsername(userToEdit.getUsername());
            }
            if(userToEdit.getBirthday() != null){
                String oldFormat = userToEdit.getBirthday();

                //change from 2020-03-07 to 04.03.2020
                StringBuilder newFromat = new StringBuilder();
                newFromat.append(oldFormat.substring(8,10) + "." + oldFormat.substring(5,7) + "." + oldFormat.substring(0,4));

                userById.setBirthday(newFromat.toString());
            }
            userRepository.save(userById);
            return userById;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}
