// Invoice.jsx (ispravljen)
import React, { useState, useEffect } from 'react';
import './Invoice.css';
import { Link } from 'react-router-dom';
import axios from '../api'; // VAŽNO: koristi centralizovani axios instance

export default function Invoice() {
    const [patients, setPatients] = useState([]);
    const [selectedPatientId, setSelectedPatientId] = useState('');
    const [editingInvoiceId, setEditingInvoiceId] = useState(null);
    const [currentEditData, setCurrentEditData] = useState({});
    const [showAddForm, setShowAddForm] = useState(false);
    const [newInvoiceData, setNewInvoiceData] = useState({
        date: '', department: '', doctor: '', price: 0, paid: false
    });
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPatientsWithInvoices = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const response = await axios.get('/client/patients-with-invoices');
                setPatients(response.data);
            } catch (err) {
                console.error("Failed to fetch patients and invoices:", err);
                setError("Failed to load data. Please try again later.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchPatientsWithInvoices();
    }, []);

    const selectedPatient = patients.find(p => p.id === Number(selectedPatientId));

    const handleEdit = (invoiceToEdit) => {
        setEditingInvoiceId(invoiceToEdit.id);
        setCurrentEditData({ ...invoiceToEdit });
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setCurrentEditData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSave = async (invoiceId) => {
        try {
            await axios.put(`/client/invoices/${invoiceId}`, currentEditData);
            setPatients(prev => prev.map(p => {
                if (p.id === Number(selectedPatientId)) {
                    const updatedInvoices = p.invoices.map(inv => inv.id === invoiceId ? currentEditData : inv);
                    return { ...p, invoices: updatedInvoices };
                }
                return p;
            }));
            setEditingInvoiceId(null);
            setCurrentEditData({});
            alert('Faktura uspješno ažurirana!');
        } catch (err) {
            console.error("Greška pri spremanju fakture:", err);
            setError("Greška pri spremanju fakture: " + err.message);
        }
    };

    const handleCancel = () => {
        setEditingInvoiceId(null);
        setCurrentEditData({});
    };

    const handleChangeNewInvoice = (e) => {
        const { name, value, type, checked } = e.target;
        setNewInvoiceData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleAddInvoice = async () => {
        if (!selectedPatientId || !newInvoiceData.date || !newInvoiceData.department || !newInvoiceData.doctor || !newInvoiceData.price) {
            alert("Molimo popunite sva obavezna polja.");
            return;
        }
        try {
            const response = await axios.post('/client/invoices', {
                ...newInvoiceData,
                patientId: Number(selectedPatientId)
            });
            const addedInvoice = response.data;
            setPatients(prev => prev.map(p => {
                if (p.id === Number(selectedPatientId)) {
                    const updated = [...(p.invoices || []), addedInvoice];
                    return { ...p, invoices: updated.sort((a, b) => a.date.localeCompare(b.date)) };
                }
                return p;
            }));
            setNewInvoiceData({ date: '', department: '', doctor: '', price: 0, paid: false });
            setShowAddForm(false);
            alert('Nova faktura uspješno dodana!');
        } catch (err) {
            console.error("Greška pri dodavanju fakture:", err);
            setError("Greška pri dodavanju fakture: " + err.message);
        }
    };

    const handleDeleteInvoice = async (invoiceId) => {
        if (!window.confirm("Jeste li sigurni da želite obrisati ovu fakturu?")) return;
        try {
            await axios.delete(`/client/invoices/${invoiceId}`);
            setPatients(prev => prev.map(p => {
                if (p.id === Number(selectedPatientId)) {
                    const updated = p.invoices.filter(inv => inv.id !== invoiceId);
                    return { ...p, invoices: updated };
                }
                return p;
            }));
            alert('Faktura uspješno obrisana!');
        } catch (err) {
            console.error("Greška pri brisanju fakture:", err);
            setError("Greška pri brisanju fakture: " + err.message);
        }
    };

    if (isLoading) return <div className="invoice-page">Loading data...</div>;
    if (error) return <div className="invoice-page error-message">{error}</div>;

    return (
        <div className="invoice-page">
            <nav className="navbar">
                <Link to="/invoices" className="nav-link active">Invoices</Link>
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
                    onChange={(e) => {
                        setSelectedPatientId(e.target.value);
                        setEditingInvoiceId(null);
                        setCurrentEditData({});
                        setShowAddForm(false);
                        setNewInvoiceData({ date: '', department: '', doctor: '', price: 0, paid: false });
                    }}>
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

                    <div className="add-invoice-section">
                        <button onClick={() => setShowAddForm(!showAddForm)} className="add-new-button">
                            {showAddForm ? 'Hide Add Form' : 'Add New Invoice'}
                        </button>
                        {showAddForm && (
                            <div className="add-form">
                                <h3>Add New Invoice Details</h3>
                                <div className="form-row">
                                    <label>Date: <input type="date" name="date" value={newInvoiceData.date} onChange={handleChangeNewInvoice} /></label>
                                    <label>Department: <input type="text" name="department" value={newInvoiceData.department} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-row">
                                    <label>Doctor: <input type="text" name="doctor" value={newInvoiceData.doctor} onChange={handleChangeNewInvoice} /></label>
                                    <label>Price (€): <input type="number" name="price" value={newInvoiceData.price} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-row full-width">
                                    <label>Paid: <input type="checkbox" name="paid" checked={newInvoiceData.paid} onChange={handleChangeNewInvoice} /></label>
                                </div>
                                <div className="form-actions">
                                    <button onClick={handleAddInvoice} className="save-button">Add Invoice</button>
                                    <button onClick={() => setShowAddForm(false)} className="cancel-button">Cancel</button>
                                </div>
                            </div>
                        )}
                    </div>

                    <table className="invoice-table">
                        <thead>
                            <tr>
                                <th>Date</th><th>Department</th><th>Doctor</th><th>Price (€)</th><th>Status</th><th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {selectedPatient.invoices?.length > 0 ? (
                                selectedPatient.invoices.map(inv => (
                                    <tr key={inv.id}>
                                        <td>{editingInvoiceId === inv.id ? <input type="date" name="date" value={currentEditData.date} onChange={handleChange} /> : inv.date}</td>
                                        <td>{editingInvoiceId === inv.id ? <input type="text" name="department" value={currentEditData.department} onChange={handleChange} /> : inv.department}</td>
                                        <td>{editingInvoiceId === inv.id ? <input type="text" name="doctor" value={currentEditData.doctor} onChange={handleChange} /> : inv.doctor}</td>
                                        <td>{editingInvoiceId === inv.id ? <input type="number" name="price" value={currentEditData.price} onChange={handleChange} /> : inv.price}</td>
                                        <td className={inv.paid ? 'paid' : 'unpaid'}>{editingInvoiceId === inv.id ? <input type="checkbox" name="paid" checked={currentEditData.paid} onChange={handleChange} /> : inv.paid ? 'Paid' : 'Not Paid'}</td>
                                        <td>
                                            {editingInvoiceId === inv.id ? (
                                                <>
                                                    <button onClick={() => handleSave(inv.id)} className="save-button">Save</button>
                                                    <button onClick={handleCancel} className="cancel-button">Cancel</button>
                                                </>
                                            ) : (
                                                <>
                                                    <button onClick={() => handleEdit(inv)} className="edit-button">Edit</button>
                                                    <button onClick={() => handleDeleteInvoice(inv.id)} className="delete-button">Delete</button>
                                                </>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr><td colSpan="6">Nema faktura za ovog pacijenta.</td></tr>
                            )}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
}
