import { toast } from 'react-toastify';
import HttpHelper from '../../utils/HttpHelper';

const updateProduct = async (product, setProduct, id) => {
  await HttpHelper(`/products/${id}`, 'PUT', product)
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then(setProduct)
    .catch(() => {
      toast.error('Oops, something went wrong.');
    });
};

export default updateProduct;
