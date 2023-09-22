package org.ieti.controllers.user;

import org.ieti.exeptions.UserNotFoundException;
import org.ieti.models.User;
import org.ieti.models.UserDto;
import org.ieti.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users/")
public class UserController {

    private final UserService usersService;
    @Autowired
    public UserController(UserService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDTO) {
        //TODO implement this method
        User user = new User(userDTO);

        URI createdUserUri = URI.create("");
        return ResponseEntity.created(createdUserUri).body(usersService.save(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        //TODO implement this method
        return ResponseEntity.ok(usersService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        //TODO implement this method
        return ResponseEntity.ok(usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id,@RequestBody UserDto userDTO ) {
        //TODO implement this method
        usersService.update(id, userDTO);
        User updateUser = usersService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        //TODO implement this method
        usersService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}