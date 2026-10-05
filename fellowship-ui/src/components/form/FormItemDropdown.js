import React from 'react';
import styles from './FormItem.module.css';

/**
 * @name FormItemDropdown
 * @description Input field
 * @return component
 */
const FormItemDropdown = ({
  onChange, value, id, label, options, errorMessage
}) => (

  <div>
    <label className={styles.label} htmlFor={id}>
      {label}
      <div>

        <select
          className={errorMessage ? styles.highlightInputBox : styles.input}
          id={id}
          onBlur={onChange}
          onChange={onChange}
          value={value}
        >
          {options.map((optionText) => (
            <option
              value={optionText[value]}
              key={optionText.key}
            >
              {optionText}
            </option>
          ))}
        </select>
        <div className={errorMessage ? styles.displayErrors : styles.hideErrorsCheckout}>
          {errorMessage || 'Errors displayed here'}
        </div>
      </div>
    </label>
  </div>
);

export default FormItemDropdown;
