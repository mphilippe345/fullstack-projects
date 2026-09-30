package com.superhero.superheroapi.domains.giftcard;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.superhero.superheroapi.constants.Paths.BASE;
import static com.superhero.superheroapi.constants.Paths.GIFT_CARD_ENDPOINT;

@RestController
@RequestMapping(BASE + GIFT_CARD_ENDPOINT)
@RequiredArgsConstructor
public class GiftCardController {

    private final Logger logger = LoggerFactory.getLogger(GiftCardController.class);

    private final IGiftCardService giftCardService;

    @GetMapping("/{id}")
    public ResponseEntity<GiftCard> getGiftCardById(@PathVariable Long id) {
        logger.info("Returning gift card with id of " + id + "...");
        return new ResponseEntity<>(giftCardService.getGiftCardById(id), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<GiftCard> getGiftCardByCode(@PathVariable String code) {
        logger.info("Returning gift card with id of " + code + "...");
        return new ResponseEntity<>(giftCardService.getGiftCardByCode(code), HttpStatus.OK);
    }

    @PostMapping("/{value}")
    public ResponseEntity<GiftCard> generateGiftCard(@PathVariable BigDecimal value) {
        logger.info("Generating new gift card...");
        return new ResponseEntity<>(giftCardService.generateGiftCard(value), HttpStatus.CREATED);
    }
}
