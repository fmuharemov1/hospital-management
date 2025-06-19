import React, { useState } from 'react';
import axios from '../api.js';
import '../AuthForm.css';
import './Login.css';

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState(''); // 'success' ili 'error'

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage(''); // Resetuj poruke pri svakom pokušaju prijave
    setMessageType('');

    try {
      const response = await axios.post('/users/login', {
        username: form.username,
        password: form.password
      });

      const { token, role } = response.data; // Dohvati token I ULOGU iz odgovora!
      localStorage.setItem('token', token); // Sačuvaj token

      setMessage('Uspješno ste se prijavili! Preusmjeravam...');
      setMessageType('success');

      // Preusmjeravanje na osnovu uloge
      switch (role) {
        case 'USER':
          setTimeout(() => { window.location.href = "/patient-emr"; }, 1500); // Kratka pauza za čitanje poruke
          break;
        case 'DOCTOR':
          setTimeout(() => { window.location.href = "/emr"; }, 1500);
          break;
        case 'ADMIN':
          setTimeout(() => { window.location.href = "/reports"; }, 1500);
          break;
        default:
          setMessage('Nepoznata uloga. Preusmjeravam na početnu stranicu.');
          setMessageType('error');
          setTimeout(() => { window.location.href = "/"; }, 1500);
      }

    } catch (err) {
      console.error(err);
      setMessageType('error'); // Postavi tip poruke na grešku

      if (err.response) {
        // Server je odgovorio sa status kodom izvan 2xx raspona
        if (err.response.status === 401) {
          setMessage('Pogrešno korisničko ime ili lozinka. Molimo pokušajte ponovo.');
        } else if (err.response.status === 403) {
          setMessage('Pogrešno korisničko ime ili lozinka. Molimo pokušajte ponovo.');
        } else if (err.response.status === 404) {
          setMessage('Korisnik sa tim imenom nije pronađen.');
        } else if (err.response.status === 500) {
          setMessage('Greška na serveru. Molimo pokušajte ponovo kasnije.');
        } else {
          setMessage('Došlo je do greške: ' + (err.response.data.message || 'Pokušaj prijave nije uspio.'));
        }
      } else if (err.request) {
        // Zahtev je poslat, ali nije primljen odgovor (npr. server nije dostupan)
        setMessage('Server nije dostupan. Provjerite vašu internetsku vezu ili pokušajte ponovo kasnije.');
      } else {
        // Nešto se dogodilo pri postavljanju zahtjeva
        setMessage('Neočekivana greška. Molimo pokušajte ponovo.');
      }
    }
  };

  return (
      <div className="min-h-screen flex items-center justify-center bg-green-600">
        <form className="bg-white p-8 rounded-xl shadow-md w-96 space-y-4" onSubmit={handleLogin}>
          <h2 className="text-2xl font-bold text-center">Login</h2>
          <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
          <input
              type="text"
              name="username"
              placeholder="Korisničko ime" /* Ažuriran placeholder */ // <-- ISPRAVKA OVDJE
              value={form.username}
              onChange={handleChange}
              className="w-full border rounded px-3 py-2"
              required
          />
          <input
              type="password"
              name="password"
              placeholder="Lozinka" /* Ažuriran placeholder */ // <-- ISPRAVKA OVDJE
              value={form.password}
              onChange={handleChange}
              className="w-full border rounded px-3 py-2"
              required
          />
          <button type="submit" className="w-full bg-green-500 text-white py-2 rounded">Prijavi se</button>
          {message && (
              <p className={`text-center text-sm ${messageType === 'success' ? 'text-green-600' : 'text-red-500'}`}>
                {message}
              </p>
          )}
        </form>
      </div>
  );
}