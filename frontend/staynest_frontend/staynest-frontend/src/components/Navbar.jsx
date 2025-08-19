import { Link } from 'react-router-dom';
import React from 'react';

export default function Navbar() {
  return (
    <nav className="bg-staynest-pink p-4 shadow-md">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold text-staynest-white">StayNest</Link>
        <div className="space-x-4 flex items-center">
          <div className="relative group">
            <button className="text-staynest-white hover:text-staynest-hover font-medium">Owner</button>
            <div className="absolute left-0 w-32 bg-staynest-white shadow-lg rounded-md mt-1 z-20 opacity-0 group-hover:opacity-100 group-hover:visible transition-opacity duration-200 pointer-events-none group-hover:pointer-events-auto">
              <Link to="/owner/signup" className="block px-4 py-2 text-gray-800 hover:bg-staynest-light hover:text-staynest-pink">Sign Up</Link>
              <Link to="/owner/login" className="block px-4 py-2 text-gray-800 hover:bg-staynest-light hover:text-staynest-pink">Login</Link>
            </div>
          </div>
          <div className="relative group">
            <button className="text-staynest-white hover:text-staynest-hover font-medium">Customer</button>
            <div className="absolute left-0 w-32 bg-staynest-white shadow-lg rounded-md mt-1 z-20 opacity-0 group-hover:opacity-100 group-hover:visible transition-opacity duration-200 pointer-events-none group-hover:pointer-events-auto">
              <Link to="/customer/signup" className="block px-4 py-2 text-gray-800 hover:bg-staynest-light hover:text-staynest-pink">Sign Up</Link>
              <Link to="/customer/login" className="block px-4 py-2 text-gray-800 hover:bg-staynest-light hover:text-staynest-pink">Login</Link>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}


