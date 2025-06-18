import React, { useState, useEffect } from 'react';
import './Profile.css';
import { Link, useNavigate } from 'react-router-dom';

export default function Profile() {
    // Initial states for user data, loading status, and errors
    const [user, setUser] = useState({
        username: 'Loading...',
        fullName: 'Loading...',
        email: 'Loading...',
        phone: 'Loading...',
        roles: []
    });
    const [isLoading, setIsLoading] = useState(true); // State for displaying loading indicator
    const [error, setError] = useState(null); // State for errors
    const navigate = useNavigate();

    // Function to retrieve JWT token from local storage
    const getAuthToken = () => {
        return localStorage.getItem('token');
    };

    // useEffect hook that runs when the component mounts
    useEffect(() => {
        const fetchUserProfile = async () => {
            setIsLoading(true); // Set loading state to true
            setError(null); // Reset previous errors

            const token = getAuthToken(); // Retrieve token
            if (!token) {
                // If no token, set error and redirect to login
                setError("Authentication token not found. Please log in.");
                navigate('/login');
                setIsLoading(false);
                return;
            }

            try {
                // Call backend endpoint to fetch user profile
                // Assuming client-service has an endpoint like /api/client/users/me
                // that returns data of the logged-in user based on the JWT.
                const response = await fetch('http://localhost:8085/api/client/users/me', {
                    method: 'GET', // Method should be GET
                    headers: {
                        'Authorization': `Bearer ${token}`, // Add JWT token to Authorization header
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    // If response is not OK (e.g., 401, 403, 500), read the error message
                    const errorData = await response.text();
                    throw new Error(`HTTP error! Status: ${response.status} - ${errorData}`);
                }

                // If the response is successful, parse the JSON data
                const data = await response.json();
                setUser({
                    username: data.username || 'N/A',
                    fullName: data.fullName || 'N/A',
                    email: data.email || 'N/A',
                    phone: data.phone || 'N/A',
                    roles: data.roles || [] // Retrieve roles if the backend returns them
                });

            } catch (err) {
                // Catch errors during data fetching
                console.error("Error fetching user profile:", err);
                setError(`Error loading profile: ${err.message}. Please try again.`);
            } finally {
                setIsLoading(false); // Regardless of success/failure, end loading
            }
        };

        fetchUserProfile(); // Call the function to fetch profile
    }, [navigate]); // navigate is added as a dependency

    // Function to log out the user
    const handleLogout = () => {
        localStorage.removeItem('token'); // Remove token
        navigate('/login'); // Redirect to login page
    };

    // Display loading state
    if (isLoading) {
        return <div className="profile-page">Loading profile...</div>;
    }

    // Display error
    if (error) {
        return (
            <div className="profile-page error-message">
                <p>{error}</p>
                <Link to="/login">Go to Login</Link>
            </div>
        );
    }

    return (
        <div className="profile-page">
            <nav className="navbar">
                <Link to="/profile" className="nav-link active">Profile</Link>
                <Link to="/appointments" className="nav-link">Appointments</Link>
                <Link to="/patient-emr" className="nav-link">EMR</Link>
                <button onClick={handleLogout} className="nav-link logout-button">Log out</button>
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
                    Roles
                    <input type="text" value={user.roles.join(', ')} readOnly />
                </label>
            </form>
        </div>
    );
}
