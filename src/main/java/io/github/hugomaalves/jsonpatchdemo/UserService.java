package io.github.hugomaalves.jsonpatchdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.github.hugomaalves.jsonpatchdemo.exceptions.DuplicatedResourceException;
import io.github.hugomaalves.jsonpatchdemo.exceptions.ResourceNotFoundException;
import io.github.hugomaalves.jsonpatchdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Faker faker;

    private final Map<String, User> usersList;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.faker = Faker.instance();
        this.usersList = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        usersList.put("bruce.wayne@gotham.com", new User("bruce.wayne@gotham.com", "Bruce Wayne", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
        usersList.put("joker@gotham.com", new User("joker@gotham.com", "Dunno the name", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
        usersList.put("gordon@gotham.com", new User("gordon@gotham.com", "Gordon", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
    }

    public User getUserByEmail(String email) {
        return Optional.ofNullable(usersList.get(email))
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    public void createUser(User newUser) {
        User user = usersList.get(newUser.getEmail());
        if (user != null) {
            throw new DuplicatedResourceException();
        }
        usersList.put(newUser.getEmail(), newUser);
    }

    public void patchUser(JsonPatch patchDocument, String email) {
        //Gets the original user from the database
        User originalUser = getUserByEmail(email);
        logger.debug("original user  {}", originalUser);

        //Converts the original user to a JsonStructure
        JsonStructure target = objectMapper.convertValue(originalUser, JsonStructure.class);
        //Applies the patch to the original user
        JsonValue patchedUser = patchDocument.apply(target);

        //Converts the JsonValue to a User instance
        User modifiedUser = objectMapper.convertValue(patchedUser, User.class);
        logger.debug("modified user {}", modifiedUser);

        //Saves the modified user in the database
        usersList.put(email, modifiedUser);
    }
}
