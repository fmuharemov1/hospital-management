import React, { useState, useEffect } from 'react';
import './MedicalRecord.css';
import { Link, useNavigate } from 'react-router-dom';

export default function MedicalRecord() {
    const navigate = useNavigate();

    // Stanja za podatke sa backenda
    const [patients, setPatients] = useState([]);
    const [medicalHistory, setMedicalHistory] = useState({}); // Keš za historiju po pacijentu
    const [selectedPatientId, setSelectedPatientId] = useState('');

    // Stanja za funkcionalnost uređivanja
    const [editingEntryId, setEditingEntryId] = useState(null);
    const [currentEditData, setCurrentEditData] = useState({});

    // Stanja za funkcionalnost dodavanja nove fakture
    const [showAddForm, setShowAddForm] = useState(false);
    const [newEntryData, setNewEntryData] = useState({
        date: '', time: '', doctor: '', department: '', diagnosis: '', therapy: ''
    });

    const [isLoading, setIsLoading] = useState(true); // Stanje za loading
    const [error, setError] = useState(null); // Stanje za greške

    // Dohvati JWT token
    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // --- Učitavanje pacijenata (pokreće se samo jednom pri mountu) ---
    useEffect(() => {
        const fetchPatients = async () => {
            setIsLoading(true);
            setError(null);
            const token = getAuthToken();
            if (!token) {
                setError("Authentication token not found. Please log in.");
                navigate('/login');
                setIsLoading(false);
                return;
            }

            try {
                const mapKartonToMedicalRecord = (karton) => ({
                    id: karton.id,
                    patientId: karton.patientId,
                    date: karton.datum,
                    time: karton.vrijeme,
                    doctor: karton.doktor,
                    department: karton.odjel,
                    diagnosis: karton.dijagnoza,
                    therapy: karton.terapija
                });
                const response = await fetch('http://localhost:8093/api/kartoni', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`Failed to fetch patients: ${response.status} - ${errorText}`);
                }

                const patientsData = await response.json();
                setPatients(patientsData);
            } catch (err) {
                console.error("Error fetching patients:", err);
                setError(`Error loading patients: ${err.message}`);
            } finally {
                setIsLoading(false);
            }
        };
        fetchPatients();
    }, [navigate]); // navigate je dodan kao dependency

    // --- Učitavanje medicinske historije za odabranog pacijenta ---
    useEffect(() => {
        const fetchPatientHistory = async () => {
            if (!selectedPatientId) return; // Ne dohvaćaj ako nema odabranog pacijenta

            setError(null); // Resetuj greške za historiju
            const token = getAuthToken();
            if (!token) {
                setError("Authentication token not found. Please log in.");
                navigate('/login');
                return;
            }

            try {
                // Provjeriti da li već imamo keširanu historiju
                if (medicalHistory[selectedPatientId]) {
                    console.log("Using cached medical history for patient:", selectedPatientId);
                    return; // Koristi keširanu verziju
                }

                console.log("Fetching medical history for patient:", selectedPatientId);
                const response = await fetch(`http://localhost:8093/api/kartoni/${selectedPatientId}/karton`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(`Failed to fetch medical history: ${response.status} - ${errorText}`);
                }

                const historyData = await response.json();
                setMedicalHistory(prev => ({
                    ...prev,
                    [selectedPatientId]: historyData // Keširaj historiju
                }));
            } catch (err) {
                console.error("Error fetching medical history:", err);
                setError(`Error loading medical history: ${err.message}`);
            }
        };

        // Pozovi kada se selectedPatientId promijeni ili kada se promijeni funkcija navigate (zbog ESLinta)
        fetchPatientHistory();
    }, [selectedPatientId, navigate, medicalHistory]); // Dodajte medicalHistory kao dependency

    // Filtriraj odabranog pacijenta i historiju
    const selectedPatient = patients.find(p => p.id === Number(selectedPatientId));
    const selectedHistory = selectedPatientId ? medicalHistory[selectedPatientId] || [] : [];

    // --- Editing Functions ---
    const handleEdit = (entryToEdit) => {
        setEditingEntryId(entryToEdit.id);
        setCurrentEditData({ ...entryToEdit });
    };

    const handleChangeEdit = (e) => {
        const { name, value } = e.target;
        setCurrentEditData(prev => ({ ...prev, [name]: value }));
    };

    const handleSaveEdit = async (entryId) => {
        const token = getAuthToken();
        if (!token) {
            alert("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8093/api/kartoni/${entryId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(currentEditData)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to update medical record entry: ${response.status} - ${errorText}`);
            }

            // Ažuriraj keširanu historiju nakon uspješnog spremanja
            setMedicalHistory(prev => {
                const updatedEntries = prev[selectedPatientId].map(entry =>
                    entry.id === entryId ? currentEditData : entry
                );
                return { ...prev, [selectedPatientId]: updatedEntries };
            });

            setEditingEntryId(null);
            setCurrentEditData({});
            alert('Medical record entry updated successfully!');
        } catch (err) {
            console.error("Error updating entry:", err);
            setError(`Error updating entry: ${err.message}`);
        }
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

    const handleAddNewEntry = async () => {
        if (!selectedPatientId) {
            alert('Please select a patient first.');
            return;
        }

        if (!newEntryData.date || !newEntryData.time || !newEntryData.doctor || !newEntryData.diagnosis) {
            alert('Please fill in all required fields (Date, Time, Doctor, Diagnosis).');
            return;
        }

        const token = getAuthToken();
        if (!token) {
            alert("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8093/api/kartoni/${selectedPatientId}/karton`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    ...newEntryData,
                    patientId: Number(selectedPatientId) // Uvjerite se da je patientId broj
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to add medical record entry: ${response.status} - ${errorText}`);
            }

            const addedEntry = await response.json(); // Pretpostavljamo da backend vraća dodani entitet sa ID-om
            // Ažuriraj keširanu historiju dodavanjem novog unosa
            setMedicalHistory(prev => ({
                ...prev,
                [selectedPatientId]: [...(prev[selectedPatientId] || []), addedEntry]
            }));

            setNewEntryData({ date: '', time: '', doctor: '', department: '', diagnosis: '', therapy: '' });
            setShowAddForm(false);
            alert('New medical record entry added successfully!');
        } catch (err) {
            console.error("Error adding entry:", err);
            setError(`Error adding entry: ${err.message}`);
        }
    };

    // --- Delete Function ---
    const handleDeleteEntry = async (entryId) => {
        if (!window.confirm("Are you sure you want to delete this medical record entry?")) {
            return;
        }

        const token = getAuthToken();
        if (!token) {
            alert("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8093/api/kartoni/${entryId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to delete medical record entry: ${response.status} - ${errorText}`);
            }

            // Ažuriraj keširanu historiju uklanjanjem unosa
            setMedicalHistory(prev => ({
                ...prev,
                [selectedPatientId]: prev[selectedPatientId].filter(entry => entry.id !== entryId)
            }));

            alert('Medical record entry deleted successfully!');
        } catch (err) {
            console.error("Error deleting entry:", err);
            setError(`Error deleting entry: ${err.message}`);
        }
    };

    // Funkcija za odjavu
    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    if (isLoading) {
        return <div className="medical-record-page">Loading data...</div>;
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
                <Link to="/rooms" className="nav-link">Rooms</Link>
                <Link to="/dr-appointments" className="nav-link">Appointments</Link>
                <Link to="/emr" className="nav-link active">EMR</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Log out</button>
            </nav>

            <h1>E-Medical Record</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />

            <div className="patient-selector">
                <label>Select Patient:</label>
                <select
                    value={selectedPatientId}
                    onChange={(e) => {
                        setSelectedPatientId(e.target.value);
                        setEditingEntryId(null);
                        setCurrentEditData({});
                        setShowAddForm(false);
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
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {selectedHistory.map((entry) => (
                                    <tr key={entry.id}>
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
                                                    <button onClick={() => handleSaveEdit(entry.id)} className="save-button">Save</button>
                                                    <button onClick={handleCancelEdit} className="cancel-button">Cancel</button>
                                                </>
                                            ) : (
                                                <>
                                                    <button onClick={() => handleEdit(entry)} className="edit-button">Edit</button>
                                                    <button onClick={() => handleDeleteEntry(entry.id)} className="delete-button">Delete</button>
                                                </>
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
