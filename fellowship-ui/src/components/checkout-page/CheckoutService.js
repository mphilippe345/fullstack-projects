import { toast } from 'react-toastify';
import HttpHelper from '../../utils/HttpHelper';
import Constants from '../../utils/constants';

/**
 *
 * @name makePayment
 * @description sends a purchase request
 * @param {*} cartContents items to purchase
 * @returns payment confirmation response
 */
const makePurchase = async (products, deliveryAddress, billingAddress, creditCard) => {
  await HttpHelper(Constants.PURCHASE_ENDPOINT, 'POST', {
    lineItems: products,
    deliveryAddress,
    billingAddress,
    creditCard
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then(() => {
      toast.success('Transaction successful.');
    })
    .catch(() => {
      /* eslint-disable no-console */
      console.log('Failed to purchase');
      /* eslint-enable no-console */
    });
};
export default makePurchase;
