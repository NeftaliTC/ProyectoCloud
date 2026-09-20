window.killaCharts = (function () {
    // Guardamos instancias para poder actualizar sin duplicar charts
    const charts = {};

    function createOrUpdateLineChart(canvasId, labels, data, label) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        // si ya existe, destruimos y recreamos (simple, robusto)
        if (charts[canvasId]) {
            charts[canvasId].destroy();
        }

        charts[canvasId] = new Chart(ctx, {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    label: label || 'Ventas',
                    data,
                    borderColor: '#e8607a',
                    backgroundColor: 'rgba(232, 96, 122, 0.16)',
                    fill: true,
                    tension: 0.35,
                    pointRadius: 3,
                    pointBackgroundColor: '#e8607a',
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: { enabled: true }
                },
                scales: {
                    x: { grid: { color: 'rgba(0,0,0,0.05)' } },
                    y: { grid: { color: 'rgba(0,0,0,0.05)' } }
                }
            }
        });
    }

    function createOrUpdateBarChart(canvasId, labels, data, label) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        if (charts[canvasId]) {
            charts[canvasId].destroy();
        }

        charts[canvasId] = new Chart(ctx, {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: label || 'Comparación',
                    data,
                    backgroundColor: ['#7aa6b8', '#0b79d0', '#0b79d0', '#0b79d0'],
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    x: { grid: { display: false } },
                    y: { grid: { color: 'rgba(0,0,0,0.05)' } }
                }
            }
        });
    }

    function createOrUpdateDonutChart(canvasId, labels, data) {
        const ctx = document.getElementById(canvasId);
        if (!ctx) return;

        if (charts[canvasId]) {
            charts[canvasId].destroy();
        }

        charts[canvasId] = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels,
                datasets: [{
                    data,
                    backgroundColor: ['#3b82f6', '#93c5fd', '#64748b', '#cbd5e1'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '62%',
                plugins: {
                    legend: { position: 'right' }
                }
            }
        });
    }

    return {
        createOrUpdateLineChart,
        createOrUpdateBarChart,
        createOrUpdateDonutChart
    };
})();