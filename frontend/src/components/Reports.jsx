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

    // Dohvati JWT token
    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // Funkcija za dohvatanje izvještaja po tipu
    const fetchReportsByType = async (reportType) => {
        setIsLoading(true);
        setError(null);

        const token = getAuthToken();
        if (!token) {
            setError("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8085/api/client/reports/${reportType}`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to fetch ${reportType} reports: ${response.status} - ${errorText}`);
            }

            const data = await response.json();
            setReportData(data);

            // Izračunaj summary podatke
            if (data && data.length > 0) {
                const totalPatients = data.reduce((sum, report) => sum + (report.brojPacijenata || 0), 0);
                const totalAppointments = data.reduce((sum, report) => sum + (report.brojTermina || 0), 0);
                const totalRevenue = data.reduce((sum, report) => sum + (report.finansijskiPregled || 0), 0);

                setSummary({
                    patients: totalPatients,
                    appointments: totalAppointments,
                    revenue: totalRevenue.toFixed(2),
                    reportCount: data.length
                });
            } else {
                setSummary({
                    patients: 0,
                    appointments: 0,
                    revenue: '0.00',
                    reportCount: 0
                });
            }
        } catch (err) {
            console.error(`Error fetching ${reportType} reports:`, err);
            setError(`Error loading ${reportType} reports: ${err.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    // Handle report selection
    const handleReportChange = (e) => {
        const reportType = e.target.value;
        setSelectedReport(reportType);

        if (reportType) {
            fetchReportsByType(reportType);
        } else {
            setReportData([]);
            setSummary(null);
        }
    };

    // Funkcija za odjavu
    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <div className="reports-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link">Invoices</Link>
                <Link to="/reports" className="nav-link active">Reports</Link>
                <Link to="/emr" className="nav-link">EMR</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Log out</button>
            </nav>

            <h1>Hospital Reports</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            <div className="report-selector">
                <label htmlFor="report-type">Select report type:</label>
                <select
                    id="report-type"
                    value={selectedReport}
                    onChange={handleReportChange}
                    disabled={isLoading}
                >
                    <option value="">-- Choose --</option>
                    <option value="daily">Daily Reports</option>
                    <option value="weekly">Weekly Reports</option>
                    <option value="monthly">Monthly Reports</option>
                </select>
            </div>

            {isLoading && <div className="loading">Loading reports...</div>}

            {error && <div className="error-message">{error}</div>}

            {summary && !isLoading && (
                <>
                    <div className="report-summary">
                        <h2>Summary for {selectedReport} reports ({summary.reportCount} reports)</h2>
                        <div className="report-results">
                            <div className="report-card">
                                <h3>Total Patients</h3>
                                <p>{summary.patients}</p>
                            </div>
                            <div className="report-card">
                                <h3>Total Appointments</h3>
                                <p>{summary.appointments}</p>
                            </div>
                            <div className="report-card">
                                <h3>Total Revenue</h3>
                                <p>€{summary.revenue}</p>
                            </div>
                        </div>
                    </div>

                    <div className="detailed-reports">
                        <h3>Detailed Reports</h3>
                        <div className="reports-table">
                            {reportData.map((report, index) => (
                                <div key={report.id || index} className="report-row">
                                    <div className="report-detail">
                                        <strong>Report #{report.id}</strong>
                                        <p>Patients: {report.brojPacijenata}</p>
                                        <p>Appointments: {report.brojTermina}</p>
                                        <p>Revenue: €{report.finansijskiPregled}</p>
                                        <p>Date: {new Date(report.datumGenerisanja).toLocaleDateString()}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </>
            )}
        </div>
    );
}