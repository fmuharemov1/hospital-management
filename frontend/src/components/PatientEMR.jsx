import React from 'react'; // Nema potrebe za useState ako nema stanja za odabir
import './MedicalRecord.css'; // Možeš koristiti isti CSS ili napraviti novi
import { Link } from 'react-router-dom';

export default function PatientEMR() {
    // HARDKODIRANI PODACI ZA PACIJENTA za demonstraciju.
    // U PRAVOJ APLIKACIJI, ID pacijenta bi došao iz konteksta prijave (npr. userId iz tokena).
    // Zatim bi se ti podaci (fullName, jmbg, history itd.) dohvatili s API-ja na osnovu tog ID-a.
    const currentPatientId = 1; // Pretpostavljamo da je ovo ID prijavljenog pacijenta

    const patients = [
        {
            id: 1,
            fullName: 'John Doe',
            jmbg: '1234567890123',
            birthDate: '1985-04-12',
            gender: 'Male',
            contact: '+387 61 234 567',
            address: 'Zmaja od Bosne bb, Sarajevo',
        },
        {
            id: 2,
            fullName: 'Ana Kovač',
            jmbg: '9876543210987',
            birthDate: '1990-10-05',
            gender: 'Female',
            contact: '+387 62 987 654',
            address: 'Titova 10, Sarajevo',
        }
    ];

    const medicalHistory = {
        1: [ // za Johna
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
        ],
        2: [ // za Anu
            {
                date: '2025-03-15',
                time: '11:00',
                doctor: 'Dr. Joint',
                department: 'Orthopedics',
                diagnosis: 'Knee pain',
                therapy: 'Physical therapy 2x/week',
            },
        ]
    };

    // Direktno pronalazimo prijavljenog pacijenta i njegovu historiju
    const selectedPatient = patients.find(p => p.id === currentPatientId);
    const selectedHistory = medicalHistory[currentPatientId] || [];

    // Ako pacijent nije pronađen (trebalo bi se rješavati u stvarnoj aplikaciji),
    // možeš prikazati poruku o grešci ili preusmjeriti.
    if (!selectedPatient) {
        return (
            <div className="medical-record-page">
                <p>Patient data not found. Please log in again.</p>
                <Link to="/" className="nav-link">Log out</Link>
            </div>
        );
    }

    return (
        <div className="medical-record-page">
            <nav className="navbar">
                {/* Rute za pacijenta */}
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
                <Link to="/emr-patient" className="nav-link active">E-Record</Link> {/* Nova ruta za pacijenta */}
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>My E-Medical Record</h1> {/* Promijenjen naslov */}
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            {/* Nema selektora za pacijente */}

            <div className="patient-info">
                {/* Prikaz osnovnih informacija o prijavljenom pacijentu */}
                <div className="info-pair">
                    <p><strong>Name:</strong> {selectedPatient.fullName}</p>
                    <p><strong>JMBG:</strong> {selectedPatient.jmbg}</p>
                </div>
            </div>

            <div className="history-section">
                <h2>My Visit History</h2> {/* Promijenjen naslov */}
                {selectedHistory.length > 0 ? (
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
                        {selectedHistory.map((entry, index) => (
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
                    <p>No previous visits recorded for you.</p>
                )}
            </div>
        </div>
    );
}