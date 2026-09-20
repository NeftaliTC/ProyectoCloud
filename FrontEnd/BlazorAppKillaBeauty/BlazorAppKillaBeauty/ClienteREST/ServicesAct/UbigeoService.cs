using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Components;
using BlazorAppKillaBeauty.ClienteREST.Models;

namespace BlazorAppKillaBeauty.ClienteREST.ServicesAct
{
    public class UbigeoService
    {
        private readonly HttpClient _httpClient;
        private readonly NavigationManager _navigationManager;
        private List<UbigeoModel> _ubigeos = new();

        public UbigeoService(HttpClient httpClient, NavigationManager navigationManager)
        {
            _httpClient = httpClient;
            _navigationManager = navigationManager;
        }

        public async Task InitializeAsync()
        {
            if (_ubigeos.Any()) return;

            try
            {
                var absoluteUrl = _navigationManager.ToAbsoluteUri("data/geodir-ubigeo-inei.csv").AbsoluteUri;

                var csvContent = await _httpClient.GetStringAsync(absoluteUrl);

                using var reader = new StringReader(csvContent);
                string? line = await reader.ReadLineAsync(); // Saltar cabecera

                while ((line = await reader.ReadLineAsync()) != null)
                {
                    if (string.IsNullOrWhiteSpace(line)) continue;

                    var columns = line.Split(',');
                    if (columns.Length >= 4)
                    {
                        _ubigeos.Add(new UbigeoModel
                        {
                            Ubigeo = columns[0].Trim(),
                            Distrito = columns[1].Trim(),
                            Provincia = columns[2].Trim(),
                            Departamento = columns[3].Trim()
                        });
                    }
                }
            }
            catch (Exception ex)
            {
                // Verás el error exacto si falta el archivo en wwwroot/data/
                Console.WriteLine($"Error crítico al procesar el archivo de ubigeos: {ex.Message}");
            }
        }

        public List<string> GetDepartamentos() =>
            _ubigeos.Select(u => u.Departamento).Distinct().OrderBy(d => d).ToList();

        public List<string> GetProvincias(string departamento) =>
            _ubigeos.Where(u => u.Departamento == departamento)
                    .Select(u => u.Provincia).Distinct().OrderBy(p => p).ToList();

        public List<string> GetDistritos(string departamento, string provincia) =>
            _ubigeos.Where(u => u.Departamento == departamento && u.Provincia == provincia)
                    .Select(u => u.Distrito).Distinct().OrderBy(d => d).ToList();

        public UbigeoModel? GetUbigeoCompleto(string depto, string prov, string dist) =>
            _ubigeos.FirstOrDefault(u => u.Departamento == depto && u.Provincia == prov && u.Distrito == dist);
    }
}
