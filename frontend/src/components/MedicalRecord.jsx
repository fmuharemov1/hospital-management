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

    // Stanja za funkcionalnost dodavanja novog kartona
    const [showAddForm, setShowAddForm] = useState(false);
    const [newEntryData, setNewEntryData] = useState({
        datumKreiranja: '',
        brojKartona: '',
        dijagnoze: [
            {
                nazivDijagnoze: '',
                opis: '',
                datumDijagnoze: '',
                terapije: [
                    {
                        nazivTerapije: '',
                        opis: '',
                        doziranje: '',
                        trajanje: ''
                    }
                ]
            }
        ]
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
                const response = await fetch('http://localhost:8085/api/client/emr/patients', {
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
    }, [navigate]);

    // --- Učitavanje medicinske historije za odabranog pacijenta ---
    useEffect(() => {
        const fetchPatientHistory = async () => {
            if (!selectedPatientId) return;

            setError(null);
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
                    return;
                }

                console.log("Fetching medical history for patient:", selectedPatientId);
                const response = await fetch(`http://localhost:8085/api/client/emr/patients/${selectedPatientId}/karton`, {
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
                console.log("Received history data:", historyData); // DEBUG
                setMedicalHistory(prev => ({
                    ...prev,
                    [selectedPatientId]: historyData
                }));
            } catch (err) {
                console.error("Error fetching medical history:", err);
                setError(`Error loading medical history: ${err.message}`);
            }
        };

        fetchPatientHistory();
    }, [selectedPatientId, navigate, medicalHistory]);

    // Filtriraj odabranog pacijenta i historiju
    const selectedPatient = patients.find(p => p.id === Number(selectedPatientId));
    const selectedHistory = selectedPatientId ? medicalHistory[selectedPatientId] || [] : [];

    // --- Handling Functions for Dynamic Form ---

    // Dodavanje nove dijagnoze
    const addDijagnoza = () => {
        setNewEntryData(prev => ({
            ...prev,
            dijagnoze: [
                ...prev.dijagnoze,
                {
                    nazivDijagnoze: '',
                    opis: '',
                    datumDijagnoze: '',
                    terapije: [
                        {
                            nazivTerapije: '',
                            opis: '',
                            doziranje: '',
                            trajanje: ''
                        }
                    ]
                }
            ]
        }));
    };

    // Uklanjanje dijagnoze
    const removeDijagnoza = (dijagnozaIndex) => {
        setNewEntryData(prev => ({
            ...prev,
            dijagnoze: prev.dijagnoze.filter((_, index) => index !== dijagnozaIndex)
        }));
    };

    // Dodavanje nove terapije u dijagnozu
    const addTerapija = (dijagnozaIndex) => {
        setNewEntryData(prev => {
            const newDijagnoze = [...prev.dijagnoze];
            newDijagnoze[dijagnozaIndex].terapije.push({
                nazivTerapije: '',
                opis: '',
                doziranje: '',
                trajanje: ''
            });
            return { ...prev, dijagnoze: newDijagnoze };
        });
    };

    // Uklanjanje terapije
    const removeTerapija = (dijagnozaIndex, terapijaIndex) => {
        setNewEntryData(prev => {
            const newDijagnoze = [...prev.dijagnoze];
            newDijagnoze[dijagnozaIndex].terapije = newDijagnoze[dijagnozaIndex].terapije.filter((_, index) => index !== terapijaIndex);
            return { ...prev, dijagnoze: newDijagnoze };
        });
    };

    // Handle changes in dijagnoza fields
    const handleDijagnozaChange = (dijagnozaIndex, field, value) => {
        setNewEntryData(prev => {
            const newDijagnoze = [...prev.dijagnoze];
            newDijagnoze[dijagnozaIndex][field] = value;
            return { ...prev, dijagnoze: newDijagnoze };
        });
    };

    // Handle changes in terapija fields
    const handleTerapijaChange = (dijagnozaIndex, terapijaIndex, field, value) => {
        setNewEntryData(prev => {
            const newDijagnoze = [...prev.dijagnoze];
            newDijagnoze[dijagnozaIndex].terapije[terapijaIndex][field] = value;
            return { ...prev, dijagnoze: newDijagnoze };
        });
    };

    // --- Basic Form Handling ---
    const handleChangeNew = (e) => {
        const { name, value } = e.target;
        setNewEntryData(prev => ({ ...prev, [name]: value }));
    };

    // Generisanje brojKartona
    const generateBrojKartona = () => {
        const year = new Date().getFullYear();
        const month = String(new Date().getMonth() + 1).padStart(2, '0');
        const day = String(new Date().getDate()).padStart(2, '0');
        const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
        return `KARTON-${selectedPatientId}-${year}${month}${day}-${random}`;
    };

    const handleAddNewEntry = async () => {
        if (!selectedPatientId) {
            alert('Please select a patient first.');
            return;
        }

        if (!newEntryData.datumKreiranja) {
            alert('Please fill in the required date field.');
            return;
        }

        // Validate dijagnoze
        const hasValidDijagnoza = newEntryData.dijagnoze.some(d => d.nazivDijagnoze.trim() !== '');
        if (!hasValidDijagnoza) {
            alert('Please add at least one diagnosis.');
            return;
        }

        const token = getAuthToken();
        if (!token) {
            alert("Authentication token not found. Please log in.");
            navigate('/login');
            return;
        }

        try {
            // Step 1: Create karton first
            const kartonData = {
                pacijentUuid: Number(selectedPatientId),
                datumKreiranja: newEntryData.datumKreiranja + "T10:00:00",
                brojKartona: newEntryData.brojKartona || generateBrojKartona()
            };

            console.log("Creating karton:", kartonData);

            const kartonResponse = await fetch(`http://localhost:8085/api/client/emr/patients/${selectedPatientId}/karton`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(kartonData)
            });

            if (!kartonResponse.ok) {
                const errorText = await kartonResponse.text();
                throw new Error(`Failed to create medical record: ${kartonResponse.status} - ${errorText}`);
            }

            const createdKarton = await kartonResponse.json();
            console.log("Created karton:", createdKarton);

            // Step 2: Add dijagnoze and terapije - STVARNI API POZIVI
            for (const dijagnoza of newEntryData.dijagnoze) {
                if (dijagnoza.nazivDijagnoze.trim() !== '') {
                    // STVARNI API poziv za dijagnozu
                    const dijagnozaResponse = await fetch(`http://localhost:8085/api/client/emr/kartoni/${createdKarton.id}/dijagnoze`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            naziv: dijagnoza.nazivDijagnoze,
                            opis: dijagnoza.opis,
                            datumDijagnoze: dijagnoza.datumDijagnoze || (newEntryData.datumKreiranja + "T10:00:00")
                        })
                    });

                    if (!dijagnozaResponse.ok) {
                        const errorText = await dijagnozaResponse.text();
                        throw new Error(`Failed to create dijagnoza: ${dijagnozaResponse.status} - ${errorText}`);
                    }

                    const createdDijagnoza = await dijagnozaResponse.json();
                    console.log("Created dijagnoza:", createdDijagnoza);

                    // Dodaj terapije za ovu dijagnozu
                    for (const terapija of dijagnoza.terapije) {
                        if (terapija.nazivTerapije.trim() !== '') {
                            // STVARNI API poziv za terapiju
                            const terapijaResponse = await fetch(`http://localhost:8085/api/client/emr/dijagnoze/${createdDijagnoza.id}/terapije`, {
                                method: 'POST',
                                headers: {
                                    'Authorization': `Bearer ${token}`,
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({
                                    naziv: terapija.nazivTerapije,
                                    opis: terapija.opis,
                                    doziranje: terapija.doziranje,
                                    trajanje: terapija.trajanje
                                })
                            });

                            if (!terapijaResponse.ok) {
                                const errorText = await terapijaResponse.text();
                                throw new Error(`Failed to create terapija: ${terapijaResponse.status} - ${errorText}`);
                            }

                            const createdTerapija = await terapijaResponse.json();
                            console.log("Created terapija:", createdTerapija);
                        }
                    }
                }
            }

            // Refresh the data after successful creation
            // Ukloni keširanu historiju da se ponovo učita sa svim podacima
            setMedicalHistory(prev => {
                const updated = { ...prev };
                delete updated[selectedPatientId];
                return updated;
            });

            // Ponovo učitaj historiju
            setTimeout(() => {
                window.location.reload(); // Temporary solution to refresh all data
            }, 1000);

            // Reset form
            setNewEntryData({
                datumKreiranja: '',
                brojKartona: '',
                dijagnoze: [
                    {
                        nazivDijagnoze: '',
                        opis: '',
                        datumDijagnoze: '',
                        terapije: [
                            {
                                nazivTerapije: '',
                                opis: '',
                                doziranje: '',
                                trajanje: ''
                            }
                        ]
                    }
                ]
            });
            setShowAddForm(false);
            alert('Medical record with diagnoses and therapies created successfully!');

        } catch (err) {
            console.error("Error adding entry:", err);
            setError(`Error adding entry: ${err.message}`);
            alert(`Error: ${err.message}`);
        }
    };

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
            const response = await fetch(`http://localhost:8085/api/client/emr/kartoni/${entryId}`, {
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
            const response = await fetch(`http://localhost:8085/api/client/emr/kartoni/${entryId}`, {
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
                        setNewEntryData({
                            datumKreiranja: '',
                            brojKartona: '',
                            dijagnoze: [
                                {
                                    nazivDijagnoze: '',
                                    opis: '',
                                    datumDijagnoze: '',
                                    terapije: [
                                        {
                                            nazivTerapije: '',
                                            opis: '',
                                            doziranje: '',
                                            trajanje: ''
                                        }
                                    ]
                                }
                            ]
                        });
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
                <div className="selected-patient-info">
                    <h2>Patient: {selectedPatient.fullName}</h2>
                    <p>Email: {selectedPatient.email}</p>
                </div>
            )}

            {selectedPatientId && (
                <div className="action-buttons">
                    <button onClick={() => setShowAddForm(!showAddForm)} className="add-entry-btn" id="boja1">
                        {showAddForm ? 'Cancel' : 'Add New Medical Record'}
                    </button>
                </div>
            )}

            {/* Form za dodavanje novog kartona sa dijagnozama i terapijama */}
            {showAddForm && (
                <div className="add-entry-form">
                    <h3>Add New Medical Record</h3>

                    {/* Basic karton info */}
                    <div className="form-row">
                        <div className="form-group">
                            <label>Date Created *</label>
                            <input
                                type="date"
                                name="datumKreiranja"
                                value={newEntryData.datumKreiranja}
                                onChange={handleChangeNew}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>Medical Record Number (optional)</label>
                            <input
                                type="text"
                                name="brojKartona"
                                value={newEntryData.brojKartona}
                                onChange={handleChangeNew}
                                placeholder="Will be auto-generated if empty"
                            />
                        </div>
                    </div>

                    {/* Dijagnoze section */}
                    <div className="dijagnoze-form-section">
                        <h4>Dijagnoze</h4>
                        {newEntryData.dijagnoze.map((dijagnoza, dijagnozaIndex) => (
                            <div key={dijagnozaIndex} className="dijagnoza-form-group">
                                <div className="dijagnoza-header">
                                    <h5>Dijagnoza {dijagnozaIndex + 1}</h5>
                                    {newEntryData.dijagnoze.length > 1 && (
                                        <button
                                            type="button"
                                            onClick={() => removeDijagnoza(dijagnozaIndex)}
                                            className="remove-btn"
                                        >
                                            Remove Dijagnoza
                                        </button>
                                    )}
                                </div>

                                <div className="form-row">
                                    <div className="form-group">
                                        <label>Naziv Dijagnoze *</label>
                                        <input
                                            type="text"
                                            value={dijagnoza.nazivDijagnoze}
                                            onChange={(e) => handleDijagnozaChange(dijagnozaIndex, 'nazivDijagnoze', e.target.value)}
                                            placeholder="Naziv dijagnoze"
                                            required
                                        />
                                    </div>
                                    <div className="form-group">
                                        <label>Datum Dijagnoze</label>
                                        <input
                                            type="date"
                                            value={dijagnoza.datumDijagnoze}
                                            onChange={(e) => handleDijagnozaChange(dijagnozaIndex, 'datumDijagnoze', e.target.value)}
                                        />
                                    </div>
                                </div>

                                <div className="form-group">
                                    <label>Opis Dijagnoze</label>
                                    <textarea
                                        value={dijagnoza.opis}
                                        onChange={(e) => handleDijagnozaChange(dijagnozaIndex, 'opis', e.target.value)}
                                        placeholder="Detaljni opis dijagnoze"
                                        rows="3"
                                    />
                                </div>

                                {/* Terapije for this dijagnoza */}
                                <div className="terapije-form-section">
                                    <h5>Terapije za ovu dijagnozu</h5>
                                    {dijagnoza.terapije.map((terapija, terapijaIndex) => (
                                        <div key={terapijaIndex} className="terapija-form-group">
                                            <div className="terapija-header">
                                                <h6>Terapija {terapijaIndex + 1}</h6>
                                                {dijagnoza.terapije.length > 1 && (
                                                    <button
                                                        type="button"
                                                        onClick={() => removeTerapija(dijagnozaIndex, terapijaIndex)}
                                                        className="remove-btn small"
                                                    >
                                                        Remove Terapija
                                                    </button>
                                                )}
                                            </div>

                                            <div className="form-row">
                                                <div className="form-group">
                                                    <label>Naziv Terapije</label>
                                                    <input
                                                        type="text"
                                                        value={terapija.nazivTerapije}
                                                        onChange={(e) => handleTerapijaChange(dijagnozaIndex, terapijaIndex, 'nazivTerapije', e.target.value)}
                                                        placeholder="Naziv terapije"
                                                    />
                                                </div>
                                                <div className="form-group">
                                                    <label>Doziranje</label>
                                                    <input
                                                        type="text"
                                                        value={terapija.doziranje}
                                                        onChange={(e) => handleTerapijaChange(dijagnozaIndex, terapijaIndex, 'doziranje', e.target.value)}
                                                        placeholder="npr. 2x dnevno"
                                                    />
                                                </div>
                                            </div>

                                            <div className="form-row">
                                                <div className="form-group">
                                                    <label>Opis Terapije</label>
                                                    <textarea
                                                        value={terapija.opis}
                                                        onChange={(e) => handleTerapijaChange(dijagnozaIndex, terapijaIndex, 'opis', e.target.value)}
                                                        placeholder="Detaljni opis terapije"
                                                        rows="2"
                                                    />
                                                </div>
                                                <div className="form-group">
                                                    <label>Trajanje</label>
                                                    <input
                                                        type="text"
                                                        value={terapija.trajanje}
                                                        onChange={(e) => handleTerapijaChange(dijagnozaIndex, terapijaIndex, 'trajanje', e.target.value)}
                                                        placeholder="npr. 7 dana"
                                                    />
                                                </div>
                                            </div>
                                        </div>
                                    ))}

                                    <button
                                        type="button"
                                        onClick={() => addTerapija(dijagnozaIndex)}
                                        className="add-terapija-btn"
                                    >
                                        + Add Terapija
                                    </button>
                                </div>
                            </div>
                        ))}

                        <button
                            type="button"
                            onClick={addDijagnoza}
                            className="add-dijagnoza-btn"
                        >
                            + Add Dijagnoza
                        </button>
                    </div>

                    <div className="form-actions">
                        <button onClick={handleAddNewEntry} className="save-btn" id="boja">Save Medical Record</button>
                        <button onClick={() => setShowAddForm(false)} className="cancel-btn" id="boja1">Cancel</button>
                    </div>
                </div>
            )}

            {/* Medical History Display */}
            {selectedPatientId && (
                <div className="medical-history-section">
                    <h3>Medical History</h3>
                    <div className="history-entries">
                        {selectedHistory.length === 0 ? (
                            <div className="no-data">No medical records found for this patient.</div>
                        ) : (
                            selectedHistory.map((entry) => (
                                <div key={entry.id} className="karton-entry">
                                    {editingEntryId === entry.id ? (
                                        <div className="edit-entry-form">
                                            <h4>Edit Medical Record</h4>
                                            <div className="form-row">
                                                <div className="form-group">
                                                    <label>Date Created</label>
                                                    <input
                                                        type="datetime-local"
                                                        name="datumKreiranja"
                                                        value={currentEditData.datumKreiranja || ''}
                                                        onChange={handleChangeEdit}
                                                    />
                                                </div>
                                                <div className="form-group">
                                                    <label>Medical Record Number</label>
                                                    <input
                                                        type="text"
                                                        name="brojKartona"
                                                        value={currentEditData.brojKartona || ''}
                                                        onChange={handleChangeEdit}
                                                    />
                                                </div>
                                            </div>
                                            <div className="form-actions">
                                                <button onClick={() => handleSaveEdit(entry.id)} className="save-btn">Save</button>
                                                <button onClick={handleCancelEdit} className="cancel-btn">Cancel</button>
                                            </div>
                                        </div>
                                    ) : (
                                        <>
                                            <div className="karton-header">
                                                <h4>Medical Record #{entry.brojKartona}</h4>
                                                <p><strong>Date Created:</strong> {new Date(entry.datumKreiranja).toLocaleDateString()}</p>
                                                <p><strong>Patient ID:</strong> {entry.patientId}</p>
                                            </div>

                                            {/* Dijagnoze Section */}
                                            <div className="dijagnoze-section">
                                                <h5>Dijagnoze</h5>
                                                {entry.dijagnoze && entry.dijagnoze.length > 0 ? (
                                                    entry.dijagnoze.map((dijagnoza, index) => (
                                                        <div key={index} className="dijagnoza-entry">
                                                            <div className="dijagnoza-info">
                                                                <p><strong>Naziv:</strong> {dijagnoza.nazivDijagnoze}</p>
                                                                <p><strong>Opis:</strong> {dijagnoza.opis || 'Nema opisa'}</p>
                                                                <p><strong>Datum:</strong> {dijagnoza.datumDijagnoze ? new Date(dijagnoza.datumDijagnoze).toLocaleDateString() : 'Nije specificirano'}</p>
                                                            </div>

                                                            {/* Terapije Section */}
                                                            <div className="terapije-section">
                                                                <h4>Terapije</h4>
                                                                {dijagnoza.terapije && dijagnoza.terapije.length > 0 ? (
                                                                    dijagnoza.terapije.map((terapija, tIndex) => (
                                                                        <div key={tIndex} className="terapija-entry">
                                                                            <p style={{ color: '#0056b3' }}><strong>Naziv:</strong> {terapija.nazivTerapije}</p>
                                                                            <p><strong>Opis:</strong> {terapija.opis || 'Nema opisa'}</p>
                                                                            <p><strong>Doziranje:</strong> {terapija.doziranje || 'Nije specificirano'}</p>
                                                                            <p><strong>Trajanje:</strong> {terapija.trajanje || 'Nije specificirano'}</p>
                                                                        </div>
                                                                    ))
                                                                ) : (
                                                                    <div className="no-data">Nema terapija za ovu dijagnozu.</div>
                                                                )}
                                                            </div>
                                                        </div>
                                                    ))
                                                ) : (
                                                    <div className="no-data">Nema dijagnoza za ovaj karton.</div>
                                                )}
                                            </div>

                                            <div className="entry-actions">
                                                <button onClick={() => handleEdit(entry)} className="edit-btn">Edit</button>
                                                <button onClick={() => handleDeleteEntry(entry.id)} className="delete-btn">Delete</button>
                                            </div>
                                        </>
                                    )}
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}