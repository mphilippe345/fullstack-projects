import { Box } from '@material-ui/core';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import React, { useState } from 'react';
import { toPrice } from '../../checkout-page/ReviewOrderWidgetService';

/**
 * @name PurchaseTableRow
 * @description creates the collapsible rows for line items
 * @return component
 */
const PurchasedTableRow = ({ userPurchase }) => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <TableRow>
        <TableCell style={{ textAlign: 'center' }}>{new Date(userPurchase.orderDate).toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' }).replaceAll('/', '-')}</TableCell>
        <TableCell style={{ textAlign: 'center' }}>
          {toPrice(userPurchase.total)}
        </TableCell>
        <TableCell style={{ textAlign: 'center' }}>
          <IconButton size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell
          align="right"
          colSpan={3}
          style={{
            paddingBottom: 0, paddingTop: 0
          }}
        >
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box style={{ float: 'right', padding: 0, columnSpan: 'inherit' }}>
              <Table size="small" style={{ columnSpan: 'inherit' }}>
                <TableHead>
                  <TableRow>
                    <TableCell>Item Name</TableCell>
                    <TableCell>Quantitiy</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {userPurchase.lineItems.map((lineItem) => (
                    <TableRow key={lineItem.productId}>
                      <TableCell>{lineItem.product.name}</TableCell>
                      <TableCell>{lineItem.quantity}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
};

export default PurchasedTableRow;
