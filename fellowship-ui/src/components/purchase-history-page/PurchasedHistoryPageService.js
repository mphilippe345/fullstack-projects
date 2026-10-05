import HttpHelper from '../../utils/HttpHelper';
/**
 *
 * @name getPurchaseByUserEmail
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} email for getting purchase based on email
 * @param {*} setUserPurchases sets json response and saves it
 * @returns sets state for products if 200 response, else sets state for apiError
 */
const getPurchasesByUserEmail = async (email, setUserPurchases) => {
  await HttpHelper(`/purchases/${email}`, 'GET')
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then((body) => {
      setUserPurchases(body);
    })
    .catch(() => {});
};

export default getPurchasesByUserEmail;
