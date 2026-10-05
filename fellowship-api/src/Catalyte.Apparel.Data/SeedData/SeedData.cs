using Catalyte.Apparel.Data.Models;
using Catalyte.Apparel.Data.SeedData;
using Microsoft.EntityFrameworkCore;
using System;

namespace Catalyte.Apparel.Data.Context
{
    public static class Extensions
    {
        /// <summary>
        /// Produces a set of seed data to insert into the database on startup.
        /// </summary>
        /// <param name="modelBuilder">Used to build model base DbContext.</param>
        public static void SeedData(this ModelBuilder modelBuilder)
        {
            var productFactory = new ProductFactory();

            modelBuilder.Entity<Product>().HasData(productFactory.GenerateRandomProducts(200));

            var lineItem = new LineItem()
            {
                Id = 1,
                DateCreated = DateTime.UtcNow,
                DateModified = DateTime.UtcNow,
                ProductId = 1,
                Quantity = 1,
                PurchaseId = 1,
            };

            modelBuilder.Entity<LineItem>().HasData(lineItem);

            var purchase = new Purchase()
            {
                Id = 1,
                BillingCity = "Atlanta",
                BillingEmail = "customer@home.com",
                BillingPhone = "(714) 345-8765",
                BillingState = "GA",
                BillingStreet = "123 Main",
                BillingStreet2 = "Apt A",
                BillingZip = "31675",
                DateCreated = DateTime.UtcNow,
                DateModified = DateTime.UtcNow,
                DeliveryCity = "Birmingham",
                DeliveryState = "AL",
                DeliveryStreet = "123 Hickley",
                DeliveryZip = "43690",
                DeliveryFirstName = "Max",
                DeliveryLastName = "Space",
                CardHolder = "Max Perkins",
                CardNumber = "1435678998761234",
                Expiration = "11/21",
                CVV = "456",
                OrderDate = new DateTime(2021, 5, 4),
                SubTotal = 20.00m,
                Shipping = 5.00m,
                Total = 25.00m,
            };

            modelBuilder.Entity<Purchase>().HasData(purchase);

            var marvin = new User()
            {
                Id = 1,
                Email = "mphilippe@catalyte.io",
                Role = "customer",
                FirstName = "Marvin",
                LastName = "Philippe",
                street = "123 mordor street",
                street2 = "Apt.b",
                city = "Jersey city",
                state = "New Jersey",
                zip = "19367"
            };
            modelBuilder.Entity<User>().HasData(marvin);

        }
    }
}
