/* eslint-disable react-hooks/exhaustive-deps */
import React, { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import Rating from '@mui/material/Rating';
import Divider from '@mui/material/Divider';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { Close } from '@material-ui/icons';

const style = {
  modal: {
    bgcolor: '#f6e4f2',
    boxShadow: 24,
    borderRadius: '4px',
    height: '85vh',
    left: '50%',
    maxHeight: '85vh',
    maxWidth: '40vw',
    minHeight: '85vh',
    minWidth: '40vw',
    overflowX: 'hidden',
    overflowY: 'auto',
    position: 'absolute',
    p: 4,
    top: '50%',
    transform: 'translate(-50%, -50%)',
    width: '40vw',
    '&::-webkit-scrollbar': {
      width: 15,
      borderRadius: 7
    },
    '&::-webkit-scrollbar-track': {
      border: '1px grey',
      borderTopRightRadius: 7
    },
    '&::-webkit-scrollbar-thumb': {
      backgroundColor: 'darkgrey',
      borderTopRightRadius: '4px',
      borderBottomRightRadius: '4px'
    }
  },
  header: {
    display: 'block',
    padding: '12px 0',
    marginTop: '-30px',
    textAlign: 'center'
  },
  close: {
    display: 'block',
    fonSize: '30px',
    marginLeft: '38vw',
    marginTop: '-30px',
    paddingBottom: '5px'
  },
  button: {
    color: '#2f4858',
    marginLeft: '36vw',
    padding: '10px'
  },
  content: {
    display: 'block',
    padding: '12px 0',
    width: 'auto'
  },
  rating: {
    padding: '3px'
  },
  name: {
    display: 'block',
    marginRight: '95px',
    marginTop: '-28px',
    textAlign: 'end',
    verticalAlign: 'top'
  },
  date: {
    display: 'block',
    textAlign: 'end',
    marginTop: '-24px'
  },
  comment: {
    marginTop: '25px',
    width: 'auto'
  }
};

/**
 * @name ReviewModal
 * @description displays review modal, containing reviews for the product clicked
 * @param open view state of modal
 * @param handleClose function to close modal when user clicks outside
 * @param clickedProduct product that was clicked
 * @returns component
 */
const ReviewModal = ({ reviewOpen, handleReviewClose, clickedProduct }) => {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    setReviews(clickedProduct.reviews);
  });

  const [sort, setSort] = useState(false);

  const sortNewsestToOldest = () => {
    const sortedReviews = reviews.sort((a, b) => (
      new Date(a.dateCreated) > new Date(b.dateCreated) ? 1 : -1));
    setReviews(sortedReviews);
  };

  const sortOldestToNewest = () => {
    const sortedReviews = reviews.sort((a, b) => (
      new Date(b.dateCreated) > new Date(a.dateCreated) ? 1 : -1));
    setReviews(sortedReviews);
  };

  const determineSort = sort ? sortNewsestToOldest : sortOldestToNewest;

  const handleSort = () => {
    determineSort();
    setSort(!sort);
  };

  return (
    <div>
      <Modal
        open={reviewOpen}
        onClose={handleReviewClose}
      >
        <Box sx={style.modal}>
          <div sx={style.modalBody}>
            <div>
              <IconButton sx={style.close}>
                <Close onClick={handleReviewClose} />
              </IconButton>
              <Typography sx={style.header} variant="h5">
                Reviews for
                {' '}
                {clickedProduct.name}
              </Typography>
              <Button sx={style.button} onClick={handleSort}>Sort</Button>
            </div>
            <Divider />
            {reviews && reviews.map((review) => (
              <div sx={style.content}>
                <Rating
                  value={review.rating}
                  precision={0.5}
                  readOnly
                  sx={{
                    ...style.rating,
                    '& .MuiRating-iconFilled': {
                      color: '#f6ae2d'
                    },
                    '& .MuiRating-iconEmpty': {
                      color: '#f6ae2d'
                    }
                  }}
                />
                <Typography sx={style.name}>{review.name}</Typography>
                <Typography sx={style.date}>
                  {new Date(review.dateCreated).toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' }).replaceAll('/', '-')}
                </Typography>
                <Typography sx={style.comment}>{review.comment}</Typography>
                <Divider />
              </div>
            ))}
          </div>
        </Box>
      </Modal>
    </div>
  );
};

export default ReviewModal;
