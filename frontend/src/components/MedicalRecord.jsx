import React from 'react';
import './MedicalRecord.css';
import { Link } from 'react-router-dom';

export default function MedicalRecord() {
    const patient = {
        fullName: 'John Doe',
        jmbg: '1234567890123',
        birthDate: '1985-04-12',
        gender: 'Male',
        contact: '+387 61 234 567',
        address: 'Zmaja od Bosne bb, Sarajevo',
    };

    const medicalHistory = [
        {
            date: '2025-04-20',
            time: '09:00',
            doctor: 'Dr. Heart',
            department: 'Cardiology',
            diagnosis: 'Hypertension',
            therapy: 'Lisinopril 10mg once daily',
        },
        {
            date: '2025-05-01',
            time: '13:30',
            doctor: 'Dr. Brain',
            department: 'Neurology',
            diagnosis: 'Migraine',
            therapy: 'Ibuprofen 400mg as needed',
        },
    ];

    return (
        <div className="medical-record-page">
            <nav className="navbar">
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
                <Link to="/record" className="nav-link active">E-Record</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>E-Medical Record</h1>
            <div className="patient-info">
                <h2>Patient Information</h2>
                <p><strong>Full Name:</strong> {patient.fullName}</p>
                <p><strong>JMBG:</strong> {patient.jmbg}</p>
                <p><strong>Birth Date:</strong> {patient.birthDate}</p>
                <p><strong>Gender:</strong> {patient.gender}</p>
                <p><strong>Contact:</strong> {patient.contact}</p>
                <p><strong>Address:</strong> {patient.address}</p>
            </div>

            <div className="history-section">
                <h2>Visit History</h2>
                {medicalHistory.length > 0 ? (
                    <table className="history-table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Doctor</th>
                            <th>Department</th>
                            <th>Diagnosis</th>
                            <th>Therapy</th>
                        </tr>
                        </thead>
                        <tbody>
                        {medicalHistory.map((entry, index) => (
                            <tr key={index}>
                                <td>{entry.date}</td>
                                <td>{entry.time}</td>
                                <td>{entry.doctor}</td>
                                <td>{entry.department}</td>
                                <td>{entry.diagnosis}</td>
                                <td>{entry.therapy}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No previous visits recorded.</p>
                )}
            </div>
        </div>
    );
}
