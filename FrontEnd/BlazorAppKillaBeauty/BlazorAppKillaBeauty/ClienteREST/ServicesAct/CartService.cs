using BlazorAppKillaBeauty.ClienteREST.Models;
using BlazorAppKillaBeauty.Components.Pages.Cliente;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class CartService
    {
        private readonly HttpClient http;

        private readonly JsonSerializerOptions jsonOptions = new()
        {
            PropertyNameCaseInsensitive = true,
            DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
        };

        public List<CartItem> Productos { get; private set; } = new();

        public int CantidadTotal => Productos.Sum(p => p.Cantidad);

        private readonly EscalaPrecioService escalaPrecioService;
        private Dictionary<int, List<EscalaPrecio>> escalasPorProducto = new();

        private async Task<List<EscalaPrecio>> ObtenerEscalasAsync(int idProducto)
        {
            if (!escalasPorProducto.ContainsKey(idProducto))
            {
                escalasPorProducto[idProducto] =
                    await escalaPrecioService.ListarPorProductoAsync(idProducto);
            }

            return escalasPorProducto[idProducto];
        }

        public async Task<decimal> ObtenerPrecioUnitarioAsync(CartItem item)
        {
            var escalas = await ObtenerEscalasAsync(item.Producto.Id);

            var escalaActual = escalas
                .Where(e => item.Cantidad >= e.CantidadMinima)
                .OrderByDescending(e => e.CantidadMinima)
                .FirstOrDefault();

            return escalaActual?.PrecioUnitario ?? item.Producto.PrecioBase;
        }

        public async Task<decimal> ObtenerSubtotalAsync(CartItem item)
        {
            var precio = await ObtenerPrecioUnitarioAsync(item);
            return precio * item.Cantidad;
        }

        public decimal Total { get; private set; } = 0;

        public decimal DescuentoTotal { get; private set; } = 0;
        public event Action? OnChange;
        private void NotifyStateChanged() => OnChange?.Invoke();

        public async Task RecalcularTotalesAsync()
        {
            Total = 0;
            DescuentoTotal = 0;

            foreach (var item in Productos)
            {
                if (item.Producto == null)
                    continue;

                var precioBase = item.Producto.PrecioBase;
                var precioEscala = await ObtenerPrecioUnitarioAsync(item);

                Total += precioEscala * item.Cantidad;

                if (precioEscala < precioBase)
                {
                    DescuentoTotal += (precioBase - precioEscala) * item.Cantidad;
                }
            }
        }

        public CartService(
            IHttpClientFactory httpClientFactory,
            EscalaPrecioService escalaPrecioService)
        {
            http = httpClientFactory.CreateClient("KillaApi");
            this.escalaPrecioService = escalaPrecioService;
        }

        public async Task CargarCarritoAsync(int idUsuario)
        {
            var carritos = await GetAsync<List<Carrito>>($"carrito/usuario/{idUsuario}");

            var carritoActual = carritos?
                .FirstOrDefault(c => c.Estado == "ACTIVO");

            Productos = carritoActual?.DetalleCarritoList ?? new();

            await ConsolidarProductosDuplicadosAsync();
            await RecalcularTotalesAsync();
            NotifyStateChanged();
        }

        public async Task<Carrito?> ObtenerPorIdAsync(int id)
        {
            return await GetAsync<Carrito>($"carrito/{id}");
        }

        public async Task<Carrito> CrearAsync(Carrito carrito)
        {
            using var response = await http.PostAsJsonAsync("carrito", carrito, jsonOptions);
            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<Carrito>(jsonOptions)
                   ?? carrito;
        }

        public async Task<Carrito> ActualizarAsync(Carrito carrito)
        {
            using var response = await http.PutAsJsonAsync($"carrito/{carrito.Id}", carrito, jsonOptions);
            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<Carrito>(jsonOptions)
                   ?? carrito;
        }

        public async Task EliminarAsync(int id)
        {
            using var response = await http.DeleteAsync($"carrito/{id}");
            await EnsureSuccessAsync(response);
        }

        /*public async Task VaciarCarritoAsync(int idUsuario)
        {
            var detalles = Productos.ToList();

            foreach (var detalle in detalles)
            {
                await EliminarDetalleAsync(detalle.Id);
            }

            Productos.Clear();
        }*/

        public async Task VaciarCarritoAsync(int idUsuario)
        {
            var detalles = Productos.ToList();

            foreach (var detalle in detalles)
            {
                if (detalle.Id > 0)
                {
                    try
                    {
                        await EliminarDetalleAsync(detalle.Id);
                    }
                    catch
                    {
                        // Si ya no existe en BD, lo ignoramos
                    }
                }
            }

            Productos.Clear();
            await RecalcularTotalesAsync();
        }

        private async Task<T?> GetAsync<T>(string url)
        {
            using var response = await http.GetAsync(url);

            if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                return default;

            await EnsureSuccessAsync(response);
            return await response.Content.ReadFromJsonAsync<T>(jsonOptions);
        }

        private static async Task EnsureSuccessAsync(HttpResponseMessage response)
        {
            if (response.IsSuccessStatusCode)
                return;

            var body = await response.Content.ReadAsStringAsync();

            throw new InvalidOperationException(
                string.IsNullOrWhiteSpace(body)
                    ? $"Error REST {(int)response.StatusCode} {response.ReasonPhrase}"
                    : body);
        }

        private ProductoCarrito ConvertirAProductoCarrito(Producto producto)
        {
            return new ProductoCarrito
            {
                Id = producto.Id,
                Nombre = producto.Nombre,
                Imagenes = producto.Imagenes,
                PrecioBase = producto.PrecioBase,
                Stock = producto.Stock 
            };
        }

        public async Task AgregarProductoAsync(Producto producto, int idUsuario, int cantidad)
        {
            await AgregarProductoPorIdAsync(producto.Id, idUsuario, cantidad);

            await CargarCarritoAsync(idUsuario);
        }

        public void AgregarProductoLocal(Producto producto, int cantidad)
        {
            var existente = Productos
                .FirstOrDefault(p => p.Producto.Id == producto.Id);

            if (existente != null)
            {
                existente.Cantidad += cantidad;
            }
            else
            {
                Productos.Add(new CartItem
                {
                    Producto = ConvertirAProductoCarrito(producto),
                    Cantidad = cantidad
                });
            }

            _ = RecalcularTotalesAsync();
        }

        private async Task<Carrito> ObtenerOCrearCarritoUsuarioAsync(int idUsuario)
        {
            var carritos = await GetAsync<List<Carrito>>($"carrito/usuario/{idUsuario}");

            var carritoActual = carritos?
                .FirstOrDefault(c => c.Estado == "ACTIVO");

            if (carritoActual != null)
                return carritoActual;

            var request = new
            {
                usuario = new
                {
                    id = idUsuario
                },
                estado = "ACTIVO"
            };

            using var response = await http.PostAsJsonAsync("carrito", request, jsonOptions);
            await EnsureSuccessAsync(response);

            return await response.Content.ReadFromJsonAsync<Carrito>(jsonOptions)
                   ?? throw new Exception("No se pudo crear el carrito.");
        }

        public async Task AgregarDetalleCarritoAsync(int idProducto, int idCarrito, int cantidad)
        {
            var request = new
            {
                cantidad = cantidad,
                producto = new
                {
                    id = idProducto
                },
                carritoDeCompras = new
                {
                    id = idCarrito
                }
            };

            var response = await http.PostAsJsonAsync(
                "detalle-carrito/agregar",
                request,
                jsonOptions);

            if (!response.IsSuccessStatusCode)
            {
                var error = await response.Content.ReadAsStringAsync();
                throw new Exception($"Error detalle carrito: {(int)response.StatusCode} - {error}");
            }
        }

        public async Task AgregarProductoPorIdAsync(int idProducto, int idUsuario, int cantidad)
        {
            var carrito = await ObtenerOCrearCarritoUsuarioAsync(idUsuario);

            var detalleExistente = carrito.DetalleCarritoList
                .FirstOrDefault(d => d.Producto?.Id == idProducto);

            if (detalleExistente != null)
            {
                await ActualizarCantidadDetalleAsync(
                    detalleExistente.Id,
                    detalleExistente.Cantidad + cantidad);

                return;
            }

            await AgregarDetalleCarritoAsync(idProducto, carrito.Id, cantidad);
        }

        public record InfoEscalaCarrito(
            decimal PrecioUnitario,
            decimal Ahorro,
            string Etiqueta,
            string SiguienteEscala
        );

        public async Task<InfoEscalaCarrito> ObtenerInfoEscalaAsync(CartItem item)
        {
            var escalas = await ObtenerEscalasAsync(item.Producto.Id);

            var escalaActual = escalas
                .Where(e => item.Cantidad >= e.CantidadMinima)
                .OrderByDescending(e => e.CantidadMinima)
                .FirstOrDefault();

            var siguienteEscala = escalas
                .Where(e => e.CantidadMinima > item.Cantidad)
                .OrderBy(e => e.CantidadMinima)
                .FirstOrDefault();

            var precio = escalaActual?.PrecioUnitario ?? item.Producto.PrecioBase;
            var ahorro = (item.Producto.PrecioBase - precio) * item.Cantidad;

            var etiqueta = NombreEscala(escalaActual?.CantidadMinima ?? 1);

            var siguiente = siguienteEscala == null
                ? ""
                : $"Agrega {siguienteEscala.CantidadMinima - item.Cantidad} mas para {NombreEscala(siguienteEscala.CantidadMinima)}";

            return new InfoEscalaCarrito(precio, ahorro, etiqueta, siguiente);
        }

        private string NombreEscala(int cantidad)
        {
            return cantidad <= 1
                ? "Unidad"
                : $"Desde {cantidad} unidades";
        }

        public async Task MigrarCarritoLocalAUsuarioAsync(int idUsuario)
        {
            var productosLocales = Productos
                .Where(d => d.Producto != null)
                .Select(d => new
                {
                    IdProducto = d.Producto!.Id,
                    Cantidad = d.Cantidad
                })
                .ToList();

            if (productosLocales.Count == 0)
            {
                await CargarCarritoAsync(idUsuario);
                return;
            }

            foreach (var item in productosLocales)
            {
                await AgregarProductoPorIdAsync(
                    item.IdProducto,
                    idUsuario,
                    item.Cantidad
                );
            }

            await CargarCarritoAsync(idUsuario);
        }

        public async Task AumentarCantidadAsync(int detalleId)
        {
            var detalle = Productos.FirstOrDefault(d => d.Id == detalleId);

            if (detalle == null)
                return;

            var nuevaCantidad = detalle.Cantidad + 1;

            await ActualizarCantidadDetalleAsync(detalleId, nuevaCantidad);

            detalle.Cantidad = nuevaCantidad;
            await RecalcularTotalesAsync();
        }

        public async Task DisminuirCantidadAsync(int detalleId)
        {
            var detalle = Productos.FirstOrDefault(d => d.Id == detalleId);

            if (detalle == null || detalle.Cantidad <= 1)
                return;

            var nuevaCantidad = detalle.Cantidad - 1;

            await ActualizarCantidadDetalleAsync(detalleId, nuevaCantidad);

            detalle.Cantidad = nuevaCantidad;
            await RecalcularTotalesAsync();
        }

        public async Task EliminarDetalleAsync(int detalleId)
        {
            using var response = await http.DeleteAsync($"detalle-carrito/{detalleId}");
            await EnsureSuccessAsync(response);

            var detalle = Productos.FirstOrDefault(d => d.Id == detalleId);

            if (detalle != null)
                Productos.Remove(detalle);

            await RecalcularTotalesAsync();
        }

        public async Task EliminarProductoLocalAsync(int idProducto)
        {
            var detalle = Productos.FirstOrDefault(d => d.Producto?.Id == idProducto);

            if (detalle != null)
                Productos.Remove(detalle);

            await RecalcularTotalesAsync();
        }

        public async Task AumentarCantidadProductoLocalAsync(int idProducto)
        {
            var detalle = Productos.FirstOrDefault(d => d.Producto?.Id == idProducto);

            if (detalle == null)
                return;

            detalle.Cantidad++;

            await RecalcularTotalesAsync();
        }

        public async Task DisminuirCantidadProductoLocalAsync(int idProducto)
        {
            var detalle = Productos.FirstOrDefault(d => d.Producto?.Id == idProducto);

            if (detalle == null || detalle.Cantidad <= 1)
                return;

            detalle.Cantidad--;

            await RecalcularTotalesAsync();
        }

        private async Task ActualizarCantidadDetalleAsync(int detalleId, int cantidad)
        {
            using var response = await http.PutAsync(
                $"detalle-carrito/{detalleId}/cantidad/{cantidad}",
                null);

            await EnsureSuccessAsync(response);
        }



        private async Task ConsolidarProductosDuplicadosAsync()
        {
            var gruposDuplicados = Productos
                .Where(d => d.Id > 0 && d.Producto != null)
                .GroupBy(d => d.Producto!.Id)
                .Where(g => g.Count() > 1)
                .ToList();

            foreach (var grupo in gruposDuplicados)
            {
                var detalles = grupo
                    .OrderBy(d => d.Id)
                    .ToList();

                var detallePrincipal = detalles.First();
                var cantidadTotal = detalles.Sum(d => d.Cantidad);

                await ActualizarCantidadDetalleAsync(detallePrincipal.Id, cantidadTotal);
                detallePrincipal.Cantidad = cantidadTotal;

                foreach (var detalleDuplicado in detalles.Skip(1))
                {
                    using var response = await http.DeleteAsync($"detalle-carrito/{detalleDuplicado.Id}");
                    await EnsureSuccessAsync(response);

                    Productos.Remove(detalleDuplicado);
                }
            }
        }
    }
}
