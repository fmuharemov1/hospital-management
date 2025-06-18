import React, { useState, useEffect } from 'react';
import './Profile.css';
import { Link, useNavigate } from 'react-router-dom';

export default function Profile() {
    // Inicijalna stanja za korisničke podatke, status učitavanja i greške
    const [user, setUser] = useState({
        username: 'Učitavam...',
        fullName: 'Učitavam...',
        email: 'Učitavam...',
        phone: 'Učitavam...',
        roles: []
    });
    const [isLoading, setIsLoading] = useState(true); // Stanje za prikaz loading indikatora
    const [error, setError] = useState(null); // Stanje za greške
    const navigate = useNavigate();

    // Funkcija za dohvatanje JWT tokena iz lokalnog skladišta
    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // useEffect hook koji se pokreće prilikom montiranja komponente
    useEffect(() => {
        const fetchUserProfile = async () => {
            setIsLoading(true); // Postavi loading stanje na true
            setError(null); // Resetuj prethodne greške

            const token = getAuthToken(); // Dohvati token
            if (!token) {
                // Ako nema tokena, postavi grešku i preusmjeri na login
                setError("Autentifikacijski token nije pronađen. Molimo prijavite se.");
                navigate('/login');
                setIsLoading(false);
                return;
            }

            try {
                // Poziv na backend endpoint za dohvat profila korisnika
                // Pretpostavljamo da će client-service imati endpoint poput /api/client/users/me
                // koji vraća podatke prijavljenog korisnika na osnovu JWT-a.
                const response = await fetch('http://localhost:8092/api/client/users/me', {
                    method: 'GET', // Metoda bi trebala biti GET
                    headers: {
                        'Authorization': `Bearer ${token}`, // Dodajte JWT token u Authorization header
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    // Ako odgovor nije OK (npr. 401, 403, 500), pročitaj poruku o grešci
                    const errorData = await response.text();
                    throw new Error(`HTTP greška! Status: ${response.status} - ${errorData}`);
                }

                // Ako je odgovor uspješan, parsiraj JSON podatke
                const data = await response.json();
                setUser({
                    username: data.username || 'N/A',
                    fullName: data.fullName || 'N/A',
                    email: data.email || 'N/A',
                    phone: data.phone || 'N/A',
                    roles: data.roles || [] // Dohvatite uloge ako ih backend vraća
                });

            } catch (err) {
                // Uhvati greške prilikom dohvaćanja podataka
                console.error("Greška pri dohvaćanju profila korisnika:", err);
                setError(`Greška pri učitavanju profila: ${err.message}. Molimo pokušajte ponovo.`);
            } finally {
                setIsLoading(false); // Bez obzira na uspjeh/neuspeh, završi loading
            }
        };

        fetchUserProfile(); // Pozovi funkciju za dohvat profila
    }, [navigate]); // navigate je dodan kao zavisnost

    // Funkcija za odjavu korisnika
    const handleLogout = () => {
        localStorage.removeItem('token'); // Ukloni token
        navigate('/login'); // Preusmjeri na login stranicu
    };

    // Prikaz loading stanja
    if (isLoading) {
        return <div className="profile-page">Učitavam profil...</div>;
    }

    // Prikaz greške
    if (error) {
        return (
            <div className="profile-page error-message">
                <p>{error}</p>
                <Link to="/login">Idi na Login</Link>
            </div>
        );
    }

    return (
        <div className="profile-page">
            <nav className="navbar">
                <Link to="/profile" className="nav-link active">Profil</Link>
                <Link to="/appointments" className="nav-link">Termini</Link>
                <Link to="/emr" className="nav-link">E-karton</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Odjavi se</button>
            </nav>
            <h1>Moj račun</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <form className="profile-form">
                <label>
                    Korisničko ime
                    <input type="text" value={user.username} readOnly />
                </label>
                <label>
                    Puno ime
                    <input type="text" value={user.fullName} readOnly />
                </label>
                <label>
                    Email
                    <input type="email" value={user.email} readOnly />
                </label>
                <label>
                    Uloge
                    <input type="text" value={user.roles.join(', ')} readOnly />
                </label>
            </form>
        </div>
    );
}