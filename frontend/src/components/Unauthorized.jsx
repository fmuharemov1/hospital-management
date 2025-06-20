import React from 'react';
import { Link } from 'react-router-dom';

const Unauthorized = () => {
    return (
        <div className="unauthorized-page">
            <div className="unauthorized-content">
                <h1>403 - Nemate dozvolu</h1>
                <p><strong> Nemate dozvolu za pristup ovoj stranici.</strong></p>
                <Link to="/profile" className="btn">Nazad na profil</Link>
            </div>
        </div>
    );
};

export default Unauthorized;