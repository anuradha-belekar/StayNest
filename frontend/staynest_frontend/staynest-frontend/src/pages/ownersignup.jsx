import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function OwnerSignup() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    role: 'ROLE_OWNER',
    phone: '',
    otp: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      if (!otpSent) {
        await axios.post('http://localhost:9090/users/signup', formData, {
          headers: { 'Content-Type': 'application/json' },
        });
        setOtpSent(true);
        setSuccess('OTP sent to your email. Please verify.');
      } else {
        await axios.post(
          'http://localhost:9090/users/verify-otp',
          {
            email: formData.email,
            otpValue: formData.otp,
          },
          { headers: { 'Content-Type': 'application/json' } }
        );
        setSuccess('Account created successfully!');
        setTimeout(() => navigate('/owner/login'), 2000);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-staynest-white rounded-lg shadow-lg">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Owner Sign Up</h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      {success && <p className="text-green-600 mb-4">{success}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          name="firstName"
          value={formData.firstName}
          onChange={handleChange}
          placeholder="First Name"
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
          required
        />
        <input
          type="text"
          name="lastName"
          value={formData.lastName}
          onChange={handleChange}
          placeholder="Last Name"
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
          required
        />
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="Email"
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
          required
        />
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          placeholder="Password"
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
          required
        />
        <input
          type="text"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
          placeholder="Phone Number"
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
          required
        />
        {otpSent && (
          <input
            type="text"
            name="otp"
            value={formData.otp}
            onChange={handleChange}
            placeholder="Enter OTP"
            className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
            required
          />
        )}
        <button
          type="submit"
          className="w-full bg-staynest-pink text-staynest-white p-2 rounded-md hover:bg-staynest-hover transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          disabled={
            formData.email === '' ||
            formData.password === '' ||
            formData.firstName === '' ||
            formData.lastName === '' ||
            formData.phone === '' ||
            (otpSent && formData.otp === '')
          }
        >
          {otpSent ? 'Verify OTP & Sign Up' : 'Send OTP'}
        </button>
      </form>
    </div>
  );
}

export default OwnerSignup;

