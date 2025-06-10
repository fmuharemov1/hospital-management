import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Appointments.css';
import { Link } from 'react-router-dom';

export default function Appointments() {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [selectedDoctor, setSelectedDoctor] = useState('');
    const [appointments, setAppointments] = useState({
        "2025-05-30": {
            "Dr. Heart": [{ time: "09:00", patient: "Marko Marković" }]
        }
    });

    const allSlots = ['09:00', '10:30', '12:00', '13:00', '14:30', '16:00'];

    const departments = {
        "Cardiology": ["Dr. Heart", "Dr. Vessels"],
        "Neurology": ["Dr. Brain", "Dr. Nerve"],
        "Orthopedics": ["Dr. Bone", "Dr. Joint"],
    };

    const formatDate = (date) => date.toISOString().split('T')[0];
    const formattedSelectedDate = formatDate(selectedDate);

    const bookedSlots = appointments[formattedSelectedDate]?.[selectedDoctor]?.map(app => app.time) || [];
    const availableSlots = allSlots.filter(time => !bookedSlots.includes(time));

    const handleBooking = (time) => {
        const newAppointment = { time, patient: 'You' };

        setAppointments(prev => {
            const day = prev[formattedSelectedDate] || {};
            const doctorAppointments = day[selectedDoctor] || [];
            return {
                ...prev,
                [formattedSelectedDate]: {
                    ...day,
                    [selectedDoctor]: [...doctorAppointments, newAppointment]
                }
            };
        });
    };

    return (
        <div className="appointments-page">
            <h1>Appointments</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <nav className="navbar">
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <div className="selection-section">
                <select value={selectedDepartment} onChange={(e) => {
                    setSelectedDepartment(e.target.value);
                    setSelectedDoctor('');
                }}>
                    <option value="">Select Department</option>
                    {Object.keys(departments).map(dept => (
                        <option key={dept} value={dept}>{dept}</option>
                    ))}
                </select>

                {selectedDepartment && (
                    <select value={selectedDoctor} onChange={(e) => setSelectedDoctor(e.target.value)}>
                        <option value="">Select Doctor</option>
                        {departments[selectedDepartment].map(doc => (
                            <option key={doc} value={doc}>{doc}</option>
                        ))}
                    </select>
                )}
            </div>

            <Calendar onChange={setSelectedDate} value={selectedDate} />

            {selectedDoctor && (
                <div className="appointments-list">
                    <h2>Appointments for {formattedSelectedDate}</h2>
                    {appointments[formattedSelectedDate]?.[selectedDoctor]?.length > 0 ? (
                        <ul>
                            {appointments[formattedSelectedDate][selectedDoctor].map((app, idx) => (
                                <li key={idx}><strong>{app.time}</strong> - {app.patient}</li>
                            ))}
                        </ul>
                    ) : (
                        <p>No appointments booked yet.</p>
                    )}

                    <h3>Available Slots</h3>
                    {availableSlots.length > 0 ? (
                        <ul>
                            {availableSlots.map((time, idx) => (
                                <li key={idx}>
                                    <button onClick={() => handleBooking(time)} className="slot-button">
                                        Book {time}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>No available slots for this doctor on this day.</p>
                    )}
                </div>
            )}
        </div>
    );
}
