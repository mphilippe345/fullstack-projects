import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import getUserByEmail from '../profile-page/ProfilePageService';
import PurchasedHistoryTable from './purchase-history-table/PurchasedHistoryTable';

/**
 * @name PurchaseHistoryPage
 * @description fetches purchases from the API and stores them in a table
 * @return component
 */
const PurchasedHistoryPage = () => {
  const [setUser] = useState('');
  const history = useHistory();
  const userEmail = localStorage.getItem('userEmail');

  const isUserLoggedIn = () => {
    if (userEmail == null) {
      history.push('/');
    }
  };
  useEffect(() => {
    getUserByEmail(userEmail, setUser);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    isUserLoggedIn();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return (
    <div>
      <PurchasedHistoryTable />
    </div>
  );
};

export default PurchasedHistoryPage;
