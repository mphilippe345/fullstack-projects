using System;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;
using System.Collections.Generic;
using System.Linq;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IReviewProvider interface, providing service methods for reviews.
    /// </summary>
    public class ReviewProvider : IReviewProvider
    {
        private readonly ILogger<IReviewProvider> _logger;
        private readonly IReviewRepository _reviewRepository;

        public ReviewProvider(IReviewRepository reviewRepository, ILogger<ReviewProvider> logger)
        {
            _logger = logger;
            _reviewRepository = reviewRepository;
        }

        /// <summary>
        /// Persists a Review to the database 
        /// </summary>
        /// <param name="newReview">The review to persist.</param>
        /// <returns>The review</returns>
        public async Task<Review> CreateReviewAsync(Review newReview)
        {
            newReview.DateCreated = DateTime.UtcNow;
            newReview.DateModified = DateTime.UtcNow;

            Review savedReview;

            List<Exception> exceptions;
            exceptions = ReviewValidator.ValidateReview(newReview);
            if (exceptions != null && exceptions.Any())
            {
                var aggregateExceptions = new AggregateException("Invalid Review:", exceptions);
                throw new BadRequestException(aggregateExceptions.Message);
            }

            try
            {
                savedReview = await _reviewRepository.CreateReviewAsync(newReview);

            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return savedReview;
        }

        /// <summary>
        /// Asynchronously retrieves review based on the review id from the database.
        /// </summary>
        /// <returns>A review that matches the review id in the database.</returns>
        public async Task<Review> GetReviewByIdAsync(int id)
        {
            Review review;

            try
            {
                review = await _reviewRepository.GetReviewByIdAsync(id);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }
            if (review == null)
            {
                throw new NotFoundException("Review not found.");

            }

            return review;
        }

        /// <summary>
        /// Asynchronously retrieves a review based on the review id from the database, and then deletes it.
        /// </summary>
        /// <param name="id"> the review id </param>
        /// <returns> It does not return any thing </returns>
        /// <exception cref="ServiceUnavailableException"> The problem of connecting to the database </exception>
        public async Task DeleteReviewByIdAsync(int id)
        {
            Review review;

            review = await GetReviewByIdAsync(id);

            try
            {
                await _reviewRepository.DeleteReviewByIdAsync(review);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }
        }
    }
}