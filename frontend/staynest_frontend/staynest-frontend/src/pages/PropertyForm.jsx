
import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

function PropertyForm() {
  const { propertyId } = useParams();
  const isEditing = !!propertyId;
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    propertyName: '',
    description: '',
    price: '',
    propertyType: 'APARTMENT',
    propertyAddress: {
      adrLine1: '',
      adrLine2: '',
      city: '',
      state: '',
      country: '',
      zipCode: '',
      latitude: '',
      longitude: '',
    },
    amenityIds: [],
    images: [],
    availablity: false,
    status: false,
  });

  const [amenities, setAmenities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // ✅ Auto-detect amenities fetcher
  const fetchAmenities = async () => {
    try {
      const token = localStorage.getItem("token");
      const headers = { Authorization: `Bearer ${token}` };

      // 1️⃣ Try direct /amenities endpoint
      try {
        const res = await axios.get("http://localhost:9090/amenities", { headers });
        if (Array.isArray(res.data)) {
          setAmenities(res.data);
          return;
        }
      } catch (err) {
        console.warn("Direct /amenities failed:", err.response?.data || err.message);
      }

      // 2️⃣ Fallback: Try /properties/owner
      try {
        const resOwner = await axios.get("http://localhost:9090/properties/owner", { headers });
        if (Array.isArray(resOwner.data) && resOwner.data.length > 0) {
          const allAmenities = new Map();
          resOwner.data.forEach((prop) => {
            (prop.amenities || []).forEach((a) => {
              allAmenities.set(a.id, a);
            });
          });
          setAmenities(Array.from(allAmenities.values()));
          return;
        }
      } catch (err) {
        console.warn("Fallback /properties/owner failed:", err.response?.data || err.message);
      }

      // 3️⃣ Final fallback: Try /properties and get amenities from first property
      try {
        const resProps = await axios.get("http://localhost:9090/properties", { headers });
        if (Array.isArray(resProps.data) && resProps.data.length > 0) {
          const firstPropId = resProps.data[0].id;
          const resDetails = await axios.get(`http://localhost:9090/properties/${firstPropId}`, { headers });
          setAmenities(resDetails.data.amenities || []);
          return;
        }
      } catch (err) {
        console.warn("Fallback /properties failed:", err.response?.data || err.message);
      }

      setError("No amenities could be fetched for this owner.");
    } catch (err) {
      console.error("Unexpected fetchAmenities error:", err);
      setError("Failed to fetch amenities.");
    }
  };

  const fetchProperty = async () => {
    if (isEditing) {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(`http://localhost:9090/properties/${propertyId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        const rawStatus = res.data.status;
        const rawAvail = res.data.availablity;

        setFormData({
          ...res.data,
          status: rawStatus === true || rawStatus === 1 || rawStatus === '0x01',
          availablity: rawAvail === true || rawAvail === 1 || rawAvail === '0x01',
          propertyAddress: {
            adrLine1: res.data.propertyAddress?.adrLine1 || '',
            adrLine2: res.data.propertyAddress?.adrLine2 || '',
            city: res.data.propertyAddress?.city || '',
            state: res.data.propertyAddress?.state || '',
            country: res.data.propertyAddress?.country || '',
            zipCode: res.data.propertyAddress?.zipCode || '',
            latitude: res.data.propertyAddress?.latitude || '',
            longitude: res.data.propertyAddress?.longitude || '',
          },
          amenityIds: res.data.amenityIds || [],
          images: [],
        });
      } catch {
        setError("Failed to fetch property details");
      }
    }
  };

  useEffect(() => {
    fetchAmenities();
    fetchProperty();
  }, [isEditing, propertyId]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (name.includes("propertyAddress.")) {
      const addressField = name.split(".")[1];
      setFormData((prev) => ({
        ...prev,
        propertyAddress: { ...prev.propertyAddress, [addressField]: value },
      }));
    } else if (type === "checkbox" && name.startsWith("amenity_")) {
      // Handle amenity checkbox clicks
      const amenityId = Number(name.split("_")[1]);
      setFormData((prev) => {
        const updatedAmenities = checked
          ? [...prev.amenityIds, amenityId]
          : prev.amenityIds.filter((id) => id !== amenityId);
        return { ...prev, amenityIds: updatedAmenities };
      });
    } else if (type === "checkbox") {
      setFormData((prev) => ({ ...prev, [name]: checked }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleFileChange = (e) => {
    setFormData({ ...formData, images: Array.from(e.target.files) });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    const payload = {
      propertyName: formData.propertyName,
      description: formData.description,
      price: Number(formData.price),
      propertyType: formData.propertyType,
      propertyAddress: formData.propertyAddress,
      amenityIds: formData.amenityIds,
      availablity: !!formData.availablity,
      status: !!formData.status,
    };

    try {
      const token = localStorage.getItem('token');
      const config = {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      };

      let propertyResponse;
      if (isEditing) {
        propertyResponse = await axios.put(
          `http://localhost:9090/properties/${propertyId}`,
          payload,
          config
        );
        setSuccess('Property updated successfully!');
      } else {
        propertyResponse = await axios.post(
          'http://localhost:9090/properties',
          payload,
          config
        );
        setSuccess('Property added successfully!');
      }

      const newPropertyId = isEditing
        ? propertyId
        : propertyResponse.data.message.match(/\d+/)[0];

      if (formData.images.length > 0) {
        const imageFormData = new FormData();
        formData.images.forEach((file) => {
          imageFormData.append('file', file);
        });

        await axios.post(
          `http://localhost:9090/properties/${newPropertyId}/propertyImages`,
          imageFormData,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setSuccess((prev) => `${prev} Images uploaded successfully!`);
      }

      setTimeout(() => navigate('/owner/properties'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit property');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto mt-10 p-6 bg-staynest-white rounded-lg shadow-lg">
      <h2 className="text-2xl font-bold mb-6 text-center text-staynest-pink">
        {isEditing ? 'Edit Property' : 'Add New Property'}
      </h2>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      {success && <p className="text-green-600 mb-4">{success}</p>}

      <form onSubmit={handleSubmit} className="space-y-4">
        <input type="text" name="propertyName" value={formData.propertyName} onChange={handleChange} placeholder="Property Name" required className="w-full p-2 border border-gray-300 rounded-md" />
        <textarea name="description" value={formData.description} onChange={handleChange} placeholder="Description" required className="w-full p-2 border border-gray-300 rounded-md" />
        <input type="number" name="price" value={formData.price} onChange={handleChange} placeholder="Price per Night" required className="w-full p-2 border border-gray-300 rounded-md" />

        <select name="propertyType" value={formData.propertyType} onChange={handleChange} className="w-full p-2 border border-gray-300 rounded-md">
          <option value="APARTMENT">Apartment</option>
          <option value="HOSTEL">Hostel</option>
          <option value="HOTEL">Hotel</option>
          <option value="RESORT">Resort</option>
          <option value="TENT">Tent</option>
          <option value="VILLA">Villa</option>
        </select>

        {Object.entries(formData.propertyAddress).map(([key, val]) => (
          <input
            key={key}
            type={key === 'latitude' || key === 'longitude' ? 'number' : 'text'}
            name={`propertyAddress.${key}`}
            value={val}
            onChange={handleChange}
            placeholder={key.charAt(0).toUpperCase() + key.slice(1)}
            className="w-full p-2 border border-gray-300 rounded-md"
          />
        ))}

        {/* Amenities as checkboxes */}
        <div>
          <label className="font-semibold">Select Amenities:</label>
          <div className="grid grid-cols-2 gap-2 mt-2">
            {amenities.map((amenity) => (
              <label key={amenity.id} className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  name={`amenity_${amenity.id}`}
                  checked={formData.amenityIds.includes(amenity.id)}
                  onChange={handleChange}
                />
                <span>{amenity.amenityName}</span>
              </label>
            ))}
          </div>
        </div>

        <div className="flex items-center">
          <input type="checkbox" name="availablity" checked={formData.availablity} onChange={handleChange} className="mr-2" />
          <label>Property Available for Booking</label>
        </div>

        <div className="flex items-center">
          <input type="checkbox" name="status" checked={formData.status} onChange={handleChange} className="mr-2" />
          <label>Property Listing Active</label>
        </div>

        <input type="file" multiple onChange={handleFileChange} accept="image/jpeg,image/png" className="w-full p-2 border border-gray-300 rounded-md" />

        <button type="submit" disabled={loading} className="w-full bg-staynest-pink text-white p-2 rounded-md hover:bg-staynest-hover transition-colors disabled:opacity-50">
          {loading ? 'Submitting...' : isEditing ? 'Update Property' : 'Add Property'}
        </button>
      </form>
    </div>
  );
}

export default PropertyForm;
