using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Catalyte.Apparel.Providers.Interfaces;
using System.Threading.Tasks;
using Catalyte.Apparel.DTOs.Reviews;
using Catalyte.Apparel.Data.Models;

namespace Catalyte.Apparel.API.Controllers
{
    /// <summary>
    /// The PromoCodeController exposes endpoints for PromoCode related actions.
    /// </summary>

    [ApiController]
    [Route("/reviews")]
    public class ReviewController : ControllerBase
    {
        private readonly ILogger<ReviewController> _logger;
        private readonly IReviewProvider _reviewProvider;
        private readonly IProductProvider _productProvider;
        private readonly IMapper _mapper;

        public ReviewController(
            ILogger<ReviewController> logger,
            IReviewProvider reviewProvider,
            IProductProvider productProvider,
            IMapper mapper)
        {
            _logger = logger;
            _mapper = mapper;
            _reviewProvider = reviewProvider;
            _productProvider = productProvider;
        }

        [HttpPost]
        public async Task<ActionResult<ReviewDTO>> CreateReviewAsync([FromBody] Review reviewToCreate)
        {
            _logger.LogInformation("Request received for CreateReviewAsync");

            var review = await _reviewProvider.CreateReviewAsync(reviewToCreate);
            var reviewDTO = _mapper.Map<Review>(review);

            return Created("/reviews", reviewDTO);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ReviewDTO>> GetReviewByIdAsync(int id)
        {
            _logger.LogInformation($"Request received for GetReviewsByIdAsync for id: {id}");

            var review = await _reviewProvider.GetReviewByIdAsync(id);
            var reviewDTO = _mapper.Map<ReviewDTO>(review);

            return Ok(reviewDTO);
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteReviewByIdAsync(int id)
        {
            _logger.LogInformation($"Request received for DeleteReviewByIdAsync for id: {id}");

            await _reviewProvider.DeleteReviewByIdAsync(id);

            return NoContent();
        }
    }
}
