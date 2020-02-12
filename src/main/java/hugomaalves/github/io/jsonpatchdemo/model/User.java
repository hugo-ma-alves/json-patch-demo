package hugomaalves.github.io.jsonpatchdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(name = "User")
public class User {

  @NotNull
  @Schema(example = "john@email.com", description = "The email of the user")
  private String email;

  @NotNull
  @Schema(example = "Bruce Wayne", description = "The full name of the user")
  private String name;

  @NotNull
  @Schema(example = "+35191919191", description = "The phone number of the user")
  private String phoneNumber;

  @NotNull
  @Schema(example = "Street something 99", description = "The address of the user")
  private String address;

  public User(String email, String name, String phoneNumber, String address) {
    this.email = email;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
