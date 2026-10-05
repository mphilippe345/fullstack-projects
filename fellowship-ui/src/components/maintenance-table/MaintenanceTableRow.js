import React from 'react';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import EditIcon from '@material-ui/icons/Edit';

/**
 * @name columnNames
 * @description this creates the columnNames that dicate what information goes in each column
 * @param {*} column information
 */
const columnNames = [
  { edit: 'edit' },
  { id: 'id' },
  { id: 'active', format: (value) => { if (value) return 'True'; return 'False'; } },
  { id: 'name' },
  { id: 'price', format: (value) => value.toFixed(2) },
  { id: 'quantity' },
  { id: 'sku' },
  { id: 'description' },
  { id: 'demographic' },
  { id: 'category' },
  { id: 'type' },
  { id: 'brand' },
  { id: 'material' },
  { id: 'imageSrc' },
  { id: 'primaryColorCode' },
  { id: 'secondaryColorCode' },
  { id: 'styleNumber' },
  { id: 'globalProductCode' },
  { id: 'dateCreated' },
  { id: 'dateModified' },
  { id: 'releaseDate' }
];

/**
 * @name MaintenanceTable
 * @description displays all products in a table sorted by ID
 * @param {*} props product
 * @return maintenance table
 */
const MaintenanceTableRow = ({ product, handleOpenModal }) => (

  <>
    <TableRow>
      {columnNames.map((column) => {
        const value = product[column.id];
        return (
          <>
            <TableCell
              key={column.id}
              align="justify"
            >
              {column.format && (typeof value === 'number' || typeof value === 'boolean')
                ? column.format(value)
                : value}
              {column.edit && <EditIcon onClick={() => handleOpenModal(product)} />}
            </TableCell>
          </>
        );
      })}
    </TableRow>

  </>

);

export default MaintenanceTableRow;
