/* eslint-disable no-mixed-operators */
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import AddShoppingCartIcon from '@material-ui/icons/AddShoppingCart';
import ShareIcon from '@material-ui/icons/Share';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import ReviewsIcon from '@mui/icons-material/Reviews';
import { toast } from 'react-toastify';
import { useCart } from '../checkout-page/CartContext';
import { addProductToWishList, removeProductFromWishList } from './ProductCardService';

/**
 * @name useStyles
 * @description Material-ui styling for ProductCard component
 * @return styling
 */
const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: 345
  },
  media: {
    height: 0,
    paddingTop: '56.25%'
  },
  expand: {
    transform: 'rotate(0deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
      duration: theme.transitions.duration.shortest
    })
  },
  expandOpen: {
    transform: 'rotate(180deg)'
  },
  avatar: {
    backgroundColor: red[500]
  },
  inWishList: {
    color: '#99078c'
  },
  default: {
    color: 'inherit'
  }
}));

/**
 * @name ProductCard
 * @description displays single product card component
 * @param {*} props product
 * @return component
 */
const ProductCard = ({
  product, handleOpenModal, handleReviewOpen, loginTracker, user, setUser
}) => {
  const classes = useStyles();
  const { dispatch } = useCart();

  /**
   * @name onAdd
   * @description handles adding a product to the cart.
   * @param {*} e
   */
  const onAdd = (e) => {
    e.stopPropagation();
    dispatch(
      {
        type: 'add',
        product: {
          id: product.id,
          title: product.name,
          price: product.price,
          description: product.description,
          quantity: 1
        }
      }
    );
    toast.success(`${product.name} added successfully to cart`);
  };

  /**
 * @name eventPropagation
 * @description This function is temporary, please remove when functionality is added for
 * ShareIcon and FavoriteIcon. Add e.stopPropagation to the top of your event handler.
 * @param {*} e
 */
  const eventPropagation = (e) => {
    e.stopPropagation();
  };

  /**
   * @name onAddToWishListClick
   * @description Handles adding product to users wishlist when favorite icon is clicked.
   * @param {*} e
   */
  const onAddToWishListClick = (e) => {
    e.stopPropagation();
    const userEmail = localStorage.getItem('userEmail');
    const productId = product.id;
    if (loginTracker === true
      && !user.wishList.some((w) => w.productID === product.id)) {
      addProductToWishList(product, userEmail, setUser);
    }
    if (loginTracker === true
      && user.wishList.some((w) => w.productID === product.id)) {
      removeProductFromWishList(product, userEmail, productId, setUser);
    }
  };

  return (
    <Card className={classes.root} onClick={() => handleOpenModal(product)}>
      <CardHeader
        avatar={(
          <Avatar aria-label="demographics" className={classes.avatar}>
            {product.demographic.charAt(0)}
          </Avatar>
        )}
        action={(
          <IconButton aria-label="settings">
            <MoreVertIcon />
          </IconButton>
        )}
        title={product.name}
        subheader={`${product.demographic} ${product.category} ${product.type}`}
      />
      <CardMedia
        className={classes.media}
        image={`${product.imageSrc}`}
        title={`${product.name}`}
      />
      <CardContent onClick={() => [handleOpenModal(product)]}>
        <Typography variant="body2" color="textSecondary" component="p">
          {product.description}
        </Typography>
        <br />
        <Typography variant="body2" color="textSecondary" component="p">
          Price: $
          {product.price.toFixed(2)}
        </Typography>
      </CardContent>
      <CardActions disableSpacing>
        <IconButton aria-label="add to favorites" onClick={(e) => onAddToWishListClick(e)}>
          <FavoriteIcon className={user && user.wishList.some((w) => w.productID === product.id)
            ? classes.inWishList : classes.inherit}
          />
        </IconButton>
        <IconButton aria-label="share" onClick={eventPropagation}>
          <ShareIcon />
        </IconButton>
        <IconButton aria-label="add to shopping cart" onClick={onAdd}>
          <AddShoppingCartIcon />
        </IconButton>
        {product.reviews && product.reviews.length > 0
          ? (
            <IconButton>
              <ReviewsIcon onClick={(e) => [e.stopPropagation(), handleReviewOpen(product)]} />
            </IconButton>
          ) : null}
      </CardActions>
    </Card>
  );
};

export default ProductCard;
