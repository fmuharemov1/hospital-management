import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import './Appointments.css'; // Možeš koristiti isti CSS ili napraviti novi
import { Link } from 'react-router-dom';

export default function DoctorAppointments() {
    const [selectedDate, setSelectedDate] = useState(new Date());

    // HARDKODIRANI DOKTOR za demonstraciju.
    // U PRAVOJ APLIKACIJI, ovo bi dolazilo iz konteksta prijave!
    const doctorName = "Dr. Heart";
    const doctorDepartment = "Cardiology"; // Ovo bi također bilo dinamički određeno

    // Stanje termina, slično kao kod pacijenta, ali bi realno dolazilo s API-ja
    // koji bi ti dao samo termine za ulogovanog doktora.
    const [appointments, setAppointments] = useState({
        "2025-05-30": { // Stari datum radi primjera
            "Dr. Heart": [
                { time: "09:00", patient: "Marko Marković" },
                { time: "10:30", patient: "Ana Anić" }
            ]
        },
        "2025-06-11": { // Termin za današnji dan, 11. jun 2025
            "Dr. Heart": [
                { time: "12:00", patient: "Petar Perić" },
                { time: "14:30", patient: "Maja Majić" }
            ]
        },
        "2025-06-19": { // Stari datum radi primjera
            "Dr. Heart": [
                { time: "09:00", patient: "Harun Bešlić" }
            ]
        },
    });

    // Svi slotovi koje doktor ima u svom rasporedu
    const allSlots = ['09:00', '10:30', '12:00', '13:00', '14:30', '16:00'];

    const formatDate = (date) => date.toISOString().split('T')[0];
    const formattedSelectedDate = formatDate(selectedDate);

    // Dohvati termine samo za odabranog datuma i hardkodiranog doktora
    const doctorsAppointmentsForSelectedDay = appointments[formattedSelectedDate]?.[doctorName] || [];

    // Funkcija koja određuje status slota (zauzet ili slobodan)
    const getSlotStatus = (time) => {
        const isBooked = doctorsAppointmentsForSelectedDay.some(app => app.time === time);
        return isBooked ? 'booked' : 'available';
    };

    return (
        <div className="appointments-page">
            <h1>Doctor Appointments - {doctorName} ({doctorDepartment})</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <nav className="navbar">
                <Link to="/rooms" className="nav-link">Rooms</Link>
                <Link to="/dr-appointments" className="nav-link active">Appointments</Link>
                <Link to="/emr" className="nav-link">E-Record</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <div className="calendar-section">
                <Calendar onChange={setSelectedDate} value={selectedDate} />
            </div>

            <div className="appointments-list">
                <h2>Appointments for {formattedSelectedDate}</h2>

                {allSlots.length > 0 ? (
                    <ul>
                        {allSlots.map((time, idx) => {
                            const status = getSlotStatus(time);
                            const appointment = doctorsAppointmentsForSelectedDay.find(app => app.time === time);
                            return (
                                <li key={idx} className={`slot-item ${status}`}>
                                    <strong>{time}</strong> -
                                    {status === 'booked' ? (
                                        <span> Patient: {appointment.patient}</span>
                                    ) : (
                                        <span> Available</span>
                                    )}
                                </li>
                            );
                        })}
                    </ul>
                ) : (
                    <p>No slots defined for this day.</p>
                )}
            </div>
        </div>
    );
}