using BlazorAppKillaBeauty.ClienteREST.ServicesAct;
using BlazorAppKillaBeauty.ClienteREST.Utils.BlazorAppKillaBeauty.Utils;
using BlazorAppKillaBeauty.Components;
using BlazorAppKillaBeauty.Services;
using BlazorAppKillaBeauty.Security;
var builder = WebApplication.CreateBuilder(args);
// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();




//builder.Services.AddScoped(sp => new HttpClient
//{
//    BaseAddress = new Uri("http://localhost:8080/KillaREST-1.0-SNAPSHOT/services/")
//});



// SERVICE
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<CuponService>();
builder.Services.AddScoped<CampanaService>();
builder.Services.AddScoped<MarcaService>();
builder.Services.AddScoped<CategoriaService>();
builder.Services.AddScoped<PaisService>();
builder.Services.AddScoped<PedidoService>();
builder.Services.AddScoped<ReporteService>();
builder.Services.AddScoped<EscalaPrecioService>();
builder.Services.AddScoped<UsuarioService>();
builder.Services.AddScoped<UsuarioPerfilService>();
builder.Services.AddScoped<UsuarioPasswordService>();
builder.Services.AddScoped<CartService>();
builder.Services.AddScoped<UbigeoService>();
builder.Services.AddHttpClient("KillaApi", client =>
{
    var baseUrl = builder.Configuration["KillaApi:BaseUrl"];

    if (string.IsNullOrWhiteSpace(baseUrl))
    {
        throw new InvalidOperationException("No se configuro KillaApi:BaseUrl en appsettings.json");
    }

    client.BaseAddress = new Uri(baseUrl);
    client.Timeout = TimeSpan.FromSeconds(30);
});


builder.Services.AddScoped<HttpClientUtils>(sp =>
{
    var factory = sp.GetRequiredService<IHttpClientFactory>();
    return new HttpClientUtils(factory.CreateClient("KillaApi"));
});
builder.Services.AddScoped<AddressService>();
builder.Services.AddScoped<ProductoService>();
builder.Services.AddScoped<CourierService>();

builder.Services.AddCookieAuth();
builder.Services.AddAuthorization();
builder.Services.AddCascadingAuthenticationState();


var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();



//te identifica
app.UseAuthentication();
//revisa permisos 
app.UseAuthorization();






app.UseAntiforgery();

app.MapAuthEndpoints();
app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();

