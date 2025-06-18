import React, { useState } from 'react';
import axios from '../api.js'; // OBAVEZNO da u api.js bude postavljen baseURL na http://localhost:8085/api
import '../AuthForm.css';
import './Login.css';

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/users/login', {
        username: form.username,
        password: form.password
      });

      localStorage.setItem('token', response.data.token);
      setMessage('Uspješno logovanje!');
    window.location.href = "/profile";
    } catch (err) {
      console.error(err);
      if (err.response?.status === 500) {
        setMessage('Greška na serveru – provjerite podatke.');
      } else {
        setMessage('Greška: ' + (err.response?.data?.message || 'Login nije uspio'));
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
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          className="w-full border rounded px-3 py-2"
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="w-full border rounded px-3 py-2"
          required
        />
        <button type="submit" className="w-full bg-green-500 text-white py-2 rounded">Login</button>
        {message && <p className="text-center text-sm text-red-500">{message}</p>}
      </form>
    </div>
  );
}