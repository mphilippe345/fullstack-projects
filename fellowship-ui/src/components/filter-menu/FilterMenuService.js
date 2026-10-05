import HttpHelper from '../../utils/HttpHelper';
import Constants from '../../utils/constants';

/**
 *
 * @name fetchBrands
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} setBrands sets state for products
 * @param {*} setApiError sets error if response other than 200 is returned
 * @returns sets state for Brands if 200 response, else sets state for apiError
 */
const fetchBrands = async (setBrands, setApiError) => {
  await HttpHelper(Constants.PRODUCT_BRANDS_ENDPOINT, 'GET')
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(Constants.API_ERROR);
    })
    .then(setBrands)
    .catch(() => {
      setApiError(true);
    });
};

/**
 *
 * @name fetchCategories
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} setCategories sets state for categories
 * @param {*} setApiError sets error if response other than 200 is returned
 * @returns sets state for Brands if 200 response, else sets state for apiError
 */
const fetchCategories = async (setCategories, setApiError) => {
  await HttpHelper(Constants.PRODUCT_CATEGORIES_ENDPOINT, 'GET')
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(Constants.API_ERROR);
    })
    .then(setCategories)
    .catch(() => {
      setApiError(true);
    });
};

/**
 *
 * @name fetchDemographics
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} setDemographics sets state for demographics
 * @param {*} setApiError sets error if response other than 200 is returned
 * @returns sets state for Brands if 200 response, else sets state for apiError
 */
const fetchDemographics = async (setDemographics, setApiError) => {
  await HttpHelper(Constants.PRODUCT_DEMOGRAPHICS_ENDPOINT, 'GET')
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(Constants.API_ERROR);
    })
    .then(setDemographics)
    .catch(() => {
      setApiError(true);
    });
};

/**
 *
 * @name fetchColors
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} setColors sets state for colors
 * @param {*} setApiError sets error if response other than 200 is returned
 * @returns sets state for Brands if 200 response, else sets state for apiError
 */
const fetchColors = async (setColors, setApiError) => {
  await HttpHelper(Constants.PRODUCT_COLORS_ENDPOINT, 'GET')
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(Constants.API_ERROR);
    })
    .then(setColors)
    .catch(() => {
      setApiError(true);
    });
};

/**
 *
 * @name fetchMaterials
 * @description Utilizes HttpHelper to make a get request to an API
 * @param {*} setMaterials sets state for materials
 * @param {*} setApiError sets error if response other than 200 is returned
 * @returns sets state for Brands if 200 response, else sets state for apiError
 */
const fetchMaterials = async (setMaterials, setApiError) => {
  await HttpHelper(Constants.PRODUCT_MATERIALS_ENDPOINT, 'GET')
    .then((response) => {
      if (response.ok) {
        return response.json();
      }
      throw new Error(Constants.API_ERROR);
    })
    .then(setMaterials)
    .catch(() => {
      setApiError(true);
    });
};

const fetchProductFilters = async (
  setBrands, setCategories, setDemographics, setColors, setMaterials, setApiError
) => {
  fetchBrands(setBrands, setApiError);
  fetchCategories(setCategories, setApiError);
  fetchDemographics(setDemographics, setApiError);
  fetchColors(setColors, setApiError);
  fetchMaterials(setMaterials, setApiError);
};

export default fetchProductFilters;
