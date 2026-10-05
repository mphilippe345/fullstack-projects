import React from 'react';
import FormItemDropdown from '../../form/FormItemDropdown';
import FormItem from '../../form/FormItem';
import styles from '../CreateProductPage.module.css';

/**
 * @name ProductDetails
 * @description Allows entry of Product Details
 * @return component
 */
const CreateProductForm = ({
  onChange, errors, newProductData, handleChecked
}) => {
  const demographicsDropDown = ['Select one', 'Men', 'Women', 'Kids'];
  const checked = true;

  return (
    <div>
      <label htmlFor="activeChecked" className={styles.checkbox}>
        <div>
          <input
            className={styles.checkbox}
            id="active"
            onChange={handleChecked}
            type="checkbox"
            value={checked}
          />
            &nbsp; Product is active.
        </div>
      </label>
      <br />

      <FormItem
        label="Name"
        placeholder="e.g. Top-Notch High Tops"
        type="text"
        id="name"
        onChange={onChange}
        value={newProductData.name}
        errorMessage={errors.name}
      />

      <FormItem
        label="Price $"
        placeholder="e.g. 123.45"
        min="0"
        count=".01"
        step=".01"
        type="number"
        id="price"
        onChange={onChange}
        value={newProductData.price}
        errorMessage={errors.price}
      />

      <FormItem
        label="Quantity"
        placeholder="e.g. 109"
        min="1"
        step="1"
        type="number"
        id="quantity"
        onChange={onChange}
        value={newProductData.quantity}
        errorMessage={errors.quantity}
      />

      <FormItem
        label="SKU"
        placeholder="e.g. AAA-BBB-CCC"
        type="text"
        id="sku"
        onChange={onChange}
        value={newProductData.sku}
        errorMessage={errors.sku}
      />

      <FormItem
        label="Description"
        placeholder="e.g. comfortable"
        type="text"
        id="description"
        onChange={onChange}
        value={newProductData.description}
        errorMessage={errors.description}
      />

      <FormItemDropdown
        label="Demographic"
        type="text"
        options={demographicsDropDown}
        id="demographic"
        onChange={onChange}
        value={newProductData.demographic}
        errorMessage={errors.demographic}
      />

      <FormItem
        label="Category"
        placeholder="e.g. basketball"
        type="text"
        id="category"
        onChange={onChange}
        value={newProductData.category}
        errorMessage={errors.category}
      />

      <FormItem
        label="Type"
        placeholder="e.g. belt"
        type="text"
        id="type"
        onChange={onChange}
        value={newProductData.type}
        errorMessage={errors.type}
      />

      <FormItem
        label="Brand"
        placeholder="e.g. Lowa"
        type="text"
        id="brand"
        onChange={onChange}
        value={newProductData.brand}
        errorMessage={errors.brand}
      />

      <FormItem
        label="Material"
        placeholder="e.g. leather"
        type="text"
        id="material"
        onChange={onChange}
        value={newProductData.material}
        errorMessage={errors.material}
      />

      <FormItem
        label="Image Source"
        placeholder="e.g. https://www.website.com"
        type="text"
        id="imageSrc"
        onChange={onChange}
        value={newProductData.imageSrc}
        errorMessage={errors.imageSrc}
      />

      <FormItem
        label="Primary Color Code"
        placeholder="e.g. #ffffff"
        type="text"
        id="primaryColorCode"
        onChange={onChange}
        value={newProductData.primaryColorCode}
        errorMessage={errors.primaryColorCode}
      />

      <FormItem
        label="Secondary Color Code"
        placeholder="e.g. #ffffff"
        type="text"
        id="secondaryColorCode"
        onChange={onChange}
        value={newProductData.secondaryColorCode}
        errorMessage={errors.secondaryColorCode}
      />

      <FormItem
        label="Style Number"
        placeholder="e.g. A1234"
        type="text"
        id="styleNumber"
        onChange={onChange}
        value={newProductData.styleNumber}
        errorMessage={errors.styleNumber}
      />

      <FormItem
        label="Global Product Code"
        placeholder="e.g. po-12345"
        type="text"
        id="globalProductCode"
        onChange={onChange}
        value={newProductData.globalProductCode}
        errorMessage={errors.globalProductCode}
      />

      <FormItem
        label="Release Date"
        placeholder="e.g. 08/19/2012"
        type="text"
        id="releaseDate"
        onChange={onChange}
        value={newProductData.releaseDate}
        errorMessage={errors.releaseDate}
      />

    </div>
  );
};

export default CreateProductForm;
