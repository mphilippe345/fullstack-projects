package com.superhero.superheroapi.domains.users;

import com.superhero.superheroapi.domains.cart.CartItem;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  Logger logger = LoggerFactory.getLogger(UserService.class);


  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }
  public User save(User user) {
    List<CartItem> userCartItems = new ArrayList<>();

    user.setCartItems(userCartItems);
    return userRepository.save(user);
  }

  public void deleteById(Long id) {
    userRepository.deleteById(id);
  }

  public User findByEmail(String email) {
    Optional<User> userOptional;

    try {
      userOptional = userRepository.findByEmail(email);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    if (userOptional.isEmpty()) {
      logger.error("User with email: " + email + " was not found.");
      throw new NotFound("User with email: " + email + " was not found.");
    }

    return userOptional.get();
  }
}

