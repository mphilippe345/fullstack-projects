import { Paper } from '@material-ui/core';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import React, { useState, useEffect } from 'react';
import getPurchasesByUserEmail from '../PurchasedHistoryPageService';
import PurchasedTableRow from './PurchasedHistoryTableRow';

/**
 * @name PurchaseHistoryTable
 * @description creates the basic table and calls in the collapsible rows
 * @return component
 */
const PurchasedHistoryTable = () => {
  const [userPurchases, setUserPurchases] = useState([]);
  const userEmail = localStorage.getItem('userEmail');

  useEffect(() => {
    getPurchasesByUserEmail(userEmail, setUserPurchases);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <TableContainer
      style={{
        height: '700px', width: '70%', margin: 'auto', marginTop: '10px'
      }}
      component={Paper}
    >
      <Table stickyHeader>
        <TableHead>
          <TableRow>
            <TableCell style={{
              minWidth: 200,
              backgroundColor: '#99078C',
              color: '#FFFFFF',
              textAlign: 'center'
            }}
            >
              Purchase Date
            </TableCell>
            <TableCell style={{
              minWidth: 200,
              backgroundColor: '#99078C',
              color: '#FFFFFF',
              textAlign: 'center'
            }}
            >
              Total Cost
            </TableCell>
            <TableCell style={{
              minWidth: 200,
              backgroundColor: '#99078C',
              color: '#FFFFFF',
              textAlign: 'center'
            }}
            >
              Items
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {userPurchases.map((userPurchase) => (
            <PurchasedTableRow
              key={userPurchase.id}
              userPurchase={userPurchase}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};
export default PurchasedHistoryTable;
