package com.superhero.superheroapi.domains.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<User> findById(@PathVariable String email) {
    return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user) {
    return ResponseEntity.ok(userService.save(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
    return userService.findById(id)
        .map(existingUser -> {
          user.setId(existingUser.getId());
          return ResponseEntity.ok(userService.save(user));
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    userService.findById(id).ifPresent(user -> userService.deleteById(id));
    return ResponseEntity.noContent().build();
  }
}
