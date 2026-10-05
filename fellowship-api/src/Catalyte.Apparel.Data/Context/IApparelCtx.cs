using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Context
{
    /// <summary>
    /// This interface provides an abstraction layer for the apparel database context.
    /// </summary>
    public interface IApparelCtx
    {

        public DbSet<Product> Products { get; set; }

        public DbSet<LineItem> LineItems { get; set; }

        public DbSet<Purchase> Purchases { get; set; }

        public DbSet<User> Users { get; set; }

        public DbSet<PromoCode> PromoCodes { get; set; }

        public DbSet<Review> Reviews { get; set; }

        public DbSet<WishListItem> WishList { get; set; }

        Task<int> SaveChangesAsync(CancellationToken cancellationToken = new CancellationToken());
    }

}
