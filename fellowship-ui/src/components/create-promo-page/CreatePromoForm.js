/* eslint-disable jsx-a11y/label-has-associated-control */
import React from 'react';
import FormItem from '../form/FormItem';
import styles from './CreatePromoPage.module.css';

/**
 * @name CreatePromoForms
 * @description Allows entry of Promo Details
 * @return component
 */
const CreatePromoForm = ({
  onChange, errors, promoData
}) => (
  <div>

    <FormItem
      errorMessage={errors.title}
      id="title"
      label="Title"
      onChange={onChange}
      placeholder=" e.g. 50% Off Hats!"
      value={promoData.title}
    />

    <label className={styles.label}>Type</label>
    <form className={styles.radioGroup}>
      <input className={styles.radio} defaultChecked id="type" name="radio" onChange={onChange} type="radio" value="$" />
      <label className={styles.label} htmlFor="$">Flat</label>
      <br />
      <input className={styles.radio} id="type" name="radio" onChange={onChange} type="radio" value="%" />
      <label className={styles.label} htmlFor="%">Percent</label>
      <br />
    </form>

    <FormItem
      errorMessage={errors.description}
      id="description"
      label="Description"
      onChange={onChange}
      placeholder=" e.g. Summer sale."
      value={promoData.description}
    />

    <FormItem
      errorMessage={errors.rate}
      id="rate"
      label="Rate"
      onChange={onChange}
      placeholder="e.g. 50"
      value={promoData.rate}
    />

  </div>
);

export default CreatePromoForm;
