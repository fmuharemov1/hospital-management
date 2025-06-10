import React, { useState } from 'react';
import './Reports.css';
import { Link } from 'react-router-dom';

export default function Reports() {
    const [selectedReport, setSelectedReport] = useState('');

    // Lažni podaci za izvještaje
    const reportData = {
        daily: {
            patients: 5,
            appointments: 7,
            revenue: 350,
        },
        weekly: {
            patients: 28,
            appointments: 32,
            revenue: 1600,
        },
        monthly: {
            patients: 112,
            appointments: 130,
            revenue: 6500,
        }
    };

    const currentReport = reportData[selectedReport];

    return (
        <div className="reports-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link">Invoices</Link>
                <Link to="/reports" className="nav-link">Reports</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>Reports</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <div className="report-selector">
                <label htmlFor="report-type">Select report type:</label>
                <select
                    id="report-type"
                    value={selectedReport}
                    onChange={(e) => setSelectedReport(e.target.value)}
                >
                    <option value="">-- Choose --</option>
                    <option value="daily">Daily Report</option>
                    <option value="weekly">Weekly Report</option>
                    <option value="monthly">Monthly Report</option>
                </select>
            </div>

            {currentReport && (
                <div className="report-results">
                    <div className="report-card">
                        <h3>Number of Patients</h3>
                        <p>{currentReport.patients}</p>
                    </div>
                    <div className="report-card">
                        <h3>Number of Appointments</h3>
                        <p>{currentReport.appointments}</p>
                    </div>
                    <div className="report-card">
                        <h3>Total Revenue</h3>
                        <p>€{currentReport.revenue}</p>
                    </div>
                </div>
            )}
        </div>
    );
}
