import React, { useState } from 'react';
import './Invoice.css';
import { Link } from 'react-router-dom';

export default function Invoice() {
    const patients = [
        {
            id: 1,
            fullName: 'John Doe',
            email: 'john@example.com',
            phone: '+387 61 234 567',
            invoices: [
                { date: '2025-05-30', department: 'Cardiology', doctor: 'Dr. Heart', price: 50, paid: true },
                { date: '2025-06-05', department: 'Neurology', doctor: 'Dr. Brain', price: 70, paid: false },
            ],
        },
        {
            id: 2,
            fullName: 'Ana Petrović',
            email: 'ana@example.com',
            phone: '+387 61 987 654',
            invoices: [
                { date: '2025-06-10', department: 'Orthopedics', doctor: 'Dr. Bone', price: 60, paid: false },
            ],
        },
    ];

    const [selectedPatientId, setSelectedPatientId] = useState('');

    const selectedPatient = patients.find(p => p.id === Number(selectedPatientId));

    return (
        <div className="invoice-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link">Invoices</Link>
                <Link to="/reports" className="nav-link">Reports</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>Invoices</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <div className="select-patient">
                <label htmlFor="patient">Select Patient:</label>
                <select
                    id="patient"
                    value={selectedPatientId}
                    onChange={(e) => setSelectedPatientId(e.target.value)}
                >
                    <option value="">-- Choose a patient --</option>
                    {patients.map((p) => (
                        <option key={p.id} value={p.id}>{p.fullName}</option>
                    ))}
                </select>
            </div>

            {selectedPatient && (
                <>
                    <div className="user-info">
                        <p><strong>Name:</strong> {selectedPatient.fullName}</p>
                        <p><strong>Email:</strong> {selectedPatient.email}</p>
                        <p><strong>Phone:</strong> {selectedPatient.phone}</p>
                    </div>

                    <table className="invoice-table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Department</th>
                            <th>Doctor</th>
                            <th>Price (€)</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        {selectedPatient.invoices.map((inv, index) => (
                            <tr key={index}>
                                <td>{inv.date}</td>
                                <td>{inv.department}</td>
                                <td>{inv.doctor}</td>
                                <td>{inv.price}</td>
                                <td className={inv.paid ? 'paid' : 'unpaid'}>
                                    {inv.paid ? 'Paid' : 'Not Paid'}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
}
