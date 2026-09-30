package com.superhero.superheroapi.domains.promo;

import static com.superhero.superheroapi.constants.StringConstants.DATA_ACCESS_ERROR;

import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.product.ProductRepository;
import com.superhero.superheroapi.exception.Conflict;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoService implements IPromoService {

  private final Logger logger = LoggerFactory.getLogger(PromoService.class);

  private final PromoRepository promoRepository;

  private final ProductRepository productRepository;

  private final DiscountComicsRepository discountComicsRepository;

  /**
   * Retrieves a list of all promo codes.
   *
   * @return A list of PromoCode objects representing all available promo codes.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public List<PromoCode> getAllPromos() {

    List<PromoCode> promos;

    try {
      promos = promoRepository.findAllByActive(true);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return promos;
  }


  /**
   * Creates a new promo code.
   *
   * @param newPromo The PromoCode object representing the new promo code to be created.
   * @return The PromoCode object representing the newly created promo code.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public PromoCode createPromo(PromoCode newPromo) {
    PromoCode savedPromo;
    try {
      savedPromo = promoRepository.save(newPromo);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
    return savedPromo;
  }

  /**
   * Assigns a promo code to a comic by creating a new association between them.
   *
   * @param promoId The ID of the promo code.
   * @param comicId The ID of the comic.
   * @return The DiscountedComics object representing the newly created association between the
   * promo code and the comic.
   * @throws ServerError if there is an error accessing the data repository.
   * @throws NotFound    if the promo code or the comic is not found.
   * @throws Conflict    if the promo code is already assigned to the comic.
   */
  @Override
  public DiscountedComics assignPromo(Long promoId, Long comicId) {
    PromoCode promo;
    Product comic;

    DiscountedComics promoComic = new DiscountedComics();

    try {
      comic = productRepository.findById(comicId).orElse(null);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    if (comic == null) {
      logger.error("Product with ID of: " + comicId + " was not found.");
      throw new NotFound("Product with ID of: " + comicId + " was not found.");
    }

    try {
      promo = promoRepository.findById(promoId).orElse(null);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    if (promo == null) {
      logger.error("PromoCode with ID of: " + promoId + " was not found.");
      throw new NotFound("PromoCode with ID of: " + promoId + " was not found.");
    }

    promoComic.setPromo_id(promoId);
    promoComic.setProduct_id(comicId);

    try {
      promoComic = discountComicsRepository.save(promoComic);
    } catch (DataIntegrityViolationException e) {
      logger.error("PromoCode with ID of: " + promoId + "is already assigned to comic with ID of: "
          + comicId);
      throw new Conflict(
          "PromoCode with ID of: " + promoId + " is already assigned to comic with ID of: "
              + comicId);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    return promoComic;

  }

  /**
   * Retrieves a list of promo codes assigned to a comic based on the given comic ID.
   *
   * @param comicId The ID of the comic to retrieve the assigned promo codes for.
   * @return A list of PromoCode objects representing the promo codes assigned to the comic.
   * @throws ServerError if there is an error accessing the data repository.
   */
  @Override
  public List<PromoCode> findAssignedPromosByProductId(Long comicId) {
    List<DiscountedComics> discountedComics;
    List<PromoCode> promosAssignedToComic;

    List<Long> ids = new ArrayList<>();
    try {
      discountedComics = discountComicsRepository.findAllByProductId(comicId);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    for (DiscountedComics promoComic : discountedComics) {
      ids.add(promoComic.getPromo_id());
    }

    try {
      promosAssignedToComic = promoRepository.findAllById(ids);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    return promosAssignedToComic;
  }

}
