package org.caicoders.login_api.controllers;

import org.caicoders.login_api.entity.User;
import org.caicoders.login_api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        return (users.isEmpty()) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/userid/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/user/username/{name}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByUserName(@PathVariable("name") String name) {

        AtomicReference<Optional<User>> optionalUser = new AtomicReference<>(Optional.empty());

        userRepository.findAll().forEach(i -> {
            if (i.getName().equals(name)) {
                optionalUser.set(Optional.of(i));
            }
        });

        return optionalUser.get().map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/user/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {

        AtomicReference<Optional<User>> optionalUser = new AtomicReference<>(Optional.empty());

        userRepository.findAll().forEach(i -> {
            if (i.getEmail().equals(email)) {
                optionalUser.set(Optional.of(i));
            }
        });

        return optionalUser.get().map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<List<User>> createUser(@RequestBody User user) {

        List<User> users = userRepository.findAll();

        boolean emailExists = users.stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));
        boolean usernameExists = users.stream().anyMatch(existingUser -> existingUser.getName().equals(user.getName()));

        boolean isValidEmail = user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");

        if (!isValidEmail) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (emailExists) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (usernameExists) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            userRepository.save(user);
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/user/userid/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<List<User>> deleteUser(@PathVariable("id") long id) {

        userRepository.deleteById(id);

        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/userid/{id}", method = RequestMethod.PUT)
    public ResponseEntity<List<User>> updatePassword(@PathVariable("id") long id, @RequestBody User user) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User _user = optionalUser.get();
            _user.setPassword(user.getPassword());
            userRepository.save(_user);
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/recovery/{username}/{email}", method = RequestMethod.GET)
    public ResponseEntity<User> recoveryPassword(@PathVariable("username") String username, @PathVariable("email") String email) {

        AtomicReference<Optional<User>> optionalUser = new AtomicReference<>(Optional.empty());

        userRepository.findAll().forEach(i -> {
            if (i.getName().equals(username) && i.getEmail().equals(email)) {
                optionalUser.set(Optional.of(i));
            }
        });

        return optionalUser.get().map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
