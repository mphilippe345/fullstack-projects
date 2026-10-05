/* eslint-disable no-mixed-operators */
import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import { QuantityPicker } from 'react-qty-picker';
import AddShoppingCartIcon from '@material-ui/icons/AddShoppingCart';
import CloseIcon from '@mui/icons-material/Close';
import FavoriteIcon from '@material-ui/icons/Favorite';
import IconButton from '@material-ui/core/IconButton';
import { toast } from 'react-toastify';
import ReviewsIcon from '@mui/icons-material/Reviews';
import { useCart } from '../checkout-page/CartContext';
import { addProductToWishList, removeProductFromWishList } from '../product-card/ProductCardService';

const ProductModal = ({
  open, clickedProduct, handleCloseModal, handleReviewOpen,
  user, setUser, loginTracker
}) => {
  const [quantityValue, setQuantityValue] = useState(1);
  const { dispatch } = useCart();
  // eslint-disable-next-line no-console
  console.log(quantityValue);
  const style = {
    Modal: {
      position: 'absolute',
      top: '50%',
      left: '50%',
      transform: 'translate(-50%, -50%)',
      width: 750,
      height: 500,
      bgcolor: '#f6e4f2',
      borderRadius: 5,
      boxShadow: 24,
      outline: 0,
      p: 4
    },

    productImage: {
      position: 'fixed',
      width: 'auto',
      height: 510,
      left: 18,
      top: 27
    },

    title: {
      position: 'relative',
      float: 'right',
      right: -55,
      fontSize: 25,
      top: 5,
      width: '400px',
      whiteSpace: 'initial',
      overflow: 'hidden'
    },

    description: {
      position: 'relative',
      right: -6,
      top: 50
    },

    descriptionContent: {
      position: 'relative',
      float: 'right',
      right: -55,
      top: 50,
      fontSize: 21,
      width: '400px',
      whiteSpace: 'initial',
      overflow: 'hidden'
    },

    category: {
      position: 'relative',
      float: 'left',
      left: 6,
      top: 70
    },

    categoryContent: {
      position: 'relative',
      float: 'left',
      left: 6,
      top: 70,
      fontSize: 21,
      paddingRight: 10,
      width: '400px',
      whiteSpace: 'initial'
    },

    type: {
      position: 'relative',
      float: 'left',
      left: 6,
      top: 97
    },

    typeContent: {
      position: 'relative',
      float: 'left',
      left: -34,
      top: 120,
      fontSize: 21,
      width: '290px',
      whiteSpace: 'initial',
      overFlow: 'hidden'

    },

    price: {
      position: 'relative',
      float: 'right',
      fontSize: 20,
      right: 3,
      top: 301,
      width: '150px',
      whiteSpace: 'initial'
    },

    quantity: {
      position: 'relative',
      float: 'right',
      right: -60,
      top: 257,
      whiteSpace: 'inital'
    },

    productcolors: {
      position: 'relative',
      float: 'right',
      right: 292,
      top: 147
    },
    colorscontent: {
      width: 60,
      height: 30,
      margin: 5,
      border: '5px',
      borderRadius: 10,
      position: 'relative',
      float: 'right',
      right: 228,
      top: 170
    },
    colorscontent2: {
      width: 60,
      height: 30,
      margin: 5,
      border: '5px',
      borderRadius: 10,
      position: 'relative',
      float: 'right',
      right: 87,
      top: 170
    },
    carticon: {
      position: 'relative',
      float: 'bottom-right',
      right: -235,
      top: 215
    },
    closeicon: {
      position: 'relative',
      float: 'right',
      right: -55,
      top: -330,
      height: '20px',
      width: 30
    },

    modalContent: {
      position: 'relative',
      width: 350,
      height: 500,
      float: 'right',
      right: 30
    },
    reviewButtion: {
      position: 'relative',
      float: 'bottom-right',
      right: -330,
      top: 167
    },
    favoriteButton: {
      position: 'relative',
      float: 'bottom-right',
      right: -235,
      top: 215
    },
    inWishList: {
      color: '#99078c'
    },
    inherit: {
      color: 'inherit'
    }

  };

  /**
   * @name onAdd
   * @description handles adding a product to the cart.
   */
  const onAdd = () => {
    dispatch(
      {
        type: 'add',
        product: {
          id: clickedProduct.id,
          title: clickedProduct.name,
          price: clickedProduct.price,
          description: clickedProduct.description,
          quantity: quantityValue
        }
      }
    );
    toast.success(`${clickedProduct.name} added successfully to cart`);
  };

  /**
   * @name onAddToWishListClick
   * @description Handles adding product to users wishlist when favorite icon is clicked.
   * @param {*} e
   */
  const onAddToWishListClick = (e) => {
    e.stopPropagation();
    const userEmail = localStorage.getItem('userEmail');
    const productId = clickedProduct.id;
    if (loginTracker === true
      && !user.wishList.some((w) => w.productID === clickedProduct.id)) {
      addProductToWishList(clickedProduct, userEmail, setUser);
    }
    if (loginTracker === true
      && user.wishList.some((w) => w.productID === clickedProduct.id)) {
      removeProductFromWishList(clickedProduct, userEmail, productId, setUser);
    }
  };

  return (
    <Modal
      open={open}
      onClose={handleCloseModal}
    >
      <Box sx={style.Modal}>
        <div style={style.modalContent}>
          <Typography style={style.title}>
            {clickedProduct.name}
          </Typography>
          <Typography style={style.description}>
            Description:
          </Typography>
          <div style={style.descriptionarea}>
            <Typography style={style.descriptionContent}>
              {clickedProduct.description}
            </Typography>
          </div>
          <Typography style={style.category}>
            Category:
          </Typography>
          <Typography style={style.categoryContent}>
            {clickedProduct.category}
          </Typography>
          <Typography style={style.type}>
            Type:
          </Typography>
          <Typography style={style.typeContent}>
            {clickedProduct.type}
          </Typography>
          <Typography style={style.productcolors}>
            Colors:
          </Typography>
          <Typography style={style.colorscontent} bgcolor={clickedProduct.primaryColorCode} />
          <Typography style={style.colorscontent2} bgcolor={clickedProduct.secondaryColorCode} />
          <Typography style={style.price}>
            $
            {''}
            {clickedProduct.price && clickedProduct.price.toFixed(2)}
          </Typography>
          <Typography style={style.quantity}>
            <QuantityPicker
              width="125px"
              value="1"
              min="1"
              onChange={(value) => {
                setQuantityValue(value);
              }}
              smooth
            />
          </Typography>
          <img src={clickedProduct.imageSrc} alt="Product" style={style.productImage} />
          <Typography>
            <IconButton aria-label="add to shopping cart" style={style.carticon} onClick={onAdd}>
              <AddShoppingCartIcon />
            </IconButton>
            <IconButton aria-label="add to favorites" onClick={(e) => onAddToWishListClick(e)} style={style.favoriteButton}>
              <FavoriteIcon style={user
                && user.wishList.some((w) => w.productID === clickedProduct.id)
                ? style.inWishList : style.inherit}
              />
            </IconButton>
          </Typography>
          <IconButton aria-label="Close" onClick={handleCloseModal} style={style.closeicon}>
            <CloseIcon />
          </IconButton>
          {clickedProduct.reviews && clickedProduct.reviews.length > 0
            ? (
              <IconButton style={style.reviewButtion}>
                <ReviewsIcon
                  onClick={() => handleReviewOpen(clickedProduct)}
                />
              </IconButton>
            ) : null}
        </div>
      </Box>
    </Modal>

  );
};

export default ProductModal;
