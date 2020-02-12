package hugomaalves.github.io.jsonpatchdemo;

import com.github.javafaker.Faker;
import hugomaalves.github.io.jsonpatchdemo.model.User;
import hugomaalves.github.io.jsonpatchdemo.exceptions.DuplicatedResourceException;
import hugomaalves.github.io.jsonpatchdemo.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final Faker faker;

  private final List<User> usersList;

  public UserService() {
    this.faker = Faker.instance();
    this.usersList = new ArrayList<>();
  }

  @PostConstruct
  public void init() {
    usersList.add(new User("bruce.wayne@gotham.com", "Bruce Wayne", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
    usersList.add(new User("joker@gotham.com", "Dunno the name", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
    usersList.add(new User("gordon@gotham.com", "Gordon", faker.phoneNumber().phoneNumber(), faker.address().fullAddress()));
  }

  public User getUserByEmail(String email) {
    return usersList.stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException());
  }

  public void createUser(User newUser) {
    User user = usersList.stream()
        .filter(u -> u.getEmail().equals(newUser.getEmail()))
        .findAny()
        .orElse(null);
    if (user != null) {
      throw new DuplicatedResourceException();
    }
    usersList.add(newUser);
  }
}
