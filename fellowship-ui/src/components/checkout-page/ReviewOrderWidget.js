import React from 'react';
import { useHistory } from 'react-router-dom';
import { useCart } from './CartContext';
import OrderItem from './OrderItem';
import {
  getSubtotal, getShipping, toPrice, getTotal
} from './ReviewOrderWidgetService';
import styles from './ReviewOrderWidget.module.css';

/**
 * @name ReviewOrderWidget
 * @description Displays order items, subtotal, shipping, and total
 * @return component
 */
const ReviewOrderWidget = ({ deliveryData }) => {
  const {
    state: { products }
  } = useCart();
  const history = useHistory();
  const redirect = () => {
    history.push('/');
  };

  return (
    <>
      {products.map(({
        price, title, description, quantity
      }) => (
        <OrderItem
          key={title}
          price={price}
          title={title}
          description={description}
          quantity={quantity}
        />
      ))}
      <hr />
      <div className={styles.subtotal}>
        <div>
          <p>Subtotal</p>
        </div>
        <div className={styles.price}>
          <p>{toPrice(getSubtotal(products))}</p>
        </div>
        <div>
          <p>Shipping</p>
        </div>
        <div className={styles.price}>
          <p>{toPrice(getShipping(deliveryData, products))}</p>
        </div>
        <div>
          <p>Total</p>
        </div>
        <div className={styles.price}>
          <p>{toPrice(getTotal(deliveryData, products))}</p>
        </div>
        <div className={styles.keepShopping}>
          {products.length === 0 ? (
            <p>Your cart is empty. Add things here:</p>
          ) : (
            <></>
          )}
          <button onClick={redirect} type="button" className={styles.keepShoppingButton}>
            Keep Shopping
          </button>
        </div>
      </div>
    </>
  );
};

export default ReviewOrderWidget;
