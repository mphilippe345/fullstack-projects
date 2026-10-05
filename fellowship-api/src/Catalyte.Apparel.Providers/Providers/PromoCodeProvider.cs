using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Providers.Interfaces;
using Catalyte.Apparel.Utilities;
using Catalyte.Apparel.Utilities.HttpResponseExceptions;
using Microsoft.Extensions.Logging;

namespace Catalyte.Apparel.Providers.Providers
{
    /// <summary>
    /// This class provides the implementation of the IPromoCodeProvider interface, providing service methods for promos.
    /// </summary>
    public class PromoCodeProvider : IPromoCodeProvider
    {
        private readonly ILogger<PromoCodeProvider> _logger;
        private readonly IPromoCodeRepository _promoCodeRepository;

        public PromoCodeProvider(IPromoCodeRepository promocodeRepository, ILogger<PromoCodeProvider> logger)
        {
            _logger = logger;
            _promoCodeRepository = promocodeRepository;
        }


        /// <summary>
        /// Persists a promo code to the database given the provided title is not already in the database or null.
        /// </summary>
        /// <param name="newPromoCode">The promo code to persist.</param>
        /// <returns>The promo code.</returns>
        public async Task<PromoCode> CreatePromoCodeAsync(PromoCode newPromoCode)
        {
            
            if (newPromoCode.title == null)
            {
                _logger.LogError("Promo code must have a title field.");
                throw new BadRequestException("Promo code must have a title field");
            }

            // CHECK TO MAKE SURE THE PROMO CODE TITLE IS NOT TAKEN
            PromoCode existingPromoCode;

            try
            {
                existingPromoCode = await _promoCodeRepository.GetPromoCodeByTitleAsync(newPromoCode.title.ToUpper());
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (existingPromoCode != default)
            {
                _logger.LogError("Title is taken.");
                throw new ConflictException("Title is taken");
            }

            // SET DEFAULT ROLE TO CUSTOMER AND TIMESTAMP
            newPromoCode.Role = Constants.PROMOCODE;
            newPromoCode.title = newPromoCode.title.ToUpper();
            newPromoCode.DateCreated = DateTime.UtcNow;
            newPromoCode.DateModified = DateTime.UtcNow;

            PromoCode savedPromoCode;

            try
            {
                savedPromoCode = await _promoCodeRepository.CreatePromoCodeAsync(newPromoCode);
                _logger.LogInformation("Promo code saved.");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            return savedPromoCode;
        }

        public async Task<PromoCode> GetPromoCodeByTitleAsync(string title)
        {
            PromoCode promoCode;

            try
            {
                promoCode = await _promoCodeRepository.GetPromoCodeByTitleAsync(title.ToUpper());
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.Message);
                throw new ServiceUnavailableException("There was a problem connecting to the database.");
            }

            if (promoCode == default)
            {
                _logger.LogError($"Could not find promo code with title: {title}");
                throw new NotFoundException($"Could not find promo code with title: {title}");
            }

            return promoCode;
        }
    }
}
