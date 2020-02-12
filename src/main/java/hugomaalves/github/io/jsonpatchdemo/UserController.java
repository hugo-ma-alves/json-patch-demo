package hugomaalves.github.io.jsonpatchdemo;

import hugomaalves.github.io.jsonpatchdemo.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.json.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "user", description = "The user API")
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{email}")
  @Operation(description = "Gets a user by its ID (email address)", summary = "Gets a user by its ID (email address)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The user has been found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "404", description = "The user does not exists", content = @Content)})
  public User getUser(@Parameter(description = "The user email address") @PathVariable String email) {
    return userService.getUserByEmail(email);
  }

  @PostMapping("/")
  @Operation(description = "Creates a new user", summary = "Creates a new User")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The user has been created", content = @Content),
      @ApiResponse(responseCode = "409", description = "The user already exists", content = @Content)})
  public ResponseEntity createUser(@RequestBody User newUser) {
    userService.createUser(newUser);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{email}").buildAndExpand(newUser.getEmail()).toUri();
    return ResponseEntity.created(location).build();
  }

  @PatchMapping(value = "/", consumes = "application/json-patch+json")
  @Operation(description = "Updates an existing user", summary = "Updates an existing user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "The user has been updated", content = @Content)})
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUser(@RequestBody JsonPatch patchDocument) {
    //TODO patch the user
  }
}
