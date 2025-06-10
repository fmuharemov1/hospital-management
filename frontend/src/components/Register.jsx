import React, { useState } from 'react';
import axios from '../api';
import '../AuthForm.css';
import { useNavigate } from 'react-router-dom';
import './Login.css';

export default function Register() {
  const [form, setForm] = useState({
    name: '',
    surname: '',
    email: '',
    username: '',
    password: ''
  });

  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/users/register', form);

      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        setMessage('✅ Uspješna registracija! Preusmjeravam...');
        setTimeout(() => navigate('/login'), 1500);
      } else {
        throw new Error('Token nije vraćen sa servera.');
      }
    } catch (err) {
      const errorMsg =
        err.response?.data?.message ||
        err.response?.data ||
        'Registracija nije uspjela';
      setMessage('❌ Greška: ' + errorMsg);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-600">
      <img
        src="/medapp-logo-removebg-preview.png"
        alt="MedApp Clinics Logo"
        className="logo"
      />
      <form
        className="bg-white p-8 rounded-xl shadow-md w-96 space-y-4"
        onSubmit={handleRegister}
      >
        <h2 className="text-2xl font-bold text-center">Registracija</h2>
        {[
          { name: 'name', placeholder: 'Ime' },
          { name: 'surname', placeholder: 'Prezime' },
          { name: 'email', placeholder: 'Email' },
          { name: 'username', placeholder: 'Korisničko ime' },
          { name: 'password', placeholder: 'Lozinka', type: 'password' }
        ].map(({ name, placeholder, type }) => (
          <input
            key={name}
            type={type || 'text'}
            name={name}
            placeholder={placeholder}
            value={form[name]}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        ))}

        <button
          type="submit"
          className="w-full bg-green-500 hover:bg-green-600 text-white py-2 rounded"
        >
          Sign Up
        </button>

        {message && (
          <p className="text-center text-sm text-red-500 mt-2">{message}</p>
        )}
      </form>
    </div>
  );
}
