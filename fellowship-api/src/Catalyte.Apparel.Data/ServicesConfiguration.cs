using Catalyte.Apparel.Data.Context;
using Catalyte.Apparel.Data.Interfaces;
using Catalyte.Apparel.Data.Repositories;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using System;

namespace Catalyte.Apparel.Data
{
    /// <summary>
    /// This class provides configuration options for services and uses
    /// appsettings.[current_environment].json file to configure context.
    /// </summary>
    public static class ServicesConfiguration
    {
        public static IServiceCollection AddDataServices(this IServiceCollection services, IConfiguration config)
        {

            services.AddDbContext<ApparelCtx>(options =>
            {
                if (config.GetValue<bool>("IsDevelopment"))
                {
                    options.UseNpgsql(config.GetConnectionString("CatalyteApparel"));
                }
                else
                {
                    options.UseNpgsql(ParseHerokuConnectionString());
                }
            });

            services.AddScoped<IApparelCtx>(provider => provider.GetService<ApparelCtx>());
            services.AddScoped<IProductRepository, ProductRepository>();
            services.AddScoped<IPurchaseRepository, PurchaseRepository>();
            services.AddScoped<IUserRepository, UserRepository>();
            services.AddScoped<IPromoCodeRepository, PromoCodeRepository>();
            services.AddScoped<IReviewRepository, ReviewRepository>();
            services.AddScoped<IReviewRepository, ReviewRepository>();
            services.AddScoped<IWishListRepository, WishListRepository>();

            return services;

        }

        private static string ParseHerokuConnectionString()
        {
            // Heroku provides PostgreSQL connection URL via env variable
            var connectionUrl = Environment.GetEnvironmentVariable("DATABASE_URL");

            // Parse connection URL to connection string for Npgsql
            connectionUrl = connectionUrl.Replace("postgres://", string.Empty);

            var pgUserPass = connectionUrl.Split("@")[0];
            var pgHostPortDb = connectionUrl.Split("@")[1];
            var pgHostPort = pgHostPortDb.Split("/")[0];

            var pgDb = pgHostPortDb.Split("/")[1];
            var pgUser = pgUserPass.Split(":")[0];
            var pgPass = pgUserPass.Split(":")[1];
            var pgHost = pgHostPort.Split(":")[0];
            var pgPort = pgHostPort.Split(":")[1];

            return $"Server={pgHost};Port={pgPort};User Id={pgUser};Password={pgPass};Database={pgDb}; SSL Mode=Require; TrustServerCertificate=true";
        }
    }

}
