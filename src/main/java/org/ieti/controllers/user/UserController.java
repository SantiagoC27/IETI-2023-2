package org.ieti.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.ieti.models.dto.UserDto;
import org.ieti.exeptions.UserNotFoundException;
import org.ieti.models.UserEntity;
import org.ieti.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Permite crear un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Usuario no creado")
    })
    public ResponseEntity<UserEntity> createUser(
            @Parameter(description = "Usuario para crear", required = true) @RequestBody UserDto userDTO) {
        UserEntity createdUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios recuperados con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles")
    })
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.all();

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{email}")
    @Operation(summary = "Buscar un usuario por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserEntity> findByEmail(
            @Parameter(description = "Correo electrónico del usuario a buscar", required = true) @PathVariable("email") String email) {
        Optional<UserEntity> user = userService.findByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new UserNotFoundException(email);
        }
    }

    @PutMapping("/{email}")
    @Operation(summary = "Actualizar un usuario por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserEntity> updateUser(
            @Parameter(description = "Correo electrónico del usuario a actualizar", required = true) @PathVariable("email") String email,
            @RequestBody UserDto userDTO) {

        UserEntity user = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        String id = user.getId().toString();

        userService.update(id, userDTO);

            UserEntity updateUserEntity = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return ResponseEntity.ok(updateUserEntity);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario por correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Correo electrónico del usuario a eliminar", required = true) @PathVariable("email") String email) {

        UserEntity user = userService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        userService.deleteById(String.valueOf(user.getId()));
        return ResponseEntity.noContent().build();
    }
}