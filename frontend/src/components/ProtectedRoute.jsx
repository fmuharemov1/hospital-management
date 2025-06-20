import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children, allowedRoles = [], redirectTo = "/login" }) => {
    const token = localStorage.getItem('token');

    if (!token) {
        return <Navigate to={redirectTo} replace />;
    }

    // Dekodiranje JWT tokena da dobijete ulogu
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userRoles = payload.roles || [];

        // Proverite da li korisnik ima dozvoljenu ulogu
        const hasPermission = allowedRoles.length === 0 ||
            allowedRoles.some(role =>
                userRoles.includes(role) ||
                userRoles.includes(`ROLE_${role}`)
            );

        if (!hasPermission) {
            return <Navigate to="/unauthorized" replace />;
        }

        return children;
    } catch (error) {
        console.error('Error decoding token:', error);
        return <Navigate to={redirectTo} replace />;
    }
};

export default ProtectedRoute;