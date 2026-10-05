import React, { useState } from 'react';
import '../toast/Toast.css';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { ToastContainer, toast, Slide } from 'react-toastify';
import ProductPage from '../product-page/ProductPage';
import CheckoutPage from '../checkout-page/CheckoutPage';
import ConfirmationPage from '../confirmation-page/ConfirmationPage';
import ToastPage from '../toast-page/ToastPage';
import MaintenancePage from '../maintenance-page/MaintenancePage';
import Header from '../header/Header';
import CreateProductPage from '../create-product-page/CreateProductPage';
import ProfilePage from '../profile-page/ProfilePage';
import CreatePromoPage from '../create-promo-page/CreatePromoPage';
import PurchasedHistoryPage from '../purchase-history-page/PurchasedHistoryPage';
import Footer from '../footer/Footer';

/**
 * @name App
 * @returns component
 */
const App = () => {
  const [user, setUser] = useState('');
  const [loginTracker, setLoginTracker] = useState(false);

  return (

    <BrowserRouter>
      <ToastContainer
        position={toast.POSITION.TOP_CENTER}
        autoClose={8000}
        transition={Slide}
        progressStyle={{ backgroundColor: 'white' }}
        pauseOnHover={false}
        closeOnClick={false}
      />
      <Header
        user={user}
        setUser={setUser}
        setLoginTracker={setLoginTracker}
      />
      <div className="pageBody">
        <Switch>
          <Route
            exact
            path="/"
            render={() => (
              <ProductPage
                user={user}
                loginTracker={loginTracker}
                setUser={setUser}
              />
            )}
          />
          <Route exact path="/checkout" render={() => <CheckoutPage />} />
          <Route exact path="/confirmation" render={() => <ConfirmationPage />} />
          <Route exact path="/toast" render={() => <ToastPage />} />
          <Route exact path="/maintenance" render={() => <MaintenancePage />} />
          <Route exact path="/create-promo" render={() => <CreatePromoPage />} />
          <Route exact path="/profile" render={() => <ProfilePage />} />
          <Route exact path="/purchase-history" render={() => <PurchasedHistoryPage />} />
          <Route exact path="/create-product" render={() => <CreateProductPage />} />
        </Switch>
      </div>
      <Footer />
    </BrowserRouter>
  );
};

export default App;
