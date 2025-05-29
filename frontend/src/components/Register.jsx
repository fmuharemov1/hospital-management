import React, { useState } from 'react';
import axios from '../api';
import '../AuthForm.css';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [form, setForm] = useState({ ime: '', prezime: '', email: '', username: '', password: '' });
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };
const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/client-service/api/users/register', form);
      localStorage.setItem('token', response.data.token);
      setMessage('✅ Uspješna registracija! Preusmjeravam...');
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setMessage('❌ Greška: ' + (err.response?.data?.message || 'Registracija nije uspjela'));
    }
  };


  return (
    <div className="min-h-screen flex items-center justify-center bg-green-600">
      <form className="bg-white p-8 rounded-xl shadow-md w-96 space-y-4" onSubmit={handleRegister}>
        <h2 className="text-2xl font-bold text-center">Registracija</h2>
        {['ime', 'prezime', 'email', 'username', 'password'].map((field) => (
          <input
            key={field}
            type={field === 'password' ? 'password' : 'text'}
            name={field}
            placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
            value={form[field]}
            onChange={handleChange}
            className="w-full border rounded px-3 py-2"
            required
          />
        ))}
        <button type="submit" className="w-full bg-green-500 text-white py-2 rounded">Sign Up</button>
        {message && <p className="text-center text-sm text-red-500">{message}</p>}
      </form>
    </div>
  );
}