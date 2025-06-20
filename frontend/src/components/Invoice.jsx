import React, { useState, useEffect } from 'react';
import './Invoice.css'; // Uvezite vaše CSS stilove
import { Link } from 'react-router-dom';

export default function Invoice() {
    // Hardkodirani početni podaci za pacijente i fakture
    // Ovi podaci će biti prikazani direktno bez poziva backenda
    const initialPatients = [
        {
            id: 1,
            fullName: 'Harun Beslic',
            email: 'hbeslic@example.com',
            phone: '+38761123456',
            invoices: [
                { id: 101, date: '2024-06-01', department: 'Kardiologija', doctor: 'Dr. Almir Alagic', price: 150.00, paid: true },
                { id: 102, date: '2024-05-15', department: 'Opsta praksa', doctor: 'Dr. Lejla Basic', price: 75.00, paid: false },
            ]
        },
        {
            id: 2,
            fullName: 'Marko Markovic',
            email: 'marko.markovic@example.com',
            phone: '+38761987654',
            invoices: [
                { id: 201, date: '2024-06-10', department: 'Dermatologija', doctor: 'Dr. Emina Hodzic', price: 200.00, paid: true },
                { id: 202, date: '2024-04-20', department: 'Pedijatrija', doctor: 'Dr. Mirza Mujic', price: 120.00, paid: false },
            ]
        },
        // Možete dodati još pacijenata po potrebi
    ];

    // Stanje za pacijente, inicijalizirano sa hardkodiranim podacima
    const [patients, setPatients] = useState(initialPatients);
    const [selectedPatientId, setSelectedPatientId] = useState('');

    // Stanja za funkcionalnost uređivanja
    const [editingInvoiceId, setEditingInvoiceId] = useState(null);
    const [currentEditData, setCurrentEditData] = useState({});

    // Stanja za funkcionalnost dodavanja nove fakture
    const [showAddForm, setShowAddForm] = useState(false);
    const [newInvoiceData, setNewInvoiceData] = useState({
        date: '', department: '', doctor: '', price: 0, paid: false
    });

    // Uklonjeni isLoading i error jer nema poziva backenda
    // const [isLoading, setIsLoading] = useState(true);
    // const [error, setError] = useState(null);

    // Nema useEffect za dohvaćanje podataka jer su podaci hardkodirani
    // useEffect(() => {
    //     // fetchPatientsWithInvoices();
    // }, []);

    const selectedPatient = patients.find(p => p.id === Number(selectedPatientId));

    // Funkcija za obradu klika na "Edit" dugme
    const handleEdit = (invoiceToEdit) => {
        setEditingInvoiceId(invoiceToEdit.id);
        setCurrentEditData({ ...invoiceToEdit });
    };

    // Funkcija za obradu promjena u input poljima tokom uređivanja
    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setCurrentEditData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    // Funkcija za spremanje izmjena (sada samo u lokalno stanje)
    const handleSave = (invoiceId) => {
        // Ažuriraj lokalno stanje nakon "uspješnog" spremanja
        setPatients(prev => prev.map(p => {
            if (p.id === Number(selectedPatientId)) {
                const updatedInvoices = p.invoices.map(inv => inv.id === invoiceId ? currentEditData : inv);
                return { ...p, invoices: updatedInvoices };
            }
            return p;
        }));
        setEditingInvoiceId(null);
        setCurrentEditData({});
        alert('Faktura uspješno ažurirana!'); // Promijenjena poruka
    };

    // Funkcija za otkazivanje uređivanja
    const handleCancel = () => {
        setEditingInvoiceId(null);
        setCurrentEditData({});
    };

    // Funkcija za obradu promjena u input poljima za novu fakturu
    const handleChangeNewInvoice = (e) => {
        const { name, value, type, checked } = e.target;
        setNewInvoiceData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    // Funkcija za dodavanje nove fakture (sada samo u lokalno stanje)
    const handleAddInvoice = () => {
        if (!selectedPatientId) {
            alert("Molimo odaberite pacijenta prije dodavanja fakture.");
            return;
        }
        if (!newInvoiceData.date || !newInvoiceData.department || !newInvoiceData.doctor || !newInvoiceData.price) {
            alert("Molimo popunite sva obavezna polja za novu fakturu (Datum, Odjel, Doktor, Cijena).");
            return;
        }

        // Generiši privremeni ID za novu fakturu
        const newId = Date.now(); // Jednostavan način za generisanje unikatnog ID-a
        const addedInvoice = {
            id: newId,
            ...newInvoiceData,
            // pacijentId: Number(selectedPatientId) // Ne treba nam pacijentId u samoj fakturi ako je unutar patient.invoices
        };

        // Ažuriraj lokalno stanje dodavanjem nove fakture
        setPatients(prevPatients => prevPatients.map(patient => {
            if (patient.id === Number(selectedPatientId)) {
                const updatedInvoices = [...(patient.invoices || []), addedInvoice];
                updatedInvoices.sort((a, b) => a.date.localeCompare(b.date)); // Sortiraj po datumu
                return { ...patient, invoices: updatedInvoices };
            }
            return patient;
        }));

        setNewInvoiceData({ date: '', department: '', doctor: '', price: 0, paid: false }); // Resetuj formu
        setShowAddForm(false); // Sakrij formu
        alert('Nova faktura uspješno dodana!'); // Promijenjena poruka
    };

    // Funkcija za brisanje fakture (sada samo iz lokalnog stanja)
    const handleDeleteInvoice = (invoiceId) => {
        if (!window.confirm("Jeste li sigurni da želite obrisati ovu fakturu?")) {
            return;
        }

        // Ažuriraj lokalno stanje uklanjanjem fakture
        setPatients(prevPatients => prevPatients.map(patient => {
            if (patient.id === Number(selectedPatientId)) {
                const updatedInvoices = patient.invoices.filter(inv => inv.id !== invoiceId);
                return { ...patient, invoices: updatedInvoices };
            }
            return patient;
        }));

        alert('Faktura uspješno obrisana!'); // Promijenjena poruka
    };

    // Uklonjeni isLoading i error uslovi renderovanja
    // if (isLoading) {
    //     return <div className="invoice-page">Učitavanje podataka...</div>;
    // }
    // if (error) {
    //     return <div className="invoice-page error-message">{error}</div>;
    // }

    return (
        <div className="invoice-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link active">Invoices</Link>
                <Link to="/reports" className="nav-link">Reports</Link>
                <Link to="/" className="nav-link">Log out</Link>
            </nav>

            <h1>Fakture</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <div className="select-patient">
                <label htmlFor="patient">Odaberi pacijenta:</label>
                <select
                    id="patient"
                    value={selectedPatientId}
                    onChange={(e) => {
                        setSelectedPatientId(e.target.value);
                        setEditingInvoiceId(null); // Resetuj edit mod kad se pacijent promijeni
                        setCurrentEditData({});
                        setShowAddForm(false); // Sakrij formu za dodavanje
                        setNewInvoiceData({ date: '', department: '', doctor: '', price: 0, paid: false }); // Resetuj podatke nove fakture
                    }}
                >
                    <option value="">-- Odaberite pacijenta --</option>
                    {patients.map((p) => (
                        <option key={p.id} value={p.id}>{p.fullName}</option>
                    ))}
                </select>
            </div>

            {selectedPatient && (
                <>
                    <div className="user-info">
                        <p><strong>Ime:</strong> {selectedPatient.fullName}</p>
                        <p><strong>Email:</strong> {selectedPatient.email}</p>
                        <p><strong>Telefon:</strong> {selectedPatient.phone}</p>
                    </div>

                    <div className="add-invoice-section">
                        <button onClick={() => setShowAddForm(!showAddForm)} className="add-new-button">
                            {showAddForm ? 'Sakrij formu za dodavanje' : 'Dodaj novu fakturu'}
                        </button>

                        {showAddForm && (
                            <div className="add-form">
                                <h3>Detalji nove fakture</h3>
                                <div className="form-row">
                                    <label>Datum: <input type="date" name="date" value={newInvoiceData.date} onChange={handleChangeNewInvoice} /></label>
                                    <label>Odjel: <input type="text" name="department" value={newInvoiceData.department} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-row">
                                    <label>Doktor: <input type="text" name="doctor" value={newInvoiceData.doctor} onChange={handleChangeNewInvoice} /></label>
                                    <label>Cijena (€): <input type="number" name="price" value={newInvoiceData.price} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-row full-width">
                                    <label>Plaćeno: <input type="checkbox" name="paid" checked={newInvoiceData.paid} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-actions">
                                    <button onClick={handleAddInvoice} className="save-button">Dodaj fakturu</button>
                                    <button onClick={() => setShowAddForm(false)} className="cancel-button">Odustani</button>
                                </div>
                            </div>
                        )}
                    </div>

                    <table className="invoice-table">
                        <thead>
                        <tr>
                            <th>Datum</th>
                            <th>Odjel</th>
                            <th>Doktor</th>
                            <th>Cijena (€)</th>
                            <th>Status</th>
                            <th>Akcije</th>
                        </tr>
                        </thead>
                        <tbody>
                        {selectedPatient.invoices && selectedPatient.invoices.length > 0 ? (
                            selectedPatient.invoices.map((inv) => (
                                <tr key={inv.id}>
                                    <td>
                                        {editingInvoiceId === inv.id ? (
                                            <input type="date" name="date" value={currentEditData.date} onChange={handleChange} />
                                        ) : (
                                            inv.date
                                        )}
                                    </td>
                                    <td>
                                        {editingInvoiceId === inv.id ? (
                                            <input type="text" name="department" value={currentEditData.department} onChange={handleChange} />
                                        ) : (
                                            inv.department
                                        )}
                                    </td>
                                    <td>
                                        {editingInvoiceId === inv.id ? (
                                            <input type="text" name="doctor" value={currentEditData.doctor} onChange={handleChange} />
                                        ) : (
                                            inv.doctor
                                        )}
                                    </td>
                                    <td>
                                        {editingInvoiceId === inv.id ? (
                                            <input type="number" name="price" value={currentEditData.price} onChange={handleChange} />
                                        ) : (
                                            inv.price
                                        )}
                                    </td>
                                    <td className={inv.paid ? 'paid' : 'unpaid'}>
                                        {editingInvoiceId === inv.id ? (
                                            <input type="checkbox" name="paid" checked={currentEditData.paid} onChange={handleChange} />
                                        ) : (
                                            inv.paid ? 'Plaćeno' : 'Nije plaćeno'
                                        )}
                                    </td>
                                    <td>
                                        {editingInvoiceId === inv.id ? (
                                            <>
                                                <button onClick={() => handleSave(inv.id)} className="save-button">Spremi</button>
                                                <button onClick={handleCancel} className="cancel-button">Odustani</button>
                                            </>
                                        ) : (
                                            <>
                                                <button onClick={() => handleEdit(inv)} className="edit-button">Uredi</button>
                                                <button onClick={() => handleDeleteInvoice(inv.id)} className="delete-button">Obriši</button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="6">Nema faktura za ovog pacijenta.</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
}
