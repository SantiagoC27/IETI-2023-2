package org.ieti.controllers.user;

import org.ieti.controllers.user.request.UserDto;
import org.ieti.exeptions.UserNotFoundException;
import org.ieti.models.UserEntity;
import org.ieti.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService usersService;
    @Autowired
    public UserController(UserService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDTO) {
        return ResponseEntity.ok(usersService.save(userDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usersService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok(usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDTO ) {
        usersService.update(id, userDTO);
        UserEntity updateUserEntity = usersService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(updateUserEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        usersService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        usersService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}