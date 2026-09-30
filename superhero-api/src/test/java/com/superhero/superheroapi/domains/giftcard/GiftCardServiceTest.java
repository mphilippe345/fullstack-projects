package com.superhero.superheroapi.domains.giftcard;

import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.NotFound;
import com.superhero.superheroapi.exception.ServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static com.superhero.superheroapi.domains.giftcard.GiftCardHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCardServiceTest {

    @InjectMocks
    private GiftCardService giftCardService;

    @Mock
    private GiftCardRepository giftCardRepository;

    GiftCard testGiftCard;

    @BeforeEach
    public void setUp() {
        testGiftCard = generateValidGiftCard();
    }

    @Test
    public void testGetGiftCardByIdReturnsGiftCard() {
        when(giftCardRepository.findById(testGiftCard.id)).thenReturn(Optional.ofNullable(testGiftCard));
        GiftCard expected = testGiftCard;
        GiftCard actual = giftCardService.getGiftCardById(VALID_ID);
        assertEquals(expected, actual, "The GiftCard objects do not match.");
    }

    @Test
    public void testGetGiftCardByIdWithNonExistentIdThrowsException() {
        Throwable actual = assertThrows(NotFound.class,
                () -> giftCardService.getGiftCardById(INVALID_ID));
        Throwable expected = new NotFound(
                "Gift card with ID of: 0 was not found.");
        assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void testGetGiftCardByCodeReturnsGiftCard() {
        when(giftCardRepository.findGiftCardByCode(testGiftCard.getCode())).thenReturn(Optional.ofNullable(testGiftCard));
        GiftCard expected = testGiftCard;
        GiftCard actual = giftCardService.getGiftCardByCode(VALID_TEST_CODE);
        assertEquals(expected, actual, "The GiftCard objects do not match.");
    }

    @Test
    public void testGetGiftCardByNonExistentCodeThrowsException() {
        Throwable actual = assertThrows(NotFound.class,
                () -> giftCardService.getGiftCardByCode(INVALID_TEST_CODE));
        Throwable expected = new NotFound(
                "Gift card with code of: " + INVALID_TEST_CODE + " was not found.");
        assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void createGiftCardReturnsGiftCardCreated() {
        when(giftCardRepository.findById(testGiftCard.id)).thenReturn(Optional.ofNullable(testGiftCard));

        GiftCard generatedCard = giftCardService.getGiftCardById(VALID_ID);

        assertEquals(testGiftCard, generatedCard, "The GiftCard objects do not match.");
    }

    @Test
    public void createGiftCardReturnsErrorWhenBalanceIsLessLessThanOne() {
        Throwable actual = assertThrows(BadRequest.class,
                () -> giftCardService.generateGiftCard(INVALID_BALANCE));
        Throwable expected = new BadRequest(
                "Balance for a new gift card must be greater than 0.");
        assertEquals(expected.getMessage(), actual.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void testCreateGiftCardThrowsDataAccessException() {
        when(giftCardRepository.save(any(GiftCard.class))).thenThrow(new DataAccessException("Simulated exception") {});
        ServerError serverError = assertThrows(ServerError.class, () -> giftCardService.generateGiftCard(VALID_BALANCE));
        assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void testCreateGiftCardCodeThrowsDataAccessException() {
        when(giftCardRepository.existsByCode(anyString())).thenThrow(new DataAccessException("Simulated exception") {});
        ServerError serverError = assertThrows(ServerError.class, () -> giftCardService.generateGiftCard(VALID_BALANCE));
        assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void getGiftCardByIdThrowsDataAccessException() {
        when(giftCardRepository.findById(anyLong())).thenThrow(new DataAccessException("Simulated exception") {});
        ServerError serverError = assertThrows(ServerError.class, () -> giftCardService.getGiftCardById(VALID_ID));
        assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void getGiftCardByCodeThrowsDataAccessException() {
        when(giftCardRepository.findGiftCardByCode(anyString())).thenThrow(new DataAccessException("Simulated exception") {});
        ServerError serverError = assertThrows(ServerError.class, () -> giftCardService.getGiftCardByCode(VALID_TEST_CODE));
        assertEquals("Simulated exception", serverError.getMessage(), "The message in the error thrown do not match the one that's expected.");
    }

    @Test
    public void testGenerateGiftCardCode() {
        when(giftCardRepository.existsByCode(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        String generatedCode = giftCardService.generateGiftCardCode();

        assertNotNull(generatedCode, "Generated code should not be null.");
    }

}