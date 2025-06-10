import React from 'react';
import './Profile.css';
import { Link } from 'react-router-dom';

export default function Profile() {
    const user = {
        username: 'johndoe',
        fullName: 'John Doe',
        email: 'johndoe@example.com',
        phone: '+387 61 234 567',
    };

    return (
        <div className="profile-page">
            <nav className="navbar">
                <Link to="/profile" className="nav-link">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
            </nav>
            <h1>My Account</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <form className="profile-form">
                <label>
                    Username
                    <input type="text" value={user.username} readOnly />
                </label>
                <label>
                    Full Name
                    <input type="text" value={user.fullName} readOnly />
                </label>
                <label>
                    Email
                    <input type="email" value={user.email} readOnly />
                </label>
                <label>
                    Phone
                    <input type="text" value={user.phone} readOnly />
                </label>
            </form>
        </div>
    );
}
