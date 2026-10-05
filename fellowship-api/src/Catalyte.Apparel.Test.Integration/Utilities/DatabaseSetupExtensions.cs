using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.SeedData;

namespace Catalyte.Apparel.Test.Integration.Utilities
{
    public static class DatabaseSetupExtensions
    {
        public static void InitializeDatabaseForTests(this ApparelCtx context)
        {
            var productFactory = new ProductFactory();
            var products = productFactory.GenerateRandomProducts(100);

            context.Products.AddRange(products);
            context.SaveChanges();
        }

        public static void ReinitializeDatabaseForTests(this ApparelCtx context)
        {
            context.Products.RemoveRange(context.Products);
            context.InitializeDatabaseForTests();
        }
    }
}
