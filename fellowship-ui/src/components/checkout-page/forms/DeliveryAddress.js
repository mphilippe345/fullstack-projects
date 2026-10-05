import React from 'react';
import FormItem from '../../form/FormItem';
import FormItemDropdown from '../../form/FormItemDropdown';
import styles from './DeliveryAddress.module.css';

/**
 * @name DeliveryAddress
 * @description Allows entry of delivery address
 * @return component
 */
const DeliveryAddress = ({ onChange, deliveryData, errors }) => {
  const usStates = ['Select state', 'Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'District of Columbia', 'Federated States of Micronesia', 'Florida', 'Georgia', 'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota', 'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Island', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

  return (

    <div className={styles.deliveryAddress}>
      <FormItem
        type="text"
        id="firstName"
        label="First Name"
        onChange={onChange}
        value={deliveryData.firstName}
        errorMessage={errors.firstName}
      />

      <FormItem
        type="text"
        id="lastName"
        label="Last Name"
        onChange={onChange}
        value={deliveryData.lastName}
        errorMessage={errors.lastName}
      />

      <FormItem
        placeholder="e.g. 123 Sesame Street"
        type="text"
        id="deliveryStreet"
        label="Street"
        onChange={onChange}
        value={deliveryData.deliveryStreet}
        errorMessage={errors.deliveryStreet}
      />

      <FormItem
        placeholder="e.g. Unit #1"
        type="text"
        id="deliveryStreet2"
        label="Street 2 (Optional)"
        onChange={onChange}
        value={deliveryData.deliveryStreet2}
        errorMessage={errors.deliveryStreet2}
      />

      <FormItem
        type="text"
        id="deliveryCity"
        label="City"
        onChange={onChange}
        value={deliveryData.deliveryCity}
        errorMessage={errors.deliveryCity}
      />

      <FormItemDropdown
        id="deliveryState"
        label="State"
        onChange={onChange}
        value={deliveryData.deliveryState}
        options={usStates}
        errorMessage={errors.deliveryState}
      />

      <FormItem
        placeholder="e.g. 12345"
        type="text"
        id="deliveryZip"
        label="Zip"
        onChange={onChange}
        value={deliveryData.deliveryZip}
        errorMessage={errors.deliveryZip}
      />
    </div>

  );
};

export default DeliveryAddress;
