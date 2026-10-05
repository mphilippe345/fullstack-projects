using Catalyte.Apparel.Data.Models;
using Microsoft.EntityFrameworkCore;
using System.Threading;
using System.Threading.Tasks;

namespace Catalyte.Apparel.Data.Context
{
    /// <summary>
    /// Apparel database context provider.
    /// </summary>
    public class ApparelCtx : DbContext, IApparelCtx
    {

        public ApparelCtx(DbContextOptions<ApparelCtx> options) : base(options)
        { }

        public DbSet<Product> Products { get; set; }

        public DbSet<LineItem> LineItems { get; set; }

        public DbSet<Purchase> Purchases { get; set; }

        public DbSet<User> Users { get; set; }

        public DbSet<PromoCode> PromoCodes { get; set; }

        public DbSet<Review> Reviews { get; set; }

        public DbSet<WishListItem> WishList { get; set; }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.SeedData();

        }

        public override Task<int> SaveChangesAsync(CancellationToken cancellationToken = new CancellationToken())
        {
            return base.SaveChangesAsync(cancellationToken);
        }
    }
}
