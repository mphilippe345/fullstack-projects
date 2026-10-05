import React from 'react';
import { toast } from 'react-toastify';
import { useHistory } from 'react-router-dom';
import { useCart } from './CartContext';
import styles from './CheckoutPage.module.css';
import ReviewOrderWidget from './ReviewOrderWidget';
import DeliveryAddress from './forms/DeliveryAddress';
import BillingDetails from './forms/BillingDetails';
import makePurchase from './CheckoutService';
import getCheckoutErrorMessages from './CheckoutValidator';

/**
 * @name CheckoutPage
 * @description A view that contains details needed to process a transaction for items
 * @return component
 */
const CheckoutPage = () => {
  const history = useHistory();

  const {
    state: { products }
  } = useCart();

  const [billingData, setBillingData] = React.useState({
    billingStreet: '',
    billingStreet2: '',
    billingCity: '',
    billingZip: '',
    billingState: null,
    email: '',
    phone: '',
    creditCard: '',
    cvv: '',
    expiration: '',
    cardholder: ''
  });
  const [deliveryData, setDeliveryData] = React.useState({
    firstName: '',
    lastName: '',
    deliveryStreet: '',
    deliveryStreet2: '',
    deliveryCity: '',
    deliveryZip: '',
    deliveryState: null
  });
  const [errors, setErrors] = React.useState({});
  const [checked, setChecked] = React.useState(false);

  /** The event is every keystroke where it updates the information of the target
   * input box right into the billingData
   */
  const onBillingChange = (e) => {
    setBillingData({ ...billingData, [e.target.id]: e.target.value });
  };

  /** The event is everykeystroke where it updates the information of the target
   * input box right into the deliveryData
   */
  const onDeliveryChange = (e) => {
    setDeliveryData({ ...deliveryData, [e.target.id]: e.target.value });
  };

  /** Gets the information in the input boxes validated and sets the error messages
   * @returns errorMessages
   */
  const setErrorMessages = () => {
    const errorMessages = getCheckoutErrorMessages(billingData, deliveryData, checked);
    setErrors(errorMessages);
    return errorMessages;
  };

  /** Checks to see if there are any states set into {object} errors
   * @param function setErrorMessages, sets errors and returns the {object} errors
   * @return a boolean
   */
  const isFormValid = (errorMessages) => Object.keys(errorMessages).length === 0;

  /** Checks to see if the checkbox is checked or not
   * @return a boolean
   */
  const handleCheck = () => {
    setChecked(!checked);
  };

  /**
   * This handles the information that is inputed and sent to to be validated. If it is passes
   * validation, it will sent it to the API to be sent to the database. It will present
   * a toast for if it succeeds or fails.
   */
  const handlePay = () => {
    const productData = products.map(({ id, quantity }) => ({ productId: id, quantity }));
    const deliveryAddress = {
      deliveryFirstName: deliveryData.firstName.trim(),
      deliveryLastName: deliveryData.lastName.trim(),
      deliveryStreet: deliveryData.deliveryStreet.trim(),
      deliveryStreet2: deliveryData.deliveryStreet2.trim(),
      deliveryCity: deliveryData.deliveryCity.trim(),
      deliveryState: deliveryData.deliveryState,
      deliveryZip: deliveryData.deliveryZip
    };
    const billingAddress = {};
    if (checked) {
      billingAddress.billingStreet = deliveryAddress.deliveryStreet.trim();
      billingAddress.billingStreet2 = deliveryAddress.deliveryStreet2.trim();
      billingAddress.billingCity = deliveryAddress.deliveryCity.trim();
      billingAddress.billingState = deliveryAddress.deliveryState;
      billingAddress.billingZip = deliveryAddress.deliveryZip;
    } else {
      billingAddress.billingStreet = billingData.billingStreet.trim();
      billingAddress.billingStreet2 = billingData.billingStreet2.trim();
      billingAddress.billingCity = billingData.billingCity.trim();
      billingAddress.billingState = billingData.billingState;
      billingAddress.billingZip = billingData.billingZip;
    }
    billingAddress.email = billingData.email.trim();
    billingAddress.phone = billingData.phone.trim();

    const creditCard = {
      cardNumber: billingData.creditCard,
      cvv: billingData.cvv,
      expiration: billingData.expiration,
      cardholder: billingData.cardholder
    };

    if (isFormValid(setErrorMessages())) {
      makePurchase(productData, deliveryAddress, billingAddress, creditCard).then(() => history.push('/confirmation'));
    } else {
      toast.error('Invalid entry. Your card was not charged. Please check form for details.');
    }
  };

  return (
    <div className={styles.checkoutContainer}>
      <div className={`${styles.step} ${styles.order}`}>
        <h3 className={styles.title}>1. Review Order</h3>
        <ReviewOrderWidget deliveryData={deliveryData} />
      </div>
      <div className={`${styles.step} ${styles.delivery}`}>
        <h3 className={styles.title}>2. Delivery Address</h3>
        <DeliveryAddress
          onChange={onDeliveryChange}
          deliveryData={deliveryData}
          errors={errors}
        />
        <label htmlFor="useSame" className={styles.sameAddressText}>
          <div className={styles.useSameAddress}>
            <input
              id="useSame"
              onChange={handleCheck}
              type="checkbox"
              value={checked}
            />
          </div>
          Same Billing Address
        </label>
      </div>
      <div className={`${styles.step} ${styles.payment}`}>
        <h3 className={styles.title}>3. Billing Details</h3>
        <BillingDetails
          onChange={onBillingChange}
          billingData={billingData}
          useShippingForBilling={checked}
          errors={errors}
        />
      </div>
      <div className={styles.payNow}>
        <button onClick={handlePay} type="button" className={styles.payButton}>
          Checkout
        </button>
      </div>
    </div>
  );
};

export default CheckoutPage;
