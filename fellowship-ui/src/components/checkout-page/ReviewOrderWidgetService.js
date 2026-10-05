/**
 * converts a price to a formatted string
 * @param {number} price
 * @returns {string} formatted price
 */
export const toPrice = (price) => `$${price.toFixed(2)}`;

/**
 * Gets the subtotal of an order
 * @param {Object []} products
 * @returns Number
 */
export const getSubtotal = (products) => products.reduce(
  (acc, item) => acc + (item.quantity * item.price), 0
);

/**
 * gets the correct shipping price based on subtotal and delivery state
 * @param {string} deliveryData
 * @param {Object []} products
 * @returns {number} shipping
 */
export const getShipping = (deliveryData, products) => {
  let shipping = 0.00;
  const shippingState = deliveryData.deliveryState;

  if (products.length === 0 || shippingState === null) {
    return shipping;
  }
  if (shippingState === 'Hawaii' || shippingState === 'Alaska') {
    if (getSubtotal(products) < 50.00) {
      shipping = 10.00;
      return shipping;
    }
    shipping = 5.00;
    return shipping;
  }
  if (shippingState !== 'Hawaii' || shippingState !== 'Alaska' || shippingState !== null) {
    if (getSubtotal(products) < 50.00) {
      shipping = 5.00;
      return shipping;
    }
    shipping = 0.00;
    return shipping;
  }
  return shipping;
};

/**
 * returns the total price from subtotal and shipping
 * @param {string} deliveryData
 * @param {Object []} products
 * @returns {number} total
 */
export const getTotal = (deliveryData, products) => {
  let total = 0.00;
  total = getShipping(deliveryData, products) + getSubtotal(products);
  return total;
};
