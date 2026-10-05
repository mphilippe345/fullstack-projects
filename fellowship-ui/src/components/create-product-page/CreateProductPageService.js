import { toast } from 'react-toastify';
import HttpHelper from '../../utils/HttpHelper';
import constants from '../../utils/constants';

const createNewProduct = async (newProduct) => {
  await HttpHelper(constants.PRODUCTS, 'POST', newProduct)
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then(() => {
      toast.success('Your product was successfully created.');
    })
    .catch(() => {
      toast.error('Oops, something went wrong.');
    });
};
export default createNewProduct;
