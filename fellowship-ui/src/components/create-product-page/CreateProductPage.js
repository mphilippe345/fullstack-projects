import React from 'react';
import { useHistory } from 'react-router-dom';
import getCreateProductErrorMessages from './CreateProductValidator';
import styles from './CreateProductPage.module.css';
import CreateProductForm from './forms/CreateProductForm';
import createNewProduct from './CreateProductPageService';

/**
 * @name CreateProductPage
 * @description A view that contains details needed to submit a new product in the database
 * @return component
 */

const CreateProductPage = () => {
  const history = useHistory();
  const [newProductData, setNewProductData] = React.useState({
    active: false,
    name: '',
    price: '',
    quantity: '',
    sku: '',
    description: '',
    demographic: '',
    category: '',
    type: '',
    brand: '',
    material: '',
    imageSrc: '',
    primaryColorCode: '',
    secondaryColorCode: '',
    styleNumber: '',
    globalProductCode: '',
    releaseDate: ''
  });

  const [errors, setErrors] = React.useState({});
  const [checked, setChecked] = React.useState(false);

  const handleChecked = () => {
    setChecked(!checked);
    if (checked) {
      newProductData.active = false;
    } else {
      newProductData.active = true;
    }
  };

  const onProductChange = (e) => {
    setNewProductData({ ...newProductData, [e.target.id]: e.target.value });
  };

  const isFormValid = () => {
    const errorMessages = getCreateProductErrorMessages(newProductData);
    setErrors(errorMessages);
    return Object.keys(errorMessages).length === 0;
  };

  const handleSubmit = () => {
    const newProduct = {
      active: newProductData.active,
      name: newProductData.name,
      price: newProductData.price,
      quantity: newProductData.quantity,
      sku: newProductData.sku.toUpperCase(),
      description: newProductData.description,
      demographic: newProductData.demographic,
      category: newProductData.category,
      type: newProductData.type,
      brand: newProductData.brand,
      material: newProductData.material,
      imageSrc: newProductData.imageSrc.toLowerCase(),
      primaryColorCode: newProductData.primaryColorCode.toLowerCase(),
      secondaryColorCode: newProductData.secondaryColorCode.toLowerCase(),
      styleNumber: newProductData.styleNumber.toUpperCase(),
      globalProductCode: newProductData.globalProductCode.toLowerCase(),
      releaseDate: newProductData.releaseDate
    };

    if (isFormValid()) {
      const isoDate = new Date(newProductData.releaseDate).toISOString();
      newProduct.releaseDate = isoDate;
      createNewProduct(newProduct).then(() => history.push('../../maintenance'));
    }
  };

  return (
    <>
      <div className={styles.createProductContainer}>
        <h3 className={`${styles.createProductTitle}`}>Create &nbsp;New &nbsp;Product</h3>
        <br />

        <CreateProductForm
          newProductData={newProductData}
          onChange={onProductChange}
          errors={errors}
          handleChecked={handleChecked}
        />
        <br />
        <br />
        <button
          onClick={handleSubmit}
          className={styles.createNewProductButton}
          type="submit"
          id="submitNewProduct"
          value={newProductData.submitNewProduct}
          fullWidth
          variant="contained"
        >
          &nbsp;+  Create New Product&nbsp;
        </button>
      </div>
    </>
  );
};

export default CreateProductPage;
