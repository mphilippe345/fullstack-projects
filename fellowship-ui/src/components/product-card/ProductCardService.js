import { toast } from 'react-toastify';
import HttpHelper from '../../utils/HttpHelper';
import Constants from '../../utils/constants';
import getUserByEmail from '../profile-page/ProfilePageService';

/**
 *
 * @name addProductToWishList
 * @description posts a productId and a userId as a wishListItem to the database.
 * @param {*} product product to add,
 * @param {*} userEmail user to add.
 * @param {*} setAdded sets state of favorite button color on product.
 * @returns promo post response, success or error toast
 */
export const addProductToWishList = async (product, userEmail, setUser) => {
  const wishListItem = {};
  wishListItem.productId = product.id;

  await HttpHelper(`${Constants.WISHLIST}/${userEmail}`, 'POST', wishListItem)
    .then((response) => {
      if (response.ok) {
        getUserByEmail(userEmail, setUser);
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then(() => {
      toast.success(`${product.name} successfully added to wishlist.`);
    })
    .catch(() => {
      toast.error('Oops, something went wrong.');
    });
};

export const removeProductFromWishList = async (product, userEmail, productId, setUser) => {
  await HttpHelper(`${Constants.WISHLIST}/${userEmail}/${productId}`, 'DELETE')
    .then((response) => {
      if (response.ok) {
        getUserByEmail(userEmail, setUser);
        return;
      }
      throw new Error(response.statusText);
    })
    .then(() => {
      toast.success(`${product.name} successfully removed from wishlist.`);
    })
    .catch(() => {
      toast.error('Oops, removing went wrong.');
    });
};
