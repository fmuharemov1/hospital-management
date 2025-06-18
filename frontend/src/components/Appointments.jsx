import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Appointments.css';
import { Link } from 'react-router-dom';

export default function Appointments() {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [selectedDoctor, setSelectedDoctor] = useState('');
    // Initial appointments data - ensure 'patient' field is present for all entries
    const [appointments, setAppointments] = useState({
        "2025-05-30": {
            "Dr. Heart": [{ time: "09:00", patient: "Marko Marković" }]
        },
        // Adding a pre-booked appointment by "You" for testing cancellation
        "2025-06-11": { // Current date for easier testing
            "Dr. Brain": [{ time: "12:00", patient: "You" }]
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

    // Filter booked slots to show only those booked by the current selected doctor
    const bookedSlotsForSelectedDoctor = appointments[formattedSelectedDate]?.[selectedDoctor] || [];
    const bookedSlotTimes = bookedSlotsForSelectedDoctor.map(app => app.time);

    const availableSlots = allSlots.filter(time => !bookedSlotTimes.includes(time));

    const handleBooking = (time) => {
        const newAppointment = { time, patient: 'You' }; // Assume 'You' is the current patient

        setAppointments(prev => {
            const dayAppointments = { ...prev[formattedSelectedDate] } || {};
            const doctorAppointments = dayAppointments[selectedDoctor] || [];

            // Add new appointment
            const updatedDoctorAppointments = [...doctorAppointments, newAppointment];

            return {
                ...prev,
                [formattedSelectedDate]: {
                    ...dayAppointments,
                    [selectedDoctor]: updatedDoctorAppointments
                }
            };
        });
        alert(`Appointment at ${time} booked successfully!`);
    };

    const handleCancelAppointment = (timeToRemove) => {
        // Confirmation dialog
        if (!window.confirm(`Are you sure you want to cancel the appointment at ${timeToRemove} with ${selectedDoctor}?`)) {
            return; // If user cancels, do nothing
        }

        setAppointments(prev => {
            const dayAppointments = { ...prev[formattedSelectedDate] } || {};
            const doctorAppointments = dayAppointments[selectedDoctor] || [];

            // Filter out the appointment that matches timeToRemove and is booked by 'You'
            const updatedDoctorAppointments = doctorAppointments.filter(app =>
                !(app.time === timeToRemove && app.patient === 'You')
            );

            // If no appointments left for the doctor on that day, potentially remove the doctor's entry
            // This is optional, depends on how you want to clean up empty arrays/objects
            if (updatedDoctorAppointments.length === 0) {
                const { [selectedDoctor]: removedDoctor, ...restOfDay } = dayAppointments;
                return {
                    ...prev,
                    [formattedSelectedDate]: Object.keys(restOfDay).length > 0 ? restOfDay : undefined // Remove day if empty
                };
            }

            return {
                ...prev,
                [formattedSelectedDate]: {
                    ...dayAppointments,
                    [selectedDoctor]: updatedDoctorAppointments
                }
            };
        });
        alert(`Appointment at ${timeToRemove} has been cancelled.`);
    };


    return (
        <div className="appointments-page">
            <h1>Appointments</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <nav className="navbar">
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link active">Appointments</Link>
                <Link to="/patient-emr" className="nav-link">E-Record</Link> {/* Corrected path */}
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
                    <h2>Appointments for {formattedSelectedDate} with {selectedDoctor}</h2>
                    {bookedSlotsForSelectedDoctor.length > 0 ? (
                        <ul>
                            {bookedSlotsForSelectedDoctor.map((app, idx) => (
                                <li key={idx}>
                                    <strong>{app.time}</strong> - {app.patient}
                                    {/* Show cancel button ONLY if the patient is 'You' (current user) */}
                                    {app.patient === 'You' && (
                                        <button
                                            onClick={() => handleCancelAppointment(app.time)}
                                            className="cancel-button"
                                        >
                                            Cancel
                                        </button>
                                    )}
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>No appointments booked yet for this doctor on this day.</p>
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