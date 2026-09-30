package com.superhero.superheroapi.domains.purchase;

import static com.superhero.superheroapi.helpers.randomStringGenerator.generateRandomString;

import com.superhero.superheroapi.domains.cart.CartService;
import com.superhero.superheroapi.domains.email.EmailService;
import com.superhero.superheroapi.domains.inventory.IInventoryService;
import com.superhero.superheroapi.domains.product.IProductService;
import com.superhero.superheroapi.domains.product.Product;
import com.superhero.superheroapi.domains.users.UserService;
import com.superhero.superheroapi.exception.BadRequest;
import com.superhero.superheroapi.exception.ServerError;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class PurchaseService implements IPurchaseService {

  @Autowired
  private final ModelMapper mapper;
  @Autowired
  private final EmailService emailService;
  @Autowired
  private final IProductService productService;
  @Autowired
  private final UserService userService;
  @Autowired
  private final IInventoryService inventoryService;
  @Autowired
  private final PurchaseRepository purchaseRepository;
  @Autowired
  private final LineItemRepository lineItemRepository;

  @Autowired
  private final CartService cartService;
  private final Logger logger = LogManager.getLogger(PurchaseService.class);


  public PurchaseService(EmailService emailService, ModelMapper mapper,
      PurchaseRepository purchaseRepository, IProductService productService,
      IInventoryService inventoryService, LineItemRepository lineItemRepository,
      UserService userService, CartService cartService) {
    this.emailService = emailService;
    this.productService = productService;
    this.userService = userService;
    this.purchaseRepository = purchaseRepository;
    this.inventoryService = inventoryService;
    this.lineItemRepository = lineItemRepository;
    this.cartService = cartService;


    this.mapper = mapper;
    TypeMap<Purchase, PurchaseDto> typeMap = mapper.createTypeMap(Purchase.class,
        PurchaseDto.class);
    typeMap.addMappings(_mapper -> {
      _mapper.map(src -> src.getPersonalInfo().getFirstName(), PurchaseDto::setFirstName);
      _mapper.map(src -> src.getPersonalInfo().getEmail(), PurchaseDto::setEmail);
      _mapper.map(Purchase::getDate, PurchaseDto::setDate);
      _mapper.map(Purchase::getOrderTotal, PurchaseDto::setOrderTotal);
      _mapper.map(Purchase::getOrderNumber, PurchaseDto::setOrderNumber);
      _mapper.map(Purchase::getOrderStatus, PurchaseDto::setOrderStatus);
    });
  }
  /**
   * Retrieves all purchases from the database
   *
   * @return all purchases
   */
  @Override
  public List<Purchase> findAllPurchases() {
    try {
      return purchaseRepository.findAll();
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }
  }

  /**
   * Retrieves all purchases containing provided email from the database
   *
   * @param userEmail - email to search by
   * @param page - current page of items to return
   * @param size - number of items per page to return
   * @return all purchases containing the email as a page
   */
  @Override
  public Page<PurchaseDto> getPurchasesByEmail(String userEmail, int page, int size) {
    userService.findByEmail(userEmail);

    Page<Purchase> purchasePage;
    PageRequest pr = sort(page, size, "date");

    try {
      purchasePage = purchaseRepository.findByEmail(userEmail, pr);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    logger.info(String.format("Got all purchases with email: %s", userEmail));
    return purchasePage.map(purchase -> mapper.map(purchase, PurchaseDto.class));
  }

  /**
   * Builds a PageRequest based on the provided page, size, and sort.
   *
   * @param page page number of the order history requested. (zero-indexed)
   * @param size size of the page you want, in other words, how many elements needed.
   * @param sort String of field needed to sort it on the front-end
   * @return PageRequest to be able to use in the repository query.
   */
  private PageRequest sort(int page, int size, String sort) {
    PageRequest pr;
    switch (sort) {
      case "date" -> pr = PageRequest.of(page, size, Sort.by("date").descending());
      default -> pr = PageRequest.of(page, size);
    }
    return pr;
  }

  /**
   * Persists a purchase to the database
   *
   * @param newPurchase - the purchase to persist
   * @return the persisted purchase with ids
   */
  @Override
  @Transactional
  public PurchaseDto savePurchase(Purchase newPurchase) {
    CreditCard creditCard = newPurchase.getCreditCard();

    validatePurchase(creditCard);
    validateCardType(creditCard);
    validateExpirationDate(creditCard.getExpirationDate());

    newPurchase.setOrderTotal(cartService.getCartTotal(newPurchase.getPersonalInfo().getEmail()));

    String orderNumber = String.format("COMIC-%s", generateRandomString(15, false));
    newPurchase.setOrderNumber(orderNumber);

    newPurchase.setDate(LocalDate.now());

    try {
      purchaseRepository.save(newPurchase);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
      throw new ServerError(e.getMessage());
    }

    handleLineItems(newPurchase);

    BigDecimal subTotal = BigDecimal.ZERO;
    for (LineItem item : newPurchase.getProducts()) {
      BigDecimal itemTotal = item.getProduct().getPrice()
          .multiply(BigDecimal.valueOf(item.getQuantity()));
      subTotal = subTotal.add(itemTotal);
    }

    emailService.sendPurchaseConfirmationEmail(newPurchase.getPersonalInfo().getEmail(),
        newPurchase, subTotal);

    newPurchase.setOrderStatus(OrderStatus.PROCESSING_PAYMENT);

    return mapper.map(newPurchase, PurchaseDto.class);
  }

  /**
   * Helper method retrieves product information for each line item and persists it
   *
   * @param purchase - the purchase object to handle line items for
   */
  private void handleLineItems(Purchase purchase) {
    Set<LineItem> itemsList = purchase.getProducts();

    if (itemsList != null) {
      itemsList.forEach(lineItem -> {
        Product product = productService.getProductById(lineItem.getProduct().getId());
        inventoryService
            .updateInventoryAmount(product, lineItem.getQuantity(), lineItem.getCondition());

        if (product != null) {
          lineItem.setProduct(product);
          lineItem.setDate(LocalDate.now());
          lineItem.setDateTime(new Date());
        }

        lineItem.setPurchase(purchase);

        try {
          lineItemRepository.save(lineItem);
        } catch (DataAccessException e) {
          logger.error(e.getMessage());
          throw new ServerError(e.getMessage());
        }
      });
    } else {
      throw new BadRequest("Line items required");
    }
  }

  /**
   * Validates required cardholder and card type
   *
   * @param ccToValidate - the credit card to validate
   */
  private void validatePurchase(CreditCard ccToValidate) {
    if (ccToValidate == null
        || ccToValidate.getCardName() == null || ccToValidate.getCardName().equals("")
        || ccToValidate.getCardType() == null || ccToValidate.getCardType().equals("")
        || Integer.parseInt(ccToValidate.getSecurityCode()) < 100) {
      throw new BadRequest("Transaction denied - invalid credit card information");
    }
  }

  /**
   * Validates if the card number format matches card type
   *
   * @param ccToValidate - the credit card to validate
   */
  private void validateCardType(CreditCard ccToValidate) {
    if ((ccToValidate.getCardType().equals("American Express") && (
        ccToValidate.getCardNumber().length() != 15
            || ccToValidate.getCardNumber().charAt(0) != '3'))
        || (ccToValidate.getCardType().equals("Visa") && (
        ccToValidate.getCardNumber().length() != 16
            || ccToValidate.getCardNumber().charAt(0) != '4'))
        || (ccToValidate.getCardType().equals("Mastercard") && (
        ccToValidate.getCardNumber().length() != 16
            || ccToValidate.getCardNumber().charAt(0) != '5'))
        || (ccToValidate.getCardType().equals("Discover") && (
        ccToValidate.getCardNumber().length() != 16
            || ccToValidate.getCardNumber().charAt(0) != '6'))) {
      throw new BadRequest(
          "Transaction denied - invalid credit card number with " + ccToValidate.getCardType());
    }
  }

  /**
   * Validates whether credit card expiration date has expired
   *
   * @param date - the credit card date to validate
   */
  private void validateExpirationDate(String date) {
    try {
      SimpleDateFormat shortFormat = new SimpleDateFormat("MM/yy");
      SimpleDateFormat longFormat = new SimpleDateFormat("MM/yyyy");
      shortFormat.set2DigitYearStart(shortFormat.parse("01/2000"));

      // concats the first day to MM/yy converted as MM/yyyy
      String formattedDate = "01/" + longFormat.format(shortFormat.parse(date));
      // incoming date to match 01/MM/yyyy
      DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      // cardDate is the string date matching the date format
      LocalDate fullCardDate = LocalDate.parse(formattedDate, df);
      LocalDate endOfExpiration = LocalDate.now().minusMonths(1)
          .with(TemporalAdjusters.lastDayOfMonth());

      // Make sure the card can be used until the end of the month it expires
      if (fullCardDate.isBefore(endOfExpiration)) {
        throw new BadRequest("Transaction denied - date expired");
      }
    } catch (DateTimeParseException | ParseException e) {
      throw new BadRequest("Transaction denied - invalid date format");
    }
  }

}
