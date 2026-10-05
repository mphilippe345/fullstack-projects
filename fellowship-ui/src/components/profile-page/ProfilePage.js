import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import EditIcon from '@material-ui/icons/Edit';
import { IconButton } from '@material-ui/core';
import { toast } from 'react-toastify';
// import { getUserByEmail, updateUser } from './ProfilePageService';
import getUserByEmail, { updateUser } from './ProfilePageService';
import styles from './ProfilePage.module.css';

import ProfileValidator from './ProfileValidator';

import getPurchasesByUserEmail from '../purchase-history-page/PurchasedHistoryPageService';

/**
 * Handles returning of the user profile information as well as updating it
 * @returns returns the information related to user profileHe
 */
const ProfilePage = () => {
  const [user, setUser] = useState({});
  const history = useHistory();
  const [editMode, setEditMode] = useState(false);
  const [firstName, setFirstName] = useState(user.firstName);
  const [lastName, setLastName] = useState(user.lastName);
  const [email, setEmail] = useState(user.email);
  const [street, setStreet] = useState(user.street);
  const [street2, setStreet2] = useState(user.street2);
  const [city, setCity] = useState(user.city);
  const [state, setState] = useState(user.state);
  const [zip, setZip] = useState(user.zip);
  const [errors, setErrors] = useState({});

  const [userPurchases, setUserPurchases] = useState([]);

  const userEmail = localStorage.getItem('userEmail');

  const isUserLoggedIn = () => {
    if (userEmail == null) {
      history.push('/');
    }
  };

  /**
   * When a save button is clicked, it updates the user information and redirects to
   * the profile information page as well as displays it.
   * @param {evt} event in action
   */
  const handleSaveClick = (evt) => {
    evt.preventDefault();
    const userToUpdate = {
      firstName, lastName, email, street, street2, city, state, zip
    };
    const errorMessages = ProfileValidator(userToUpdate);
    setErrors(errorMessages);
    if (Object.keys(errorMessages).length === 0) {
      updateUser(userToUpdate, setUser, user.id);
      toast.success('User information has successfully updated');
      setEditMode(false);
    } else {
      toast.error('There is invalid input.');
    }
  };

  /**
   * When cancel button is clicked, it does not update the user information
   */
  const handleCancelClick = () => {
    updateUser(user, setUser, user.id);
  };

  /**
   * When pencil icon is clicked, it displays editable profile page
   */
  const handleEditPencilClick = () => {
    setEditMode(!editMode);
  };

  useEffect(() => {
    getUserByEmail(userEmail, setUser);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    isUserLoggedIn();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    setFirstName(user.firstName);
    setLastName(user.lastName);
    setEmail(user.email);
    setStreet(user.street);
    setStreet2(user.street2);
    setCity(user.city);
    setState(user.state);
    setZip(user.zip);
  }, [
    user.city, user.email, user.firstName, user.lastName,
    user.state, user.street, user.street2, user.zip
  ]);

  useEffect(() => {
    getPurchasesByUserEmail(userEmail, setUserPurchases);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const redirect = () => {
    history.push('/purchase-history');
  };

  return (
    <>

      <div>
        {userPurchases.length === 0 ? (
          <></>
        ) : (
          <button onClick={redirect} type="button" className={styles.viewPurchaseHistory}>
            <text>View Purchase History</text>
          </button>
        )}
      </div>

      <div className={styles.ProfilePageContainer}>

        {editMode
          ? (
            <div className={styles.profileHolder}>
              <div className={styles.editPencilIcon}>
                <IconButton onClick={handleEditPencilClick}>
                  <EditIcon />
                </IconButton>
              </div>
              <h3 className={styles.title}>Profile</h3>
              <form>
                <label htmlFor="firstName" className={styles.editFirstName}>
                  <span className={styles.editFirstNameText}>First Name</span>
                  <input type="text" id="firstName" value={firstName} onChange={(e) => { setFirstName(e.target.value); }} />
                  <div className={errors.firstName ? styles.showErrors : styles.hideErrors}>
                    {errors.firstName ? errors.firstName : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="lastName" className={styles.editLastName}>
                  <span className={styles.editLastNameText}>Last Name</span>
                  <input type="text" id="lastName" value={lastName} onChange={(e) => { setLastName(e.target.value); }} />
                  <div className={errors.lastName ? styles.showErrors : styles.hideErrors}>
                    {errors.lastName ? errors.lastName : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="userEmail" className={styles.editEmail}>
                  <span className={styles.editEmailText}>Email</span>
                  <input type="text" id="userEmail" value={email} disabled="true" />
                  <div className={errors.email ? styles.showErrors : styles.hideErrors}>
                    {errors.email ? errors.email : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="street" className={styles.editStreet}>
                  <span className={styles.editStreetText}>Street</span>
                  <input type="text" id="street" value={street} onChange={(e) => { setStreet(e.target.value); }} />
                  <div className={errors.street ? styles.showErrors : styles.hideErrors}>
                    {errors.street ? errors.street : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="street2" className={styles.editStreet2}>
                  <span className={styles.editStreet2Text}>Street 2</span>
                  <input type="text" id="street2" value={street2} onChange={(e) => { setStreet2(e.target.value); }} />
                  <div className={errors.street2 ? styles.showErrors : styles.hideErrors}>
                    {errors.street2 ? errors.street2 : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="city" className={styles.editCity}>
                  <span className={styles.editCityText}>City</span>
                  <input type="text" id="city" value={city} onChange={(e) => { setCity(e.target.value); }} />
                  <div className={errors.city ? styles.showErrors : styles.hideErrors}>
                    {errors.city ? errors.city : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="state" className={styles.editState}>
                  <span className={styles.editStateText}>State</span>
                  <input type="text" id="state" value={state} onChange={(e) => { setState(e.target.value); }} />
                  <div className={errors.state ? styles.showErrors : styles.hideErrors}>
                    {errors.state ? errors.state : 'placeholder string for error message'}
                  </div>
                </label>
                <br />
                <label htmlFor="zip" className={styles.editZip}>
                  <span className={styles.editZipText}>Zip</span>
                  <input type="text" id="zip" value={zip} onChange={(e) => { setZip(e.target.value); }} />
                  <div className={errors.zip ? styles.showErrors : styles.hideErrors}>
                    {errors.zip ? errors.zip : 'placeholder string for error message'}
                  </div>
                </label>
                <div className={styles.buttons}>
                  <button type="submit" onClick={handleSaveClick} className={styles.save}>Save </button>
                  <button type="submit" onClick={handleCancelClick} className={styles.cancel}>Cancel </button>
                </div>
              </form>
            </div>
          )
          : (
            <div className={styles.profileHolder}>
              <div className={styles.editPencilIcon}>
                <IconButton onClick={handleEditPencilClick}>
                  <EditIcon />
                </IconButton>
              </div>
              <h3 className={styles.title}>Profile</h3>
              <label htmlFor="firstName" className={styles.firstName}>
                <font size="+2">First Name</font>
                <div className={styles.firstNameText}>
                  {user.firstName}
                </div>
              </label>
              <label htmlFor="lastName" className={styles.lastName}>
                <font size="+2">Last Name</font>
                <div className={styles.lastNameText}>
                  {user.lastName}
                </div>
              </label>
              <label htmlFor="email" className={styles.email}>
                <font size="+2">Email</font>
                <div className={styles.emailText}>
                  {user.email}
                </div>
              </label>
              <label htmlFor="street" className={styles.street}>
                <font size="+2">Street</font>
                <div className={styles.streetText}>
                  {user.street}
                </div>
              </label>
              <label htmlFor="street2" className={styles.street2}>
                <font size="+2">Street 2</font>
                <div className={styles.street2Text}>
                  {user.street2}
                </div>
              </label>
              <label htmlFor="city" className={styles.city}>
                <font size="+2">City</font>
                <div className={styles.cityText}>
                  {user.city}
                </div>
              </label>
              <label htmlFor="state" className={styles.state}>
                <font size="+2">State</font>
                <div className={styles.stateText}>
                  {user.state}
                </div>
              </label>
              <label htmlFor="zip" className={styles.zip}>
                <font size="+2">Zip</font>
                <div className={styles.zipText}>
                  {user.zip}
                </div>
              </label>
            </div>
          )}
      </div>

    </>

  );
};

export default ProfilePage;
