package com.superhero.superheroapi.domains.giftcard;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.superhero.superheroapi.domains.giftcard.GiftCardHelper.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class GiftCardControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    GiftCardRepository giftCardRepository;

    private static MockMvc mockMvc;

    GiftCard testGiftCard;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        giftCardRepository.save(generateValidGiftCard());
    }

    @Test
    public void getGiftCardByIdReturns200() throws Exception {
        testGiftCard = giftCardRepository.save(generateValidGiftCard());
        mockMvc.perform(get(BASE_GIFT_CARD_PATH + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getGiftCardByIdWithNonExistentIdReturns404() throws Exception {
        mockMvc.perform(get(BASE_GIFT_CARD_PATH + "/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getGiftCardByCodeReturns200() throws Exception {
        mockMvc.perform(get(BASE_GIFT_CARD_PATH + "/code/" + VALID_TEST_CODE))
                .andExpect(status().isOk());
    }

    @Test
    public void getGiftCardByCodeWithNonExistentCodeReturns404() throws Exception {
        mockMvc.perform(get(BASE_GIFT_CARD_PATH + "/code/" + INVALID_TEST_CODE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postGiftCardReturns201() throws Exception {
        mockMvc.perform(post(BASE_GIFT_CARD_PATH + "/{value}", VALID_BALANCE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void postGiftCardWithNoBalanceReturns400() throws Exception {
        mockMvc.perform(post(BASE_GIFT_CARD_PATH + "/{value}", INVALID_BALANCE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}