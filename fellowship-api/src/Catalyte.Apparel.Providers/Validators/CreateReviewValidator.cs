using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Catalyte.Apparel.Data.Models;
namespace Catalyte.Apparel.Providers
{
    public class ReviewValidator
    {
        /// <summary>
        /// Uses all review validation helper methods
        /// </summary>
        /// <param name="review">The current review trying to persist to the database</param>
        /// <returns>A list of exceptions for validation errors, if any</returns>
        public static List<Exception> ValidateReview(Review review)
        {
            var exceptions = new List<Exception>();
            ValidateComment(review, exceptions);
            ValidateRating(review, exceptions);
            ValidateEmail(review, exceptions);
            ValidateName(review, exceptions);
            ValidateProductID(review, exceptions);
            return exceptions;
        }

        /// <summary>
        /// Validates the comment within the review object
        /// </summary>
        /// <param name="review">A review object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateComment(Review review, List<Exception> exceptions)
        {
            if (review.Comment == null || review.Comment == string.Empty)
            {
                exceptions.Add(new ArgumentException("Comment is required"));
                return exceptions;
            }
            if (review.Comment.Length > 840)
            {
                exceptions.Add(new ArgumentException("Comment must contain less than 840 characters"));
                return exceptions;
            }
            if (review.Comment.Length < 30)
            {
                exceptions.Add(new ArgumentException("Comment must contain more than 30 characters"));
                return exceptions;
            }

            return exceptions;

        }

        /// <summary>
        /// Validates the rating within the review object
        /// </summary>
        /// <param name="review">A review object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateRating(Review review, List<Exception> exceptions)
        {
            if (review.Rating == null)
            {
                exceptions.Add(new ArgumentException("Rating is required"));
                return exceptions;
            }
            if (review.Rating < 0.5m || review.Rating > 5)
            {
                exceptions.Add(new ArgumentException("Rating must be a number from .5 to 5"));
                return exceptions;
            }
            if (review.Rating % .5m != 0)
            {
                exceptions.Add(new ArgumentException("Rating must be a multiple of .5"));
                return exceptions;
            }

            return exceptions;

        }

        /// <summary>
        /// Validates the email within the review object
        /// </summary>
        /// <param name="review">A review object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateEmail(Review review, List<Exception> exceptions)
        {
            if (review.Email == null)
            {
                exceptions.Add(new ArgumentException("Email is required"));
                return exceptions;
            }
            if (!Regex.Match(review.Email, @"^[^@\s]+@[^@\s]+\.[^@\s]+$").Success)
                exceptions.Add(new ArgumentException("Email must follow format: username123@domain.com"));
            return exceptions;
        }

        /// <summary>
        /// Validates the name within the review object
        /// </summary>
        /// <param name="review">A review object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateName(Review review, List<Exception> exceptions)
        {
            if (review.Name == null || review.Name == string.Empty)
                exceptions.Add(new ArgumentException("Name is required"));
            return exceptions;
        }

        /// <summary>
        /// Validates the product id within the review object
        /// </summary>
        /// <param name="review">A review object to be persisted to the database</param>
        /// <param name="exceptions">A list of exceptions</param>
        /// <returns>Returns the list of exceptions with an exception added if there is an error</returns>
        public static List<Exception> ValidateProductID(Review review, List<Exception> exceptions)
        {
            if (review.ProductId == null)
            {
                exceptions.Add(new ArgumentException("ProductId is required"));
                return exceptions;
            }
            if (review.ProductId < 1)
            {
                var errorMessage = new ArgumentException("Product with ID: " + review.ProductId + " not found");
                exceptions.Add(errorMessage);
                return exceptions;
            }

            return exceptions;

        }
    }
}