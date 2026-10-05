import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/app/App';
import { CartProvider } from './components/checkout-page/CartContext';

ReactDOM.render(
  <React.StrictMode>
    <CartProvider>
      <App />
    </CartProvider>
  </React.StrictMode>,
  document.getElementById('root')
);
