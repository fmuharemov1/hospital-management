import React, { useState } from 'react';
import './Rooms.css'; // Keep your existing CSS
import { Link } from 'react-router-dom';

export default function Rooms() {
    // Dummy patient data for demonstration
    const patients = [
        { id: 1, name: 'John Doe' },
        { id: 2, name: 'Jane Smith' },
        { id: 3, name: 'Michael Johnson' },
    ];

    // Initial room data
    const initialRooms = [
        { room_number: '101', capacity: 2, status: 'Available', current_patient: null },
        { room_number: '102', capacity: 1, status: 'Occupied', current_patient: 'Jane Smith' },
        { room_number: '103', capacity: 3, status: 'Available', current_patient: null },
        { room_number: '104', capacity: 2, status: 'Occupied', current_patient: 'Michael Johnson' },
        { room_number: '105', capacity: 1, status: 'Available', current_patient: null },
    ];

    const [rooms, setRooms] = useState(initialRooms); // Use state to manage rooms
    const [selectedRoomNumber, setSelectedRoomNumber] = useState('');
    const [selectedPatientId, setSelectedPatientId] = useState(''); // State for selected patient to book

    // Find the selected room by its number
    const selectedRoom = rooms.find(room => room.room_number === selectedRoomNumber);

    const handleBooking = () => {
        if (!selectedRoomNumber || !selectedPatientId) {
            alert('Please select both a room and a patient to book.');
            return;
        }

        const patientToBook = patients.find(p => p.id === parseInt(selectedPatientId));

        if (!patientToBook) {
            alert('Selected patient not found.');
            return;
        }

        setRooms(prevRooms =>
            prevRooms.map(room => {
                if (room.room_number === selectedRoomNumber && room.status === 'Available') {
                    return {
                        ...room,
                        status: 'Occupied',
                        current_patient: patientToBook.name,
                    };
                }
                return room;
            })
        );
        alert(`Room ${selectedRoomNumber} successfully booked for ${patientToBook.name}!`);
        // Optionally reset selection
        setSelectedRoomNumber('');
        setSelectedPatientId('');
    };

    return (
        <div className="rooms-page" style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', backgroundColor: '#fff', borderRadius: '10px', boxShadow: '0 5px 15px rgba(0,0,0,0.1)', color: '#333' }}>
            <h1>Room Overview</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <nav className="navbar">
                <Link to="/rooms" className="nav-link active">Rooms</Link> {/* Mark Rooms as active */}
                <Link to="/dr-appointments" className="nav-link">Appointments</Link>
                <Link to="/emr" className="nav-link">E-Record</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>
            <div style={{ marginBottom: '20px' }}>
                <label htmlFor="roomSelect" style={{ marginRight: '10px', fontWeight: 'bold' }}>Select a Room:</label>
                <select
                    id="roomSelect"
                    value={selectedRoomNumber}
                    onChange={e => {
                        setSelectedRoomNumber(e.target.value);
                        setSelectedPatientId(''); // Reset patient selection when room changes
                    }}
                    style={{ padding: '8px 12px', borderRadius: '6px', border: '1px solid #ccc' }}
                >
                    <option value="">-- Select --</option>
                    {rooms.map(room => (
                        <option key={room.room_number} value={room.room_number}>
                            Room {room.room_number}
                        </option>
                    ))}
                </select>
            </div>

            {selectedRoom ? (
                <div style={{ border: '1px solid #00b894', borderRadius: '8px', padding: '20px', backgroundColor: '#e0f2f1' }}>
                    <h2>Room Details</h2>
                    <p><strong>Room Number:</strong> {selectedRoom.room_number}</p>
                    <p><strong>Capacity:</strong> {selectedRoom.capacity} {selectedRoom.capacity > 1 ? 'people' : 'person'}</p>
                    <p>
                        <strong>Status:</strong> {selectedRoom.status}
                        {selectedRoom.status === 'Occupied' && selectedRoom.current_patient &&
                            <span> (Patient: {selectedRoom.current_patient})</span>
                        }
                    </p>

                    {selectedRoom.status === 'Available' && (
                        <div style={{ marginTop: '20px', paddingTop: '15px', borderTop: '1px solid #00b894' }}>
                            <h3>Book Room {selectedRoom.room_number}</h3>
                            <label htmlFor="patientSelect" style={{ marginRight: '10px', fontWeight: 'bold' }}>Select Patient:</label>
                            <select
                                id="patientSelect"
                                value={selectedPatientId}
                                onChange={e => setSelectedPatientId(e.target.value)}
                                style={{ padding: '8px 12px', borderRadius: '6px', border: '1px solid #ccc', marginRight: '10px' }}
                            >
                                <option value="">-- Select Patient --</option>
                                {patients.map(patient => (
                                    <option key={patient.id} value={patient.id}>
                                        {patient.name}
                                    </option>
                                ))}
                            </select>
                            <button
                                onClick={handleBooking}
                                style={{ padding: '10px 15px', backgroundColor: '#00b894', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                            >
                                Book Room
                            </button>
                        </div>
                    )}
                </div>
            ) : (
                <p>Please select a room to view information.</p>
            )}
        </div>
    );
}