import React, { useState, useEffect } from 'react';
import './MedicalRecord.css'; // Pretpostavljam da će CSS stilovi biti u ovom fajlu
import { Link, useNavigate } from 'react-router-dom';

export default function PatientEMR() {
    const navigate = useNavigate();
    const [patientInfo, setPatientInfo] = useState(null);
    const [medicalHistory, setMedicalHistory] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // Dohvati informacije o trenutnom pacijentu
    const fetchPatientInfo = async () => {
        const token = getAuthToken();
        if (!token) {
            setError("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch('http://localhost:8085/api/client/patient-emr/me', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch patient info: ${response.status}`);
            }

            const data = await response.json();
            console.log("Patient info received:", data);
            setPatientInfo(data);
        } catch (err) {
            console.error("Error fetching patient info:", err);
            setError(`Error loading patient info: ${err.message}`);
        }
    };

    // Dohvati historiju trenutnog pacijenta
    const fetchMedicalHistory = async () => {
        const token = getAuthToken();
        if (!token) return;

        try {
            const response = await fetch('http://localhost:8085/api/client/patient-emr/history', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch medical history: ${response.status}`);
            }

            const data = await response.json();
            console.log("Medical history received:", data);
            setMedicalHistory(data);
        } catch (err) {
            console.error("Error fetching medical history:", err);
            setError(`Error loading medical history: ${err.message}`);
        }
    };

    useEffect(() => {
        const loadData = async () => {
            setIsLoading(true);
            setError(null);

            // Fetch info and history concurrently for better performance
            await Promise.all([
                fetchPatientInfo(),
                fetchMedicalHistory()
            ]);

            setIsLoading(false);
        };

        loadData();
    }, [navigate]); // Added navigate to dependency array for useEffect best practices

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    // Ove funkcije za formatiranje su već postojale u vašem kodu, ostavio sam ih
    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        try {
            return new Date(dateString).toLocaleDateString('en-GB');
        } catch (error) {
            return dateString;
        }
    };

    const formatDateTime = (dateString) => {
        if (!dateString) return 'N/A';
        try {
            return new Date(dateString).toLocaleString('en-GB');
        } catch (error) {
            return dateString;
        }
    };

    if (isLoading) {
        return <div className="medical-record-page">Loading your medical record...</div>;
    }

    if (error) {
        return (
            <div className="medical-record-page error-message">
                <p>{error}</p>
                <Link to="/login">Go to Login</Link>
            </div>
        );
    }

    return (
        <div className="medical-record-page">
            <nav className="navbar">
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
                <Link to="/patient-emr" className="nav-link active">E-Record</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Log out</button>
            </nav>

            <h1>My E-Medical Record</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            {patientInfo && (
                <div className="patient-info">
                    <div className="info-pair">
                        <p><strong>Name:</strong> {patientInfo.fullName}</p>
                        <p><strong>Email:</strong> {patientInfo.email}</p>
                    </div>
                    {patientInfo.jmbg && (
                        <div className="info-pair">
                            <p><strong>JMBG:</strong> {patientInfo.jmbg}</p>
                        </div>
                    )}
                </div>
            )}

            {/* Ažurirani deo za prikaz istorije */}
            <div className="history-section">
                <h2>My Visit History</h2>
                {medicalHistory && medicalHistory.length > 0 ? (
                    <div className="medical-records-container">
                        <div className="records-table">
                            {medicalHistory.map((karton, kartonIndex) => (
                                <div key={karton.id || kartonIndex} className="record-card">
                                    <div className="record-header">
                                        <div className="record-info">
                                            <h3 className="record-title">Medical Record #{karton.brojKartona}</h3>
                                            {/* Koristite formatDate funkciju koju ste već definisali */}
                                            <span className="record-date">📅 {formatDate(karton.datumKreiranja)}</span>
                                        </div>
                                        <div className="record-stats">
                                            <span className="stat-badge diagnosis-badge">
                                                {karton.dijagnoze?.length || 0} Diagnoses
                                            </span>
                                            <span className="stat-badge therapy-badge">
                                                {karton.dijagnoze?.reduce((total, d) => total + (d.terapije?.length || 0), 0) || 0} Therapies
                                            </span>
                                        </div>
                                    </div>

                                    {karton.dijagnoze && karton.dijagnoze.length > 0 && (
                                        <div className="diagnoses-grid">
                                            {karton.dijagnoze.map((dijagnoza, dijagnozaIndex) => (
                                                <div key={dijagnoza.id || dijagnozaIndex} className="diagnosis-row">
                                                    <div className="diagnosis-main">
                                                        <div className="diagnosis-header">
                                                            <h4 className="diagnosis-name">{dijagnoza.naziv}</h4>
                                                            {/* Koristite formatDate funkciju */}
                                                            <span className="diagnosis-date">
                                                                {formatDate(dijagnoza.datumDijagnoze)}
                                                            </span>
                                                        </div>
                                                        <p className="diagnosis-description">{dijagnoza.opis}</p>
                                                    </div>

                                                    {dijagnoza.terapije && dijagnoza.terapije.length > 0 && (
                                                        <div className="therapies-list">
                                                            <h5 className="therapy-title">💊 Prescribed Treatments:</h5>
                                                            <div className="therapy-grid">
                                                                {dijagnoza.terapije.map((terapija, terapijaIndex) => (
                                                                    <div key={terapija.id || terapijaIndex} className="therapy-item">
                                                                        <div className="therapy-header">
                                                                            <strong className="therapy-name">{terapija.naziv}</strong>
                                                                            <span className="therapy-period">
                                                                                {/* Koristite formatDate funkciju */}
                                                                                {formatDate(terapija.datumPocetka)} - {formatDate(terapija.datumZavrsetka)}
                                                                            </span>
                                                                        </div>
                                                                        <p className="therapy-instructions">{terapija.opis}</p>
                                                                    </div>
                                                                ))}
                                                            </div>
                                                        </div>
                                                    )}
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                ) : (
                    <div className="no-records">
                        <p>No previous visits recorded for you.</p>
                        <p>Your medical history will appear here after your first visit.</p>
                    </div>
                )}
            </div>
        </div>
    );
}