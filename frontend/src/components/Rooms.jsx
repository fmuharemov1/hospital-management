import React, { useState, useEffect } from 'react';
import './Rooms.css';
import { Link } from 'react-router-dom';
import axios from 'axios';

export default function Rooms() {
    const [patients, setPatients] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [selectedRoomNumber, setSelectedRoomNumber] = useState('');
    const [selectedPatientId, setSelectedPatientId] = useState('');
    const [selectedDischargePatients, setSelectedDischargePatients] = useState([]);
    const [selectedAddPatients, setSelectedAddPatients] = useState([]);  // Za masovno dodavanje

    const refreshData = () => {
        axios.get('http://localhost:8088/korisnici/tip/pacijenti')
            .then(response => setPatients(response.data))
            .catch(error => console.error('Greška pri dohvatu pacijenata:', error));

        axios.get('http://localhost:8088/sobe')
            .then(response => setRooms(response.data))
            .catch(error => console.error('Greška pri dohvatu soba:', error));
    };

    useEffect(() => {
        refreshData();
    }, []);

    const selectedRoom = rooms.find(room => room.broj_sobe === selectedRoomNumber);

    const handleBooking = () => {
        if (!selectedRoomNumber || !selectedPatientId) {
            alert('Please select both a room and a patient to book.');
            return;
        }

        const patch = [
            { op: 'replace', path: '/role/soba/id', value: selectedRoom.id }
        ];

        axios.patch(`http://localhost:8088/korisnici/${selectedPatientId}/dodijeliSobu`, patch, {
            headers: { 'Content-Type': 'application/json-patch+json' }
        })
        .then(() => {
            alert(`Room ${selectedRoomNumber} successfully booked!`);
            refreshData();
        })
        .catch(error => {
            console.error('Greška pri dodjeli sobe:', error);
            alert('Greška pri dodjeli sobe.');
        });

        setSelectedRoomNumber('');
        setSelectedPatientId('');
    };

    const togglePatientSelection = (patientId, setSelectedArray) => {
        setSelectedArray(prev =>
            prev.includes(patientId)
                ? prev.filter(id => id !== patientId)
                : [...prev, patientId]
        );
    };

    const handleDischarge = () => {
        if (!selectedRoom || selectedDischargePatients.length === 0) {
            alert('Odaberite sobu i barem jednog pacijenta za otpust.');
            return;
        }

        const payload = {
            pacijentIds: selectedDischargePatients
        };

        axios.post(`http://localhost:8088/sobe/${selectedRoom.id}/otpusti-pacijente`, payload)
            .then(() => {
                alert('Pacijenti uspješno otpušteni.');
                refreshData();
                setSelectedDischargePatients([]);
            })
            .catch(error => {
                console.error('Greška pri otpustu pacijenata:', error);
                alert('Greška pri otpustu pacijenata.');
            });
    };

    const handleAddPatients = () => {
        if (!selectedRoom || selectedAddPatients.length === 0) {
            alert('Odaberite sobu i barem jednog pacijenta za dodavanje.');
            return;
        }

        const payload = {
            pacijentIds: selectedAddPatients
        };

        axios.post(`http://localhost:8088/sobe/${selectedRoom.id}/dodaj-pacijente`, payload)
            .then(() => {
                alert('Pacijenti uspješno dodani u sobu.');
                refreshData();
                setSelectedAddPatients([]);
            })
            .catch(error => {
                console.error('Greška pri dodavanju pacijenata:', error);
                alert('Greška pri dodavanju pacijenata.');
            });
    };

    return (
        <div className="rooms-page" style={{ maxWidth: '600px', margin: '40px auto', padding: '20px', backgroundColor: '#fff', borderRadius: '10px', boxShadow: '0 5px 15px rgba(0,0,0,0.1)', color: '#333' }}>
            <h1>Room Overview</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <nav className="navbar">
                <Link to="/rooms" className="nav-link active">Rooms</Link>
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
                        setSelectedPatientId('');
                        setSelectedDischargePatients([]);
                        setSelectedAddPatients([]);
                    }}
                    style={{ padding: '8px 12px', borderRadius: '6px', border: '1px solid #ccc' }}
                >
                    <option value="">-- Select --</option>
                    {rooms.map(room => (
                        <option key={room.broj_sobe} value={room.broj_sobe}>
                            Room {room.broj_sobe}
                        </option>
                    ))}
                </select>
            </div>

            {selectedRoom ? (
                <div style={{ border: '1px solid #00b894', borderRadius: '8px', padding: '20px', backgroundColor: '#e0f2f1' }}>
                    <h2>Room Details</h2>
                    <p><strong>Room Number:</strong> {selectedRoom.broj_sobe}</p>
                    <p><strong>Capacity:</strong> {selectedRoom.kapacitet} {selectedRoom.kapacitet > 1 ? 'people' : 'person'}</p>
                    <p>
                        <strong>Status:</strong> {selectedRoom.status}
                        {selectedRoom.status === 'Popunjena' && selectedRoom.current_patient &&
                            <span> (Patient: {selectedRoom.current_patient})</span>
                        }
                    </p>

                    {/* Individual booking */}
                    {(selectedRoom.status === 'Slobodna' || selectedRoom.status === 'Dostupna') && (
                        <div style={{ marginTop: '20px', paddingTop: '15px', borderTop: '1px solid #00b894' }}>
                            <h3>Book Room {selectedRoom.broj_sobe}</h3>
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
                                        {patient.ime} {patient.prezime}
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

                    {/* Mass add patients */}
                    {(selectedRoom.status === 'Slobodna' || selectedRoom.status === 'Dostupna') && (
                        <div style={{ marginTop: '20px', paddingTop: '15px', borderTop: '1px solid #0984e3' }}>
                            <h3>Add Multiple Patients to Room {selectedRoom.broj_sobe}</h3>
                            <p>Select patients to add:</p>
                            <ul style={{ listStyle: 'none', padding: 0, maxHeight: '150px', overflowY: 'auto' }}>
                                {patients
                                    .filter(p => !p.role?.soba || p.role.soba.id !== selectedRoom.id) // Only patients NOT already in this room
                                    .map(patient => (
                                        <li key={patient.id}>
                                            <label>
                                                <input
                                                    type="checkbox"
                                                    checked={selectedAddPatients.includes(patient.id)}
                                                    onChange={() => togglePatientSelection(patient.id, setSelectedAddPatients)}
                                                    style={{ marginRight: '8px' }}
                                                />
                                                {patient.ime} {patient.prezime}
                                            </label>
                                        </li>
                                    ))}
                            </ul>
                            <button
                                onClick={handleAddPatients}
                                style={{ padding: '10px 15px', backgroundColor: '#0984e3', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                            >
                                Add Selected Patients
                            </button>
                        </div>
                    )}

                    {/* Discharge patients */}
                    {patients.some(p => p.role?.soba?.id === selectedRoom.id) && (
                        <div style={{ marginTop: '20px', paddingTop: '15px', borderTop: '1px solid #00b894' }}>
                            <h3>Discharge Patients from Room {selectedRoom.broj_sobe}</h3>
                            <p>Select patients to discharge:</p>
                            <ul style={{ listStyle: 'none', padding: 0 }}>
                                {patients
                                    .filter(p => p.role?.soba?.id === selectedRoom.id)
                                    .map(patient => (
                                        <li key={patient.id}>
                                            <label>
                                                <input
                                                    type="checkbox"
                                                    checked={selectedDischargePatients.includes(patient.id)}
                                                    onChange={() => togglePatientSelection(patient.id, setSelectedDischargePatients)}
                                                    style={{ marginRight: '8px' }}
                                                />
                                                {patient.ime} {patient.prezime}
                                            </label>
                                        </li>
                                    ))}
                            </ul>
                            <button
                                onClick={handleDischarge}
                                style={{ padding: '10px 15px', backgroundColor: '#d63031', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer' }}
                            >
                                Discharge Selected
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
