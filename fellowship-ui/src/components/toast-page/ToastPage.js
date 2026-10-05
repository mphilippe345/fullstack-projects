import React from 'react';
import { toast } from 'react-toastify';
/**
 * @name ProductPage
 * @description fetches products from API and displays products as product cards
 * @return component
 */
const ToastPage = () => {
  const notify = () => {
    toast.success('Success');
    toast.error('Error');
    toast.info('Info');
  };
  return (
    <div>
      <button type="button" style={{ position: 'absolute', right: '660px', top: '300px' }} onClick={notify}>click me!</button>
    </div>
  );
};

export default ToastPage;
