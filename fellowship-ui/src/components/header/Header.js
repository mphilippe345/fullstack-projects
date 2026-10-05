import React, { useState, useEffect } from 'react';
import { NavLink, useHistory, useLocation } from 'react-router-dom';
import GoogleLogin, { GoogleLogout } from 'react-google-login';
import { ShoppingCartOutlined, AccountCircle } from '@material-ui/icons';
import Badge from '@material-ui/core/Badge';
import { useCart } from '../checkout-page/CartContext';
import loginUser from './HeaderService';
import constants from '../../utils/constants';
import fellowShipLogo from '../../utils/Images/Fellowshiplogo.png';
import styles from './Header.module.css';
import getUserByEmail from '../profile-page/ProfilePageService';

/**
 * @name Header
 * @description Displays the navigation header
 * @return component
 */
const Header = ({ user, setUser, setLoginTracker }) => {
  const [googleError, setGoogleError] = useState('');
  const [apiError, setApiError] = useState(false);

  const {
    state: { products }
  } = useCart();
  const history = useHistory();
  const location = useLocation();
  /**
   * @name handleGoogleLoginSuccess
   * @description Function to run if google login was successful
   * @param {Object} response Response object from google
   */
  const handleGoogleLoginSuccess = (response) => {
    sessionStorage.setItem('token', response.getAuthResponse().id_token);
    const googleUser = {
      email: response.profileObj.email,
      firstName: response.profileObj.givenName,
      lastName: response.profileObj.familyName
    };
    localStorage.setItem('userEmail', googleUser.email);
    localStorage.removeItem('logOut');
    loginUser(googleUser, setUser, setApiError);
    setGoogleError('');
    setLoginTracker(true);
  };
  /**
   * @name handleGoogleLoginSuccess
   * @description Function to run if google login was unsuccessful
   */
  const handleGoogleLoginFailure = () => {
    setGoogleError('There was a problem logging in with Google. Please wait and try again later.');
  };
  /**
   * @name handleGoogleLogoutSuccess
   * @description Function to run if google logout was successful
   */
  const handleGoogleLogoutSuccess = () => {
    setUser('');
    setGoogleError('');
    setLoginTracker(false);
    localStorage.clear();
    if (location.pathname === '/profile') {
      history.push('/');
    }
  };
  /**
   * @name handleGoogleLogoutFailure
   * @description Function to run if google logout was unsuccessful
   */
  const handleGoogleLogoutFailure = () => {
    setGoogleError('There was a problem logging out with Google. Please wait and try again later.');
  };

  /**
   * useEffect to get user by email.
   */
  useEffect(() => {
    getUserByEmail(localStorage.getItem('userEmail'));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  return (
    <div className={styles.Header}>
      <NavLink to="/home">
        <img src={fellowShipLogo} alt="Home" id={styles.Headerlogo} className={styles.Header} />
      </NavLink>
      <NavLink to="/checkout">
        <div id={styles.Carticon} className={styles.Header}>
          <Badge badgeContent={products.length} color="secondary">
            <ShoppingCartOutlined alt="cart" />
          </Badge>
        </div>
      </NavLink>
      {user && <span className={styles.Buttontext}>{user.firstName}</span>}
      {user && <span className={styles.Buttontext}>{user.lastName}</span>}
      {googleError && <span>{googleError}</span>}
      {apiError && <span>Api Error</span>}
      {!user ? (
        <GoogleLogin
          className={styles.Loginbutton}
          clientId={constants.GOOGLE_CLIENT_ID}
          buttonText="Login"
          onSuccess={handleGoogleLoginSuccess}
          onFailure={handleGoogleLoginFailure}
          cookiePolicy="single_host_origin"
        />
      ) : (
        [
          <span>
            <GoogleLogout
              className={styles.Logoutbutton}
              clientId={constants.GOOGLE_CLIENT_ID}
              buttonText="Logout"
              onLogoutSuccess={handleGoogleLogoutSuccess}
              onFailure={handleGoogleLogoutFailure}
            />

          </span>, <NavLink to="/profile"><span><AccountCircle id={styles.Accountcircle} /></span></NavLink>
        ]
      )}
    </div>
  );
};
export default Header;
