import React from 'react';
import styles from './FormItem.module.css';

/**
 * @name FormItem
 * @description Input field
 * @return component
 */
const FormItem = ({
  onChange, value, id, label, placeholder, type, errorMessage
}) => (

  <div className={styles.form}>
    <label className={styles.label} htmlFor={id}>
      {label}
      <div>
        <input
          className={errorMessage ? styles.highlightInputBox : styles.input}
          id={id}
          onChange={onChange}
          placeholder={placeholder}
          type={type}
          value={value}
          // step=".01"
        />
        <div className={errorMessage ? styles.displayErrors : styles.hideErrorsCheckout}>
          {errorMessage || 'Errors displayed here'}
        </div>
      </div>
    </label>
  </div>
);

export default FormItem;
