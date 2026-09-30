package com.superhero.superheroapi.domains.giftcard;

import static com.superhero.superheroapi.helpers.randomStringGenerator.generateRandomString;

import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

/**
 * Service class for the gift card features.
 */
@Service
@RequiredArgsConstructor
public class GiftCardService implements IGiftCardService {

    private final Logger logger = LoggerFactory.getLogger(GiftCardService.class);

    private final GiftCardRepository giftCardRepository;

    /**
     * Returns a gift card object based on its ID.
     *
     * @param id ID of the gift card that is being returned.
     * @return gift card with the ID that was given.
     */
    @Override
    public GiftCard getGiftCardById(Long id) {
        Optional<GiftCard> giftCardOptional;

        try {
            giftCardOptional = giftCardRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }

        if (giftCardOptional.isEmpty()) {
            logger.error("Gift card with ID of: " + id + " was not found.");
            throw new NotFound("Gift card with ID of: " + id + " was not found.");
        }
        return giftCardOptional.get();
    }

    /**
     * Returns a gift card object based on its 12 character code.
     *
     * @param code code of the gift card that is being returned.
     * @return gift card with the code that was given.
     */
    @Override
    public GiftCard getGiftCardByCode(String code) {
        Optional<GiftCard> giftCardOptional;

        try {
            giftCardOptional = giftCardRepository.findGiftCardByCode(code);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }

        if (giftCardOptional.isEmpty()) {
            logger.error("Gift card with code of: " + code + " was not found.");
            throw new NotFound("Gift card with code of: " + code + " was not found.");
        }
        return giftCardOptional.get();
    }

    /**
     * Generates a gift card with a balance that is being passed in.
     *
     * @param balance balance that the user of the gift card is requesting.
     * @return A gift card object with an ID, a 12 character code, a current balance, purchase date,and active status.
     */
    @Override
    public GiftCard generateGiftCard(BigDecimal balance) {
        if (!(balance.compareTo(BigDecimal.ZERO) > 0)) {
            logger.error("Balance for a new gift card must be greater than 0.");
            throw new BadRequest("Balance for a new gift card must be greater than 0.");
        }
        GiftCard cardToSave = new GiftCard();
        cardToSave.setBalance(balance);
        cardToSave.setPurchaseDate(new Date());
        cardToSave.setCode(generateGiftCardCode());
        cardToSave.setActive(true);

        try {
            return giftCardRepository.save(cardToSave);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            throw new ServerError(e.getMessage());
        }
    }

    /**
     * Generates the 12 character code for the gift card.
     *
     * @return string with 12 characters and 2 hyphens after every 4 characters.
     */
    public String generateGiftCardCode() {
        String generatedCode;

        do {
            generatedCode = generateRandomString(12, true);
            boolean giftCardExists;

            try {
               giftCardExists = giftCardRepository.existsByCode(generatedCode);
            } catch (DataAccessException e) {
                logger.error(e.getMessage());
                throw new ServerError(e.getMessage());
            }

            if (!giftCardExists) {
                break;
            }

        } while (true);

        return generatedCode;
    }
}
