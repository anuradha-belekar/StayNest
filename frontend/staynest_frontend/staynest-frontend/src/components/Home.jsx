import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <div className="max-w-4xl mx-auto mt-10 p-6 text-center bg-staynest-white rounded-lg shadow-lg">
      <h1 className="text-4xl font-bold text-gray-800 mb-4">Welcome to StayNest</h1>
      <p className="text-lg text-gray-600 mb-6">
        Discover unique accommodations or list your property with ease. Join our community of travelers and property owners today!
      </p>
      <div className="space-x-4">
        <Link
          to="/owner/signup"
          className="inline-block px-6 py-3 bg-staynest-pink text-staynest-white font-medium rounded-md hover:bg-staynest-hover transition-colors"
        >
          Become an Owner
        </Link>
        <Link
          to="/customer/signup"
          className="inline-block px-6 py-3 bg-staynest-white text-staynest-pink border border-staynest-pink font-medium rounded-md hover:bg-staynest-light hover:text-staynest-hover transition-colors"
        >
          Book as a Customer
        </Link>
      </div>
    </div>
  );
}

export default Home;


