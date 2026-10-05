import React from 'react';
import { unmountComponentAtNode } from 'react-dom';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom/cjs/react-router-dom.min';
import MaintenancePage from './MaintenancePage';
import fetchProducts from './MaintenancePageService';

jest.mock('./MaintenancePageService');
let container = null;

describe('MaintenancePage Component Tests', () => {
  beforeEach(() => {
    // setup a DOM element as a render target
    container = document.createElement('div');
    document.body.appendChild(container);
  });

  afterEach(() => {
    // cleanup on exiting
    unmountComponentAtNode(container);
    container.remove();
    container = null;
  });

  it('shows error msg text when an error is thrown', () => {
    fetchProducts.mockImplementation((setProducts, setApiError) => {
      setApiError(true);
    });
    render(
      <BrowserRouter><MaintenancePage /></BrowserRouter>, container
    );
    expect(screen.getByTestId('errMsg')).toHaveTextContent('Oops, something went wrong');
  });
});
