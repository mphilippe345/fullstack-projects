import React, { useEffect, useState } from 'react';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import Paper from '@material-ui/core/Paper';
import { NavLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import MaintenanceTableRow from '../maintenance-table/MaintenanceTableRow';
import styles from './MaintenancePage.module.css';
import Constants from '../../utils/constants';
import fetchProducts from './MaintenancePageService';
import MaintenanaceEditModal from '../maintenance-edit-modal/MaintenanceEditModal';
import getCreateProductErrorMessages from '../create-product-page/CreateProductValidator';
import updateProduct from '../maintenance-edit-modal/MaintenanceEditService';
// import CreateProductPage from '../create-product-page/CreateProductPage';

/**
 * @name MaintenancePage
 * @description fetches products from API and displays products in a table sorted by ID
 * @return MaintenancePage
 */
const MaintenancePage = () => {
  const [products, setProducts] = useState([]);
  const [apiError, setApiError] = useState(false);
  const [open, setOpen] = React.useState(false);
  const [clickedRow, setClickedRow] = useState({});
  const [errors, setErrors] = React.useState({});
  const [checked, setChecked] = React.useState(false);
  const [isChecked, setIschecked] = useState(false);

  useEffect(() => {
    fetchProducts(setProducts, setApiError);
  }, []);

  const [newProductData, setNewProductData] = React.useState({
    active: '',
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

  const handleOpenModal = (product) => {
    setOpen(true);
    setClickedRow(product);
    setNewProductData(clickedRow);
  };

  const handleCloseModal = () => {
    setOpen(false);
    setErrors({});
  };

  const [updatedProductData, setUpdatedProductData] = React.useState({
    active: true,
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

  const handleChecked = () => {
    setChecked(!checked);
    if (checked) {
      newProductData.active = true;
      setIschecked(true);
    } else {
      newProductData.active = false;
      setIschecked(false);
    }
  };

  const onProductChange = (e) => {
    setNewProductData({ ...newProductData, [e?.target?.id]: e?.target?.value });
  };

  const isFormValid = () => {
    const errorMessages = getCreateProductErrorMessages(newProductData);
    setErrors(errorMessages);
    return Object.keys(errorMessages).length === 0;
  };

  const handleSubmit = async () => {
    const updatedProduct = {
      active: newProductData.active,
      name: newProductData.name,
      price: parseInt(newProductData?.price, 10).toFixed(2),
      quantity: newProductData.quantity,
      sku: newProductData.sku.toUpperCase(),
      description: newProductData.description,
      demographic: newProductData.demographic,
      category: newProductData.category,
      type: newProductData.type,
      brand: newProductData.brand,
      material: newProductData.material,
      imageSrc: newProductData.imageSrc?.toLowerCase(),
      primaryColorCode: newProductData.primaryColorCode?.toLowerCase(),
      secondaryColorCode: newProductData.secondaryColorCode?.toLowerCase(),
      styleNumber: newProductData.styleNumber?.toUpperCase(),
      globalProductCode: newProductData.globalProductCode?.toLowerCase(),
      releaseDate: newProductData.releaseDate
    };

    if (isFormValid()) {
      const isoDate = new Date(newProductData.releaseDate).toISOString();
      updatedProduct.releaseDate = isoDate;
      setUpdatedProductData(updatedProduct);
      await updateProduct(updatedProduct, setClickedRow, clickedRow?.id);
      handleCloseModal();
      toast.success('Product information has successfully updated');
    }
  };
  useEffect(() => {
    setNewProductData(clickedRow);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [clickedRow]);

  useEffect(() => {
    clickedRow.name = updatedProductData.name;
    clickedRow.price = updatedProductData.price;
    clickedRow.quantity = updatedProductData.quantity;
    clickedRow.sku = updatedProductData.sku;
    clickedRow.description = updatedProductData.description;
    clickedRow.demographic = updatedProductData.demographic;
    clickedRow.type = updatedProductData.type;
    clickedRow.brand = updatedProductData.brand;
    clickedRow.material = updatedProductData.material;
    clickedRow.imageSrc = updatedProductData.imageSrc;
    clickedRow.primaryColorCode = updatedProductData.primaryColorCode;
    clickedRow.secondaryColorCode = updatedProductData.secondaryColorCode;
    clickedRow.styleNumber = updatedProductData.styleNumber;
    clickedRow.globalProductCode = updatedProductData.globalProductCode;
    clickedRow.releaseDate = updatedProductData.releaseDate;
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [updatedProductData]);

  useEffect(() => {
    if (clickedRow?.active === true) {
      setIschecked(true);
    } else {
      setIschecked(false);
    }
  }, [clickedRow?.active, newProductData]);
  return (
    <div>
      {apiError && <p className={styles.errMsg} data-testid="errMsg">{Constants.API_ERROR}</p>}
      <div className="buttonContainer">
        <NavLink to="/create-product">
          <button className={styles.createNewProductButton} type="submit">
            <text>+  Create New Product</text>
          </button>
        </NavLink>
        <NavLink to="/create-promo">
          <button className={styles.createPromoButton} type="submit">
            <text>+  Create New Promo</text>
          </button>
        </NavLink>
      </div>

      <div>
        <MaintenanaceEditModal
          open={open}
          setOpen={setOpen}
          clickedRow={clickedRow}
          setClickedRow={setClickedRow}
          onChange={onProductChange}
          newProductData={newProductData}
          handleSubmit={handleSubmit}
          handleCloseModal={handleCloseModal}
          errors={errors}
          handleChecked={handleChecked}
          checked={checked}
          isChecked={isChecked}
        />
        <div>
          <TableContainer
            style={{
              height: '700px', width: '95%', margin: 'auto', marginTop: '10px'
            }}
            component={Paper}
          >
            <Table className={styles.MaintenanceTable}>
              <TableHead className={styles.MaintenanceTableHeader}>
                <TableRow>
                  <TableCell> </TableCell>
                  <TableCell>ID</TableCell>
                  <TableCell>Active</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Price</TableCell>
                  <TableCell>Quantity</TableCell>
                  <TableCell>Sku</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell>Demographic</TableCell>
                  <TableCell>Category</TableCell>
                  <TableCell>Type</TableCell>
                  <TableCell>Brand</TableCell>
                  <TableCell>Material</TableCell>
                  <TableCell>ImageSrc</TableCell>
                  <TableCell>Primary Color</TableCell>
                  <TableCell>Secondary Color</TableCell>
                  <TableCell>Style Number</TableCell>
                  <TableCell>Global Product Code</TableCell>
                  <TableCell>Date Created</TableCell>
                  <TableCell>Date Modified</TableCell>
                  <TableCell>Release Date</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {products.map((product) => (
                  <MaintenanceTableRow product={product} handleOpenModal={handleOpenModal} />
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
    </div>
  );
};

export default MaintenancePage;
