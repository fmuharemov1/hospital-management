import './App.css';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Register from './components/Register';
import Login from './components/Login';
import About from './components/About';
import Profile from './components/Profile';
import Appointments from './components/Appointments';
import Invoice from './components/Invoice';
import Reports from './components/Reports';
import PatientEMR from './components/PatientEMR';
import ProtectedRoute from './components/ProtectedRoute';


function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <nav className="navbar">
          <Link to="/" className="nav-link">Home</Link>
          <Link to="/about" className="nav-link">About Us</Link>
        </nav>
        <Routes>
          {/* Prikazuje naslovnu stranicu (landing) */}
          <Route path="/" element={
            <header className="hero">
              <div className="hero-left">
                <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
                <h1>Welcome To</h1>
                <h2>Patient Care & Hospital Management System</h2>
                <div className="buttons">
                  <Link to="/login" className="btn">Login</Link>
                  <Link to="/register" className="btn">Sign Up</Link>
                </div>
              </div>
              <div className="hero-right">
                <img src="/doctor.png" alt="Doctors" className="hero-image" />
              </div>
            </header>
          } />

          {/* Registracija i login prikazuju samo forme */}
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          <Route path="/about" element={<About />} />
        <Route path="/profile" element={
          <ProtectedRoute><Profile /></ProtectedRoute>
        } />

        <Route path="/appointments" element={
          <ProtectedRoute><Appointments /></ProtectedRoute>
        } />

        <Route path="/invoices" element={
          <ProtectedRoute><Invoice /></ProtectedRoute>
        } />

        <Route path="/reports" element={
          <ProtectedRoute><Reports /></ProtectedRoute>
        } />

        <Route path="/patient-emr" element={
          <ProtectedRoute><PatientEMR /></ProtectedRoute>
        } />

        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;