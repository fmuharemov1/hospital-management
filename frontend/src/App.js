import './App.css';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Register from './components/Register';
import Login from './components/Login';
import About from './components/About';
import Profile from './components/Profile';
import Appointments from './components/Appointments';
import Invoice from './components/Invoice';
import Reports from './components/Reports';
import MedicalRecord from './components/MedicalRecord';
import Rooms from './components/Rooms';
import DrAppointment from './components/DrAppointments';
import PatientEMR from "./components/PatientEMR";
import ProtectedRoute from './components/ProtectedRoute';
import Unauthorized from './components/Unauthorized';
import RoleBasedNavigation from './components/RoleBasedNavigation';

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
            <Route path="/unauthorized" element={<Unauthorized />} />


            {/* Svi ulogovani korisnici */}
            <Route path="/profile" element={
              <ProtectedRoute allowedRoles={['USER']}>
                <Profile />
              </ProtectedRoute>
            } />

            <Route path="/appointments" element={
              <ProtectedRoute allowedRoles={['DOCTOR', 'USER']}>
                <Appointments />
              </ProtectedRoute>
            } />
            {/* Samo ADMIN i STAFF mogu pristupiti fakturama */}
            <Route path="/invoices" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <Invoice />
              </ProtectedRoute>
            } />
            {/* Samo ADMIN može pristupiti izvještajima */}
            <Route path="/reports" element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <Reports />
              </ProtectedRoute>
            } />
            {/* DOCTOR i ADMIN mogu pristupiti EMR */}
            <Route path="/emr" element={
              <ProtectedRoute allowedRoles={[ 'DOCTOR']}>
                <MedicalRecord />
              </ProtectedRoute>
            } />
            {/* Samo DOCTOR može pristupiti svojim terminima */}
            <Route path="/dr-appointments" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <DrAppointment />
              </ProtectedRoute>
            } />
            {/* STAFF i ADMIN mogu pristupiti sobama */}
            <Route path="/rooms" element={
              <ProtectedRoute allowedRoles={['DOCTOR']}>
                <Rooms />
              </ProtectedRoute>
            } />
            {/* Patient EMR - svi ulogovani */}
            <Route path="/patient-emr" element={
              <ProtectedRoute allowedRoles={[ 'USER']}>
                <PatientEMR />
              </ProtectedRoute>
            } />
          </Routes>
        </div>
      </BrowserRouter>
  );
}

export default App;