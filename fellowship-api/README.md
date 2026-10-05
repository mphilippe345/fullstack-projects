# .Net Sports Apparel API

## Getting Started

### Start the Server

- Click the drop down arrow next to the App Runner button and select `Apparel.Catalyte.API`
- Click Build > Build Solution
- Click the App Runner button

### Connections

By default, this service starts up on port 8085 and accepts cross-origin requests from `*`.

### Dependencies

#### .Net Runtime

You must have a .Net runtime installed on your machine.

#### Postgres

This server requires that you have Postgres installed and running on the default Postgres port of 5432. It requires that you have a database created on the server with the name of `postgres`
- Your username should be `postgres`
- Your password should be `root`

## Other notes
I hope this document will give you some insights as to how and why I choose this application architecture.

I have needed to and chosen to, use a few tools in this application to make it easier to develop and maintain.  Some of the tools are used at runtime and a couple of them are used during development.  The tools used for development are Visual Studio extensions and can be used with any project.
## Visual Studio Extensions

#### Markdown Editor
This extension let's you edit and preview .md files.
https://marketplace.visualstudio.com/items?itemName=MadsKristensen.MarkdownEditor

#### Add New File
By using this extension you can quickly create new files without having to right-click or go through the menu.  You will see that by using proper naming conventions the correct type of file will be created prepopulated with great content.
https://marketplace.visualstudio.com/items?itemName=MadsKristensen.AddNewFile

## NuGet Packages

This is some helpful documentation explaing the NuGet package manager.
https://docs.microsoft.com/en-us/nuget/consume-packages/install-use-packages-powershell

#### Google Authenticator
https://www.nuget.org/packages/GoogleAuthenticator/

#### Entity Framework Core
This is the heart of the Entity Framework tools.
`Install-Package Microsoft.EntityFrameworkCore -Version 5.0.10`
https://www.nuget.org/packages/Microsoft.EntityFrameworkCore/6.0.0-rc.1.21452.10

#### Entity Framework Core Design
Used for creating EF migrations
`Install-Package Microsoft.EntityFrameworkCore.Design -Version 5.0.10`

#### Swashbuckle
Also known as Swagger, this tool creates an interface for the API when you run the application.  You can use markup in the controllers to show documentation on the interface.  It is preinstalled with basic configuration (Startup.cs) when creating a new API with the newest versions of Visual Studio.
https://docs.microsoft.com/en-us/aspnet/core/tutorials/getting-started-with-swashbuckle?view=aspnetcore-5.0&tabs=visual-studio


 