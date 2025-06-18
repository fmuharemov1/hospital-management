import React, { useState } from 'react';
import './MedicalRecord.css';
import { Link } from 'react-router-dom';

export default function MedicalRecord() {
    // Initial patient data - this would typically come from an API
    const initialPatients = [
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

    // Initial medical history data - add unique IDs to each entry for easier management
    const initialMedicalHistory = {
        1: [ // for John
            {
                id: 'mhr101', date: '2025-04-20', time: '09:00', doctor: 'Dr. Heart',
                department: 'Cardiology', diagnosis: 'Hypertension', therapy: 'Lisinopril 10mg once daily',
            },
            {
                id: 'mhr102', date: '2025-05-01', time: '13:30', doctor: 'Dr. Brain',
                department: 'Neurology', diagnosis: 'Migraine', therapy: 'Ibuprofen 400mg as needed',
            },
        ],
        2: [ // for Ana
            {
                id: 'mhr201', date: '2025-03-15', time: '11:00', doctor: 'Dr. Joint',
                department: 'Orthopedics', diagnosis: 'Knee pain', therapy: 'Physical therapy 2x/week',
            },
        ]
    };

    // States to manage the data
    const [patients, setPatients] = useState(initialPatients);
    const [medicalHistory, setMedicalHistory] = useState(initialMedicalHistory);
    const [selectedPatientId, setSelectedPatientId] = useState('');

    // States for editing functionality
    const [editingEntryId, setEditingEntryId] = useState(null); // ID of the entry being edited
    const [currentEditData, setCurrentEditData] = useState({}); // Temporary data for editing

    // States for adding new entry functionality
    const [showAddForm, setShowAddForm] = useState(false); // Controls visibility of the add form
    const [newEntryData, setNewEntryData] = useState({ // Data for the new entry form
        date: '', time: '', doctor: '', department: '', diagnosis: '', therapy: '',
    });

    const selectedPatient = patients.find(p => p.id === parseInt(selectedPatientId));
    const selectedHistory = selectedPatientId ? medicalHistory[selectedPatientId] || [] : [];

    // --- Editing Functions ---
    const handleEdit = (entryToEdit) => {
        setEditingEntryId(entryToEdit.id);
        setCurrentEditData({ ...entryToEdit }); // Create a copy for editing
    };

    const handleChangeEdit = (e) => {
        const { name, value } = e.target;
        setCurrentEditData(prev => ({ ...prev, [name]: value }));
    };

    const handleSaveEdit = () => {
        setMedicalHistory(prevHistory => {
            const updatedPatientHistory = prevHistory[selectedPatientId].map(entry =>
                entry.id === editingEntryId ? currentEditData : entry
            );
            return {
                ...prevHistory,
                [selectedPatientId]: updatedPatientHistory
            };
        });
        setEditingEntryId(null);
        setCurrentEditData({});
        alert('Medical record entry updated successfully!');
    };

    const handleCancelEdit = () => {
        setEditingEntryId(null);
        setCurrentEditData({});
    };

    // --- Adding New Entry Functions ---
    const handleChangeNew = (e) => {
        const { name, value } = e.target;
        setNewEntryData(prev => ({ ...prev, [name]: value }));
    };

    const handleAddNewEntry = () => {
        if (!selectedPatientId) {
            alert('Please select a patient first.');
            return;
        }

        // Basic validation for new entry data (you can add more)
        if (!newEntryData.date || !newEntryData.time || !newEntryData.doctor || !newEntryData.diagnosis) {
            alert('Please fill in all required fields (Date, Time, Doctor, Diagnosis).');
            return;
        }

        setMedicalHistory(prevHistory => {
            const newEntry = {
                ...newEntryData,
                id: `mhr${Date.now()}` // Simple unique ID generation for demo
            };
            const updatedPatientHistory = [...(prevHistory[selectedPatientId] || []), newEntry];
            return {
                ...prevHistory,
                [selectedPatientId]: updatedPatientHistory
            };
        });
        setNewEntryData({ // Reset form
            date: '', time: '', doctor: '', department: '', diagnosis: '', therapy: '',
        });
        setShowAddForm(false); // Hide form after adding
        alert('New medical record entry added successfully!');
    };

    return (
        <div className="medical-record-page">
            <nav className="navbar">
                <Link to="/rooms" className="nav-link">Rooms</Link>
                <Link to="/dr-appointments" className="nav-link">Appointments</Link>
                <Link to="/emr" className="nav-link active">E-Record</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>E-Medical Record</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            <div className="patient-selector">
                <label>Select Patient:</label>
                <select
                    value={selectedPatientId}
                    onChange={(e) => {
                        setSelectedPatientId(e.target.value);
                        setEditingEntryId(null); // Reset editing mode when patient changes
                        setCurrentEditData({});
                        setShowAddForm(false); // Hide add form
                        setNewEntryData({date: '', time: '', doctor: '', department: '', diagnosis: '', therapy: ''});
                    }}
                >
                    <option value="">-- Choose --</option>
                    {patients.map((p) => (
                        <option key={p.id} value={p.id}>
                            {p.fullName}
                        </option>
                    ))}
                </select>
            </div>

            {selectedPatient && (
                <>
                    <div className="patient-info">
                        <div className="info-pair">
                            <p><strong>Name:</strong> {selectedPatient.fullName}</p>
                            <p><strong>JMBG:</strong> {selectedPatient.jmbg}</p>
                        </div>
                    </div>

                    <div className="history-section">
                        <button
                            onClick={() => setShowAddForm(!showAddForm)}
                            className="add-new-button"
                        >
                            {showAddForm ? 'Hide Form' : 'Add New Visit'}
                        </button>

                        {showAddForm && (
                            <div className="add-form">
                                <h3>Add New Visit Details</h3>
                                <div className="form-row">
                                    <label>Date: <input type="date" name="date" value={newEntryData.date} onChange={handleChangeNew} /></label>
                                    <label>Time: <input type="time" name="time" value={newEntryData.time} onChange={handleChangeNew} /></label>
                                </div>
                                <div className="form-row">
                                    <label>Doctor: <input type="text" name="doctor" value={newEntryData.doctor} onChange={handleChangeNew} /></label>
                                    <label>Department: <input type="text" name="department" value={newEntryData.department} onChange={handleChangeNew} /></label>
                                </div>
                                <div className="form-row full-width">
                                    <label>Diagnosis: <textarea name="diagnosis" value={newEntryData.diagnosis} onChange={handleChangeNew}></textarea></label>
                                </div>
                                <div className="form-row full-width">
                                    <label>Therapy: <textarea name="therapy" value={newEntryData.therapy} onChange={handleChangeNew}></textarea></label>
                                </div>
                                <div className="form-actions">
                                    <button onClick={handleAddNewEntry} className="save-button">Add Visit</button>
                                    <button onClick={() => setShowAddForm(false)} className="cancel-button">Cancel</button>
                                </div>
                            </div>
                        )}

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
                                    <th>Actions</th> {/* New column for edit/save/cancel */}
                                </tr>
                                </thead>
                                <tbody>
                                {selectedHistory.map((entry) => (
                                    <tr key={entry.id}> {/* Use unique ID for key */}
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <input type="date" name="date" value={currentEditData.date} onChange={handleChangeEdit} />
                                            ) : (
                                                entry.date
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <input type="time" name="time" value={currentEditData.time} onChange={handleChangeEdit} />
                                            ) : (
                                                entry.time
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <input type="text" name="doctor" value={currentEditData.doctor} onChange={handleChangeEdit} />
                                            ) : (
                                                entry.doctor
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <input type="text" name="department" value={currentEditData.department} onChange={handleChangeEdit} />
                                            ) : (
                                                entry.department
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <textarea name="diagnosis" value={currentEditData.diagnosis} onChange={handleChangeEdit}></textarea>
                                            ) : (
                                                entry.diagnosis
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <textarea name="therapy" value={currentEditData.therapy} onChange={handleChangeEdit}></textarea>
                                            ) : (
                                                entry.therapy
                                            )}
                                        </td>
                                        <td>
                                            {editingEntryId === entry.id ? (
                                                <>
                                                    <button onClick={handleSaveEdit} className="save-button">Save</button>
                                                    <button onClick={handleCancelEdit} className="cancel-button">Cancel</button>
                                                </>
                                            ) : (
                                                <button onClick={() => handleEdit(entry)} className="edit-button">Edit</button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p>No previous visits recorded for this patient.</p>
                        )}
                    </div>
                </>
            )}
        </div>
    );
}