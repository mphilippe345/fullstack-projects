import React from 'react';

const CartContext = React.createContext();

function cartReducer(state, action) {
  const stateCopied = { ...state };
  switch (action.type) {
    case 'delete': {
      return {
        ...state,
        products: state.products.filter((product) => product.title !== action.product.title)
      };
    }
    case 'add': {
      const stateValues = Object.values(stateCopied.products);
      const matchingProducts = stateCopied.products.filter((item) => item.id === action.product.id);
      if (matchingProducts.length > 0) {
        const itemsInStateValues = stateValues.filter((item) => item.id === matchingProducts[0].id);
        if (itemsInStateValues.includes(matchingProducts[0])) {
          stateCopied.products[stateCopied.products
            .findIndex(
              (item) => item.id === action.product.id
            )].quantity += action.product.quantity;
        }
        return {
          ...stateCopied,
          products: [...stateCopied.products]
        };
      }
      return {
        ...stateCopied,
        products: [...stateCopied.products, action.product]
      };
    }
    default: {
      throw new Error(`Unhandled action type: ${action.type}`);
    }
  }
}

function CartProvider({ children }) {
  const initialProducts = {
    products: [],
    setProducts: () => { }
  };
  const [state, dispatch] = React.useReducer(cartReducer, initialProducts);

  const value = { state, dispatch };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
}

function useCart() {
  const context = React.useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCartDispatch must be used within a CartProvider');
  }
  return context;
}

export { CartProvider, useCart };
