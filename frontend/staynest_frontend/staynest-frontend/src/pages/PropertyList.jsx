import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function PropertyList() {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const IMAGE_BASE_URL = 'http://localhost:9090';

  useEffect(() => {
    const fetchProperties = async () => {
      setLoading(true);
      setError('');
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          setError('No authentication token found. Please log in.');
          navigate('/owner/login');
          return;
        }

        const res = await axios.get('http://localhost:9090/properties', {
          headers: { Authorization: `Bearer ${token}` },
        });

        // Ensure res.data is an array
        const propertiesData = Array.isArray(res.data) ? res.data : [];
        console.log('Properties data:', propertiesData);

        // Fetch images for each property
        const propertiesWithImages = await Promise.all(
          propertiesData.map(async (property) => {
            try {
              const imageRes = await axios.get(`http://localhost:9090/properties/${property.id}/propertyImages`, {
                headers: { Authorization: `Bearer ${token}` },
              });
              console.log(`Images for property ${property.id}:`, imageRes.data);
              let images = [];
              if (Array.isArray(imageRes.data)) {
                images = await Promise.all(
                  imageRes.data
                    .filter((img) => img.fileName && img.success)
                    .map(async (img) => {
                      try {
                        const imageData = await axios.get(`${IMAGE_BASE_URL}${img.fileName}`, {
                          headers: { Authorization: `Bearer ${token}` },
                          responseType: 'blob',
                        });
                        return URL.createObjectURL(imageData.data);
                      } catch (err) {
                        console.error(`Failed to fetch image ${img.fileName}:`, err.message);
                        return null;
                      }
                    })
                );
              }
              return { ...property, images: images.filter(Boolean), imageError: images.length === 0 ? 'No valid images found' : null };
            } catch (err) {
              console.error(`Failed to fetch images for property ${property.id}:`, err.response?.data || err.message);
              return { ...property, images: [], imageError: err.response?.data?.message || 'Failed to fetch images' };
            }
          })
        );

        setProperties(propertiesWithImages);
      } catch (err) {
        console.error('Failed to fetch properties:', err.response?.data || err.message);
        if (err.response?.status === 401 || err.response?.status === 403) {
          localStorage.removeItem('token');
          setError('Session expired or unauthorized. Please log in again.');
          navigate('/owner/login');
        } else {
          setError(err.response?.data?.message || 'Failed to fetch properties');
        }
      } finally {
        setLoading(false);
      }
    };
    fetchProperties();
  }, [navigate]);

  const handleDelete = async (propertyId) => {
    if (window.confirm('Are you sure you want to delete this property?')) {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          navigate('/owner/login');
          return;
        }
        await axios.delete(`http://localhost:9090/properties/${propertyId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setProperties(properties.filter((property) => property.id !== propertyId));
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to delete property');
      }
    }
  };

  useEffect(() => {
    // Cleanup blob URLs to prevent memory leaks
    return () => {
      properties.forEach((property) => {
        property.images?.forEach((image) => {
          if (image) URL.revokeObjectURL(image);
        });
      });
    };
  }, [properties]);

  return (
    <div className="max-w-4xl mx-auto mt-10 p-6 bg-staynest-white rounded-lg shadow-lg">
      <h2 className="text-2xl font-bold mb-6 text-center text-staynest-pink">Your Properties</h2>
      <button
        onClick={() => navigate('/owner/add-property')}
        className="mb-4 bg-staynest-pink text-white p-2 rounded-md hover:bg-staynest-hover"
      >
        Add New Property
      </button>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      {loading ? (
        <p>Loading...</p>
      ) : properties.length === 0 ? (
        <p>No properties found.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {properties.map((property) => (
            <div key={property.id} className="border p-4 rounded-md">
              <h3 className="text-lg font-semibold">{property.propertyName}</h3>
              <p>{property.description}</p>
              <p>Price: ${property.price}/night</p>
              <p>Type: {property.propertyType}</p>
              <p>
                Address: {property.propertyAddress.adrLine1}, {property.propertyAddress.city},{' '}
                {property.propertyAddress.state}, {property.propertyAddress.country}
              </p>
              {property.imageError && (
                <p className="text-red-500 text-sm mt-2">{property.imageError}</p>
              )}
              {property.images && property.images.length > 0 ? (
                <div className="mt-4">
                  <img
                    src={property.images[0]}
                    alt={property.propertyName}
                    className="w-full h-48 object-cover rounded-md"
                    onError={(e) => {
                      console.error(`Failed to load image for property ${property.id}: ${property.images[0]}`);
                      e.target.src = '/assets/fallback-image.jpg';
                    }}
                  />
                </div>
              ) : (
                <div className="mt-4">
                  <p className="text-gray-500 text-sm">No image available</p>
                  <img
                    src="/assets/fallback-image.jpg"
                    alt="No image available"
                    className="w-full h-48 object-cover rounded-md"
                  />
                </div>
              )}
              <div className="mt-2 space-x-2">
                <button
                  onClick={() => navigate(`/owner/edit-property/${property.id}`)}
                  className="bg-staynest-pink text-white p-2 rounded-md hover:bg-staynest-hover"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(property.id)}
                  className="bg-red-500 text-white p-2 rounded-md hover:bg-red-600"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default PropertyList;

