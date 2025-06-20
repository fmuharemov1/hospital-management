import React from 'react';
import { Link } from 'react-router-dom';

const RoleBasedNavigation = () => {
    const getUserRole = () => {
        const token = localStorage.getItem('token');
        if (!token) return null;

        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.roles || [];
        } catch {
            return null;
        }
    };

    const userRoles = getUserRole();
    const hasRole = (role) => userRoles &&
        (userRoles.includes(role) || userRoles.includes(`ROLE_${role}`));

    if (!userRoles) return null;

    return (
        <nav className="navbar">
            <Link to="/" className="nav-link">Home</Link>
            <Link to="/about" className="nav-link">About Us</Link>

            {/* Svi ulogovani korisnici */}
            <Link to="/profile" className="nav-link">Profile</Link>
            <Link to="/appointments" className="nav-link">Appointments</Link>

            {/* Samo ADMIN i STAFF mogu videti fakture */}
            {(hasRole('ADMIN') ) && (
                <Link to="/invoices" className="nav-link">Invoices</Link>
            )}

            {/* Samo ADMIN može videti izvještaje */}
            {hasRole('ADMIN') && (
                <Link to="/reports" className="nav-link">Reports</Link>
            )}

            {/* DOCTOR i ADMIN mogu videti EMR */}
            {(hasRole('DOCTOR') ) && (
                <Link to="/emr" className="nav-link">EMR</Link>
            )}

            {/* DOCTOR može videti svoje termine */}
            {hasRole('DOCTOR') && (
                <Link to="/dr-appointments" className="nav-link">My Appointments</Link>
            )}

            {/* STAFF i ADMIN mogu videti sobe */}
            {(hasRole('DOCTOR') ) && (
                <Link to="/rooms" className="nav-link">Rooms</Link>
            )}

            <button onClick={() => {
                localStorage.removeItem('token');
                window.location.href = '/login';
            }} className="nav-link logout-button">
                Logout
            </button>
        </nav>
    );
};

export default RoleBasedNavigation;