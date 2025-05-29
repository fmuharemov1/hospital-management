import './App.css';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Register from './components/Register';
import Login from './components/Login';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          {/* Prikazuje naslovnu stranicu (landing) */}
          <Route path="/" element={
            <header className="hero">
              <div className="hero-left">
                <img src="/medapp-logo.png" alt="MedApp Clinics Logo" className="logo" />
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
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
