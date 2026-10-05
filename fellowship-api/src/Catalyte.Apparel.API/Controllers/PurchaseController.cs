using AutoMapper;
using Catalyte.Apparel.API.DTOMappings;
using System.Collections.Generic;
using System.Threading.Tasks;
using Catalyte.Apparel.DTOs.Purchases;
using Catalyte.Apparel.Providers.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace Catalyte.Apparel.API.Controllers
{
    /// <summary>
    /// The PurchasessController exposes endpoints for purchase related actions.
    /// </summary>
    [ApiController]
    [Route("/purchases")]
    public class PurchaseController : ControllerBase
    {
        private readonly ILogger<PurchaseController> _logger;
        private readonly IPurchaseProvider _purchaseProvider;
        private readonly IMapper _mapper;

        public PurchaseController(
            ILogger<PurchaseController> logger,
            IPurchaseProvider purchaseProvider,
            IMapper mapper
        )
        {
            _logger = logger;
            _purchaseProvider = purchaseProvider;
            _mapper = mapper;
        }

        [HttpGet]
       public ActionResult PurchaseEndpointError()
        {
            return NotFound("Email is required");
        }

        [HttpGet("{email}")]
        public async Task<ActionResult<List<PurchaseResponseDTO>>> GetPurchasesByEmailAsync(string email)
        {
            _logger.LogInformation("Request received for GetAllPurchasesAsync");

            var purchases = await _purchaseProvider.GetPurchasesByEmailAsync(email);
            var purchaseResponseDTOs = _mapper.MapPurchasesToPurchaseResponseDTOs(purchases);
 
            return Ok(purchaseResponseDTOs);
        }

        [HttpPost]
        public async Task<ActionResult<List<PurchaseResponseDTO>>> CreatePurchaseAsync([FromBody] PurchaseRequestDTO model)
        {
            _logger.LogInformation("Request received for CreatePurchase");

            var newPurchase = _mapper.MapCreatePurchaseDTOToPurchase(model);
            var savedPurchase = await _purchaseProvider.CreatePurchaseAsync(newPurchase);
            var purchaseResponseDTO = _mapper.MapPurchaseToPurchaseResponseDTO(savedPurchase);

            if (purchaseResponseDTO == null)
            {
                return NoContent();
            }

            return Created($"/purchases/", purchaseResponseDTO);
        }
    }
}
