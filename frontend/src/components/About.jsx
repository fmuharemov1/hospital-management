import React from 'react';
import './About.css'

function About() {
    return (
        <div className="about-page">
            <h1>About Us</h1>
            <img src="/medapp-logo-removebg-preview.png" alt="MedApp Clinics Logo" className="logo" />
            <p className="about-description">
                Welcome to <strong>MedApp Clinics</strong> — your trusted partner in comprehensive patient care and hospital management.
                Our mission is to deliver outstanding healthcare solutions that enhance the well-being of our patients and streamline hospital operations.
            </p>

            <footer className="about-footer">
                <h3>Contact Us</h3>
                <p>Email: contact@medappclinics.com</p>
                <p>Phone: +123 456 7890</p>
                <p>Address: 123 Healthcare St, MedCity</p>
            </footer>

        </div>
    );
}

export default About;
