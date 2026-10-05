import React, { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import CloseIcon from '@mui/icons-material/Close';
import FormItemDropdown from '../form/FormItemDropdown';
import FormItem from '../form/FormItem';

const MaintenanaceEditModal = ({
  open, clickedRow, errors, handleSubmit, handleCloseModal, onChange,
  handleChecked, checked, isChecked, newProductData
}) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [quantity, setQuantity] = useState('');
  const [sku, setSku] = useState('');
  const [description, setDescription] = useState('');
  const [demographic, setDemographic] = useState('');
  const [category, setCategory] = useState('');
  const [type, setType] = useState('');
  const [brand, setBrand] = useState('');
  const [material, setMaterial] = useState('');
  const [imageSrc, setImageSrc] = useState('');
  const [primaryColorCode, setPrimaryColorCode] = useState('');
  const [secondaryColorCode, setSecondaryColorCode] = useState('');
  const [styleNumber, setStyleNumber] = useState('');
  const [globalProductCode, setGlobalProductCode] = useState('');
  const [releaseDate, setReleaseDate] = useState('');

  const demographicsDropDown = ['Select one', 'Men', 'Women', 'Kids'];

  const newProductData2 = {
    name,
    price,
    quantity,
    sku,
    description,
    demographic,
    category,
    type,
    brand,
    material,
    imageSrc,
    primaryColorCode,
    secondaryColorCode,
    styleNumber,
    globalProductCode,
    releaseDate
  };

  const modalStyle = {
    Modal: {
      position: 'absolute',
      top: '50%',
      left: '50%',
      transform: 'translate(-50%, -50%)',
      width: 750,
      height: 550,
      bgcolor: '#f6e4f2',
      borderRadius: 3,
      boxShadow: 24,
      outline: 0,
      p: 4,
      border: '2px solid',
      overflow: 'scroll',
      overflowX: 'hidden'
    },
    Title: {
      color: '#666666',
      fontfamily: 'Varela Round, sans-serif',
      fontSize: 'xx-large',
      fontWeight: 900,
      textAlign: 'center',
      textTransform: 'uppercase',
      marginTop: '-.5vh'
    },
    submitButton: {
      position: 'relative',
      background: '#f8a1e5',
      borderRadius: '20px',
      color: '#2f4858',
      cursor: 'pointer',
      fontFamily: 'Varela Round, sans-serif',
      fontSize: 'large',
      fontWeight: 700,
      height: '40px',
      marginLeft: '10px',
      width: '135px',
      right: '-285px'
    },

    closeButton: {
      position: 'relative',
      float: 'right',
      right: '-28px',
      top: '-27px',
      height: '25px'
    }

  };

  useEffect(() => {
    setName(clickedRow?.name);
    setPrice(clickedRow?.price);
    setQuantity(clickedRow?.quantity);
    setSku(clickedRow?.sku);
    setDescription(clickedRow?.description);
    setDemographic(clickedRow?.demographic);
    setDemographic(newProductData.demographic);
    setCategory(clickedRow?.category);
    setType(clickedRow?.type);
    setBrand(clickedRow?.brand);
    setMaterial(clickedRow?.material);
    setImageSrc(clickedRow?.imageSrc);
    setPrimaryColorCode(clickedRow?.primaryColorCode);
    setSecondaryColorCode(clickedRow?.secondaryColorCode);
    setStyleNumber(clickedRow?.styleNumber);
    setGlobalProductCode(clickedRow?.globalProductCode);
    setReleaseDate(clickedRow?.releaseDate);
  }, [clickedRow?.name, clickedRow?.price,
    clickedRow?.quantity, clickedRow?.sku,
    clickedRow?.description, clickedRow?.demographic, clickedRow?.category, clickedRow?.type,
    clickedRow?.brand, clickedRow?.material, clickedRow?.imageSrc, clickedRow?.primaryColorCode,
    clickedRow?.secondaryColorCode, clickedRow?.styleNumber, clickedRow?.globalProductCode,
    clickedRow?.releaseDate, newProductData.demographic]);

  return (

    <Modal
      open={open}
      onClose={handleCloseModal}
    >
      <Box sx={modalStyle.Modal}>
        <div style={modalStyle.modalContent}>
          <CloseIcon onClick={handleCloseModal} style={modalStyle.closeButton} />
          <h3 style={modalStyle.Title}> Product Info</h3>
          <label htmlFor="activeChecked" className={modalStyle.checkbox}>
            <div>
              <input
                className={modalStyle.checkbox}
                id="active"
                onChange={handleChecked}
                type="checkbox"
                value={checked}
                checked={isChecked}
              />
            &nbsp; Product is active.
            </div>
          </label>
          <br />

          <FormItem
            label="Name"
            type="text"
            id="name"
            onChange={onChange}
            placeholder={name}
            errorMessage={errors.name}
          />

          <FormItem
            label="Price $"
            min="0"
            count=".01"
            step=".01"
            type="number"
            id="price"
            onChange={onChange}
            placeholder={price}
            errorMessage={errors.price}
          />

          <FormItem
            label="Quantity"
            min="1"
            step="1"
            type="number"
            id="quantity"
            onChange={onChange}
            placeholder={quantity}
            errorMessage={errors.quantity}
          />

          <FormItem
            label="SKU"
            type="text"
            id="sku"
            onChange={onChange}
            placeholder={sku}
            errorMessage={errors.sku}
          />

          <FormItem
            label="Description"
            type="text"
            id="description"
            onChange={onChange}
            placeholder={description}
            errorMessage={errors.description}
          />

          <FormItemDropdown
            label="Demographic"
            type="text"
            options={demographicsDropDown}
            id="demographic"
            onChange={onChange}
            placeholder={demographic}
            errorMessage={errors.demographic}
            value={demographic}
          />

          <FormItem
            label="Category"
            type="text"
            id="category"
            onChange={onChange}
            placeholder={category}
            errorMessage={errors.category}
          />

          <FormItem
            label="Type"
            type="text"
            id="type"
            onChange={onChange}
            placeholder={type}
            errorMessage={errors.type}
          />

          <FormItem
            label="Brand"
            type="text"
            id="brand"
            onChange={onChange}
            placeholder={brand}
            errorMessage={errors.brand}
          />

          <FormItem
            label="Material"
            type="text"
            id="material"
            onChange={onChange}
            placeholder={material}
            errorMessage={errors.material}
          />

          <FormItem
            label="Image Source"
            type="text"
            id="imageSrc"
            onChange={onChange}
            placeholder={imageSrc}
            errorMessage={errors.imageSrc}
          />

          <FormItem
            label="Primary Color Code"
            type="text"
            id="primaryColorCode"
            onChange={onChange}
            placeholder={primaryColorCode}
            errorMessage={errors.primaryColorCode}
          />

          <FormItem
            label="Secondary Color Code"
            type="text"
            id="secondaryColorCode"
            onChange={onChange}
            placeholder={secondaryColorCode}
            errorMessage={errors.secondaryColorCode}
          />

          <FormItem
            label="Style Number"
            type="text"
            id="styleNumber"
            onChange={onChange}
            placeholder={styleNumber}
            errorMessage={errors.styleNumber}
          />

          <FormItem
            label="Global Product Code"
            type="text"
            id="globalProductCode"
            onChange={onChange}
            placeholder={globalProductCode}
            errorMessage={errors.globalProductCode}
          />

          <FormItem
            label="Release Date"
            type="text"
            id="releaseDate"
            onChange={onChange}
            placeholder={releaseDate}
            errorMessage={errors.releaseDate}
          />
          <br />
          <br />
          <button
            onClick={handleSubmit}
            type="submit"
            style={modalStyle.submitButton}
            fullWidth
            value={newProductData2}
            variant="contained"
          >
            Submit
          </button>
        </div>
      </Box>
    </Modal>

  );
};

export default MaintenanaceEditModal;
