import React, { useEffect, useState } from 'react';
import ProductCard from '../product-card/ProductCard';
import styles from './ProductPage.module.css';
import Constants from '../../utils/constants';
import fetchProducts from './ProductPageService';
import FilterMenu from '../filter-menu/FilterMenu';
import ProductModal from '../product-modal/ProductModal';
import ReviewModal from '../review-modal/ReviewModal';

/**
 * @name ProductPage
 * @description fetches products from API and displays products as product cards
 * @return component
 */
const ProductPage = ({ user, loginTracker, setUser }) => {
  const [products, setProducts] = useState([]);
  const [apiError, setApiError] = useState(false);
  const [open, setOpen] = useState(false);
  const [reviewOpen, setReviewOpen] = useState(false);
  const [clickedProduct, setClickedProduct] = useState({});
  const [filters, setFilters] = useState([]);
  const [allowTooSpecificError, setAllowTooSpecificError] = useState(false);

  /**
   * @name handleOpenModal
   * @description handles opening of the product modal.
   * @param {*} product
   */
  const handleOpenModal = (product) => {
    setOpen(true);
    setClickedProduct(product);
  };

  /**
   * @name handleCloseModal
   * @description handles closing of the product modal/
   */
  const handleCloseModal = () => setOpen(false);

  /**
   * @name handleReviewOpen
   * @description handles opening of the review modal.
   * @param {*} product
   */
  const handleReviewOpen = (product) => {
    setOpen(false);
    setReviewOpen(true);
    setClickedProduct(product);
  };

  /**
   * @name handleReviewClose
   * @description handles closing of the the review modal.
   */
  const handleReviewClose = () => setReviewOpen(false);

  /**
   * useEffect to fetch products based on filters selected.
   */
  useEffect(() => {
    fetchProducts(setProducts, setApiError, filters);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  return (
    <div>
      <div>
        <FilterMenu
          setFilters={setFilters}
          filters={filters}
          setProducts={setProducts}
          setApiError={setApiError}
          setAllowTooSpecificError={setAllowTooSpecificError}
        />

      </div>
      {apiError && <p className={styles.errMsg} data-testid="errMsg">{Constants.API_ERROR}</p>}
      <ProductModal
        open={open}
        user={user}
        setUser={setUser}
        loginTracker={loginTracker}
        clickedProduct={clickedProduct}
        handleCloseModal={handleCloseModal}
        handleReviewOpen={handleReviewOpen}
      />
      <ReviewModal
        reviewOpen={reviewOpen}
        handleReviewClose={handleReviewClose}
        clickedProduct={clickedProduct}
      />

      <div className={allowTooSpecificError && products.length === 0
        ? styles.noProducts : styles.app}
      >
        {allowTooSpecificError && products.length === 0 ? (
          <div className={styles.noProducts}>
            No products like that exist. Please reset your filters and try again.
          </div>
        ) : (products.map((product) => (
          <div key={product.id}>
            <ProductCard
              product={product}
              handleOpenModal={handleOpenModal}
              handleReviewOpen={handleReviewOpen}
              user={user}
              setUser={setUser}
              loginTracker={loginTracker}
            />
          </div>
        )))}
      </div>
    </div>
  );
};

export default ProductPage;
