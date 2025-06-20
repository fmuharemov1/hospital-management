import React, { useState, useEffect } from 'react';
import './Reports.css';
import { Link, useNavigate } from 'react-router-dom';

export default function Reports() {
    const navigate = useNavigate();
    const [selectedReport, setSelectedReport] = useState('');
    const [reportData, setReportData] = useState([]);
    const [summary, setSummary] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // Novi state-ovi za filtriranje i sortiranje
    const [minPatients, setMinPatients] = useState('');
    const [minAmount, setMinAmount] = useState('');
    const [sortBy, setSortBy] = useState('');
    const [showFilters, setShowFilters] = useState(false);

    // MOCKUP PODACI
    const mockupData = {
        daily: [
            {
                id: 1,
                tipIzvjestaja: 'daily',
                datumGenerisanja: '2024-01-15',
                brojPacijenata: 25,
                brojTermina: 32,
                finansijskiPregled: 3500.50,
                osoblje: 'Dr. Marić, Sestra Ana'
            },
            {
                id: 2,
                tipIzvjestaja: 'daily',
                datumGenerisanja: '2024-01-16',
                brojPacijenata: 18,
                brojTermina: 24,
                finansijskiPregled: 2800.75,
                osoblje: 'Dr. Petrović, Sestra Milica'
            },
            {
                id: 3,
                tipIzvjestaja: 'daily',
                datumGenerisanja: '2024-01-17',
                brojPacijenata: 30,
                brojTermina: 38,
                finansijskiPregled: 4200.00,
                osoblje: 'Dr. Nikolić, Sestra Jovana'
            },
            {
                id: 4,
                tipIzvjestaja: 'daily',
                datumGenerisanja: '2024-01-18',
                brojPacijenata: 12,
                brojTermina: 15,
                finansijskiPregled: 1800.25,
                osoblje: 'Dr. Stojanović, Sestra Marija'
            },
            {
                id: 5,
                tipIzvjestaja: 'daily',
                datumGenerisanja: '2024-01-19',
                brojPacijenata: 35,
                brojTermina: 42,
                finansijskiPregled: 5100.80,
                osoblje: 'Dr. Jovanović, Sestra Petra'
            }
        ],
        weekly: [
            {
                id: 10,
                tipIzvjestaja: 'weekly',
                datumGenerisanja: '2024-01-15',
                brojPacijenata: 150,
                brojTermina: 190,
                finansijskiPregled: 22500.00,
                osoblje: 'Svi doktori, Sve sestre'
            },
            {
                id: 11,
                tipIzvjestaja: 'weekly',
                datumGenerisanja: '2024-01-22',
                brojPacijenata: 135,
                brojTermina: 168,
                finansijskiPregled: 19800.50,
                osoblje: 'Svi doktori, Sve sestre'
            },
            {
                id: 12,
                tipIzvjestaja: 'weekly',
                datumGenerisanja: '2024-01-29',
                brojPacijenata: 178,
                brojTermina: 215,
                finansijskiPregled: 28900.75,
                osoblje: 'Svi doktori, Sve sestre'
            },
            {
                id: 13,
                tipIzvjestaja: 'weekly',
                datumGenerisanja: '2024-02-05',
                brojPacijenata: 90,
                brojTermina: 112,
                finansijskiPregled: 15600.00,
                osoblje: 'Svi doktori, Sve sestre'
            }
        ],
        monthly: [
            {
                id: 20,
                tipIzvjestaja: 'monthly',
                datumGenerisanja: '2024-01-31',
                brojPacijenata: 650,
                brojTermina: 780,
                finansijskiPregled: 95000.00,
                osoblje: 'Kompletno osoblje'
            },
            {
                id: 21,
                tipIzvjestaja: 'monthly',
                datumGenerisanja: '2024-02-29',
                brojPacijenata: 580,
                brojTermina: 695,
                finansijskiPregled: 87500.25,
                osoblje: 'Kompletno osoblje'
            },
            {
                id: 22,
                tipIzvjestaja: 'monthly',
                datumGenerisanja: '2024-03-31',
                brojPacijenata: 720,
                brojTermina: 860,
                finansijskiPregled: 108000.50,
                osoblje: 'Kompletno osoblje'
            },
            {
                id: 23,
                tipIzvjestaja: 'monthly',
                datumGenerisanja: '2024-04-30',
                brojPacijenata: 480,
                brojTermina: 576,
                finansijskiPregled: 72000.00,
                osoblje: 'Kompletno osoblje'
            }
        ]
    };

    // Funkcija za filtriranje podataka
    const filterData = (data, minPatientsValue, minAmountValue) => {
        return data.filter(report => {
            const meetsPatientsCriteria = !minPatientsValue ||
                report.brojPacijenata >= parseInt(minPatientsValue);
            const meetsAmountCriteria = !minAmountValue ||
                report.finansijskiPregled >= parseFloat(minAmountValue);

            return meetsPatientsCriteria && meetsAmountCriteria;
        });
    };

    // Funkcija za sortiranje podataka
    const sortReportData = (data, sortOrder) => {
        if (!sortOrder) return data;

        return [...data].sort((a, b) => {
            const amountA = a.finansijskiPregled || 0;
            const amountB = b.finansijskiPregled || 0;

            if (sortOrder === 'asc') {
                return amountA - amountB;
            } else if (sortOrder === 'desc') {
                return amountB - amountA;
            }
            return 0;
        });
    };

    // Funkcija za "dohvatanje" mockup podataka
    const fetchMockupReports = async (reportType, minPatientsValue = '', minAmountValue = '') => {
        setIsLoading(true);
        setError(null);

        try {
            // Simulacija API poziva sa delay-om
            await new Promise(resolve => setTimeout(resolve, 800));

            // Uzmi podatke za odabrani tip izvještaja
            let data = mockupData[reportType] || [];

            // Filtriraj podatke
            data = filterData(data, minPatientsValue, minAmountValue);

            // Sortiraj podatke ako je potrebno
            const sortedData = sortReportData(data, sortBy);
            setReportData(sortedData);

            // Izračunaj summary podatke
            if (sortedData && sortedData.length > 0) {
                const totalPatients = sortedData.reduce((sum, report) => sum + (report.brojPacijenata || 0), 0);
                const totalAppointments = sortedData.reduce((sum, report) => sum + (report.brojTermina || 0), 0);
                const totalRevenue = sortedData.reduce((sum, report) => sum + (report.finansijskiPregled || 0), 0);

                setSummary({
                    patients: totalPatients,
                    appointments: totalAppointments,
                    revenue: totalRevenue.toFixed(2),
                    reportCount: sortedData.length
                });
            } else {
                setSummary({
                    patients: 0,
                    appointments: 0,
                    revenue: '0.00',
                    reportCount: 0
                });
            }
        } catch (error) {
            console.error("Error fetching mockup reports:", error);
            setError(`Failed to fetch reports: ${error.message}`);
            setReportData([]);
            setSummary(null);
        } finally {
            setIsLoading(false);
        }
    };

    // Handle report selection
    const handleReportChange = (e) => {
        const reportType = e.target.value;
        setSelectedReport(reportType);

        if (reportType) {
            fetchMockupReports(reportType, minPatients, minAmount);
        } else {
            setReportData([]);
            setSummary(null);
        }
    };

    // Handle filter changes
    const handleFilterChange = () => {
        if (selectedReport) {
            fetchMockupReports(selectedReport, minPatients, minAmount);
        }
    };

    // Handle sort change
    const handleSortChange = (e) => {
        const newSortBy = e.target.value;
        setSortBy(newSortBy);

        if (reportData.length > 0) {
            const sortedData = sortReportData(reportData, newSortBy);
            setReportData(sortedData);
        }
    };

    // Reset filters
    const handleResetFilters = () => {
        setMinPatients('');
        setMinAmount('');
        setSortBy('');

        if (selectedReport) {
            fetchMockupReports(selectedReport, '', '');
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="reports-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link">Invoices</Link>
                <Link to="/reports" className="nav-link active">Reports</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Log out</button>
            </nav>

            <h1>Hospital Reports</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            {/* Dodaj mockup indicator */}

            <div className="report-controls">
                <div className="report-selector">
                    <label htmlFor="report-type">Tip izvještaja:</label>
                    <select
                        id="report-type"
                        value={selectedReport}
                        onChange={handleReportChange}
                        disabled={isLoading}
                    >
                        <option value="">-- Odaberite tip --</option>
                        <option value="daily">Dnevni izvještaji</option>
                        <option value="weekly">Sedmični izvještaji</option>
                        <option value="monthly">Mjesečni izvještaji</option>
                    </select>
                </div>

                {selectedReport && (
                    <div className="filter-controls">
                        <button
                            onClick={() => setShowFilters(!showFilters)}
                            className="toggle-filters-btn"
                        >
                            {showFilters ? 'Sakrij filtere' : 'Prikaži filtere'}
                        </button>

                        {showFilters && (
                            <div className="filters-section">
                                <div className="filter-row">
                                    <div className="filter-group">
                                        <label htmlFor="min-patients">Min. broj pacijenata:</label>
                                        <input
                                            type="number"
                                            id="min-patients"
                                            value={minPatients}
                                            onChange={(e) => setMinPatients(e.target.value)}
                                            placeholder="npr. 20"
                                            min="0"
                                        />
                                    </div>

                                    <div className="filter-group">
                                        <label htmlFor="min-amount">Min. finansijski iznos (€):</label>
                                        <input
                                            type="number"
                                            id="min-amount"
                                            value={minAmount}
                                            onChange={(e) => setMinAmount(e.target.value)}
                                            placeholder="npr. 3000"
                                            min="0"
                                            step="0.01"
                                        />
                                    </div>

                                    <div className="filter-group">
                                        <label htmlFor="sort-by">Sortiraj po iznosu:</label>
                                        <select
                                            id="sort-by"
                                            value={sortBy}
                                            onChange={handleSortChange}
                                        >
                                            <option value="">-- Bez sortiranja --</option>
                                            <option value="desc">DESC</option>
                                            <option value="asc">ASC</option>
                                        </select>
                                    </div>
                                </div>


                                <div className="filter-buttons">
                                    <button
                                        onClick={handleFilterChange}
                                        className="apply-filters-btn"
                                        disabled={isLoading}
                                    >
                                        Primjeni filtere
                                    </button>
                                    <button
                                        onClick={handleResetFilters}
                                        className="reset-filters-btn"
                                        disabled={isLoading}
                                    >
                                        Resetuj filtere
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </div>

            {isLoading && <div className="loading">Učitavanje izvještaja...</div>}
            {error && <div className="error-message">{error}</div>}

            {summary && (
                <>
                    <div className="report-summary">
                        <div className="report-results">
                            <div className="report-card">
                                <h3>Ukupno pacijenata</h3>
                                <p>{summary.patients}</p>
                            </div>
                            <div className="report-card">
                                <h3>Ukupno termina</h3>
                                <p>{summary.appointments}</p>
                            </div>
                            <div className="report-card">
                                <h3>Ukupan prihod</h3>
                                <p>€{summary.revenue}</p>
                            </div>
                            <div className="report-card">
                                <h3>Broj izvještaja</h3>
                                <p>{summary.reportCount}</p>
                            </div>
                        </div>
                    </div>

                    <div className="detailed-reports">
                        <h3>Detaljni izvještaji ({reportData.length})</h3>
                        {reportData.length > 0 ? (
                            <table className="reports-table">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tip izvještaja</th>
                                    <th>Datum</th>
                                    <th>Broj pacijenata</th>
                                    <th>Broj termina</th>
                                    <th>
                                        Finansijski pregled (€)
                                        {sortBy && (
                                            <span className="sort-indicator">
                                                {sortBy === 'desc' ? ' ↓' : ' ↑'}
                                            </span>
                                        )}
                                    </th>

                                </tr>
                                </thead>
                                <tbody>
                                {reportData.map((report, index) => (
                                    <tr key={report.id || index}>
                                        <td>{report.id}</td>
                                        <td>
                                            <span className={`report-type-badge ${report.tipIzvjestaja}`}>
                                                {report.tipIzvjestaja === 'daily' ? 'Dnevni' :
                                                    report.tipIzvjestaja === 'weekly' ? 'Nedeljni' : 'Mesečni'}
                                            </span>
                                        </td>
                                        <td>{new Date(report.datumGenerisanja).toLocaleDateString('sr-RS')}</td>
                                        <td>{report.brojPacijenata}</td>
                                        <td>{report.brojTermina}</td>
                                        <td className="amount-cell">€{report.finansijskiPregled.toFixed(2)}</td>

                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <div className="no-reports-message">
                                <p>Nema dostupnih izvještaja za odabrane filtere.</p>
                                <small>Pokušajte sa drugačijim kriterijumima pretrage.</small>
                            </div>
                        )}
                    </div>
                </>
            )}

            {selectedReport && reportData.length === 0 && !summary && !isLoading && !error && (
                <div className="no-reports-message">
                    <p>Nema dostupnih izvještaja za odabrani tip i filtere.</p>
                    <small>Pokušajte sa drugačijim kriterijumima pretrage.</small>
                </div>
            )}
        </div>
    );
}