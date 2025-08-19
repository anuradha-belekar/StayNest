import { useState } from 'react';
  import { useNavigate } from 'react-router-dom';
  import axios from 'axios';

  function OwnerLogin() {
    const [credentials, setCredentials] = useState({ email: '', password: '' });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
      setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      setError('');
      setSuccess('');
      setLoading(true);
      try {
        const res = await axios.post('http://localhost:9090/users/signin', credentials);
        const { jwt } = res.data;

        // Save token in local storage or cookies
        localStorage.setItem('token', jwt);
        setSuccess('Login successful! Redirecting...');
        setTimeout(() => navigate('/owner/properties'), 2000); // redirect to property dashboard
      } catch (err) {
        setError(err.response?.data?.message || 'Invalid email or password');
      } finally {
        setLoading(false);
      }
    };

    return (
      <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Owner Login</h2>
        {error && <p className="text-red-500 mb-4">{error}</p>}
        {success && <p className="text-green-600 mb-4">{success}</p>}
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="email"
            name="email"
            value={credentials.email}
            onChange={handleChange}
            placeholder="Email"
            className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
            required
          />
          <input
            type="password"
            name="password"
            value={credentials.password}
            onChange={handleChange}
            placeholder="Password"
            className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-staynest-pink"
            required
          />
          <button
            type="submit"
            className="w-full bg-staynest-pink text-white p-2 rounded-md hover:bg-staynest-hover transition-colors disabled:opacity-50"
            disabled={loading || credentials.email === '' || credentials.password === ''}
          >
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>
      </div>
    );
  }

  export default OwnerLogin;

