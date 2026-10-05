using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Catalyte.Apparel.Providers.Interfaces;
using System.Threading.Tasks;
using Catalyte.Apparel.DTOs.PromoCode;
using Catalyte.Apparel.Data.Models;
using System.Collections.Generic;

namespace Catalyte.Apparel.API.Controllers
{
    /// <summary>
    /// The PromoCodeController exposes endpoints for PromoCode related actions.
    /// </summary>

    [ApiController]
    [Route("/promos")]
    public class PromoCodeController : ControllerBase
    {
        private readonly ILogger<PromoCodeController> _logger;
        private readonly IPromoCodeProvider _promoCodeProvider;
        private readonly IMapper _mapper;

        public PromoCodeController(
            ILogger<PromoCodeController> logger,
            IPromoCodeProvider promoCodeProvider,
            IMapper mapper)
        {
            _logger = logger;
            _mapper = mapper;
            _promoCodeProvider = promoCodeProvider;
        }

        [HttpPost]
        public async Task<ActionResult<PromoCodeDTO>> CreatePromoCodeAsync([FromBody] PromoCode promoCode)
        {
            _logger.LogInformation("Request received for CreatePromoAsync");

            var promo = await _promoCodeProvider.CreatePromoCodeAsync(promoCode);
            var promoCodeDTO = _mapper.Map<PromoCodeDTO>(promo);

            return Created("/promocode", promoCodeDTO);
        }

        [HttpGet("{title}")]
        public async Task<ActionResult<PromoCodeDTO>> GetPromoCodeByTitleAsync(string title)
        {
            _logger.LogInformation("Request received for GetPromoCodeByTitleAsync");

            var promoCode = await _promoCodeProvider.GetPromoCodeByTitleAsync(title);
            var promoCodeDTO = _mapper.Map<PromoCodeDTO>(promoCode);

            return Ok(promoCodeDTO);
        }
    }
}
