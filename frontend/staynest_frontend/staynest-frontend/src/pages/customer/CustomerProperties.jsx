import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

function CustomerProperties() {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const API_BASE = "http://localhost:9090";

  useEffect(() => {
    const fetchProperties = async () => {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          setError("Please log in first.");
          navigate("/customer/login");
          return;
        }

        const res = await axios.get(`${API_BASE}/properties`);
        const propsWithImages = await Promise.all(
          res.data.map(async (p) => {
            try {
              const imgRes = await axios.get(
                `${API_BASE}/properties/${p.id}/propertyImages`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              return {
                ...p,
                imageUrls: imgRes.data.map(
                  (img) =>
                    `${API_BASE}/properties/${p.id}/propertyImages/${img.id}`
                ),
              };
            } catch {
              return { ...p, imageUrls: [] };
            }
          })
        );
        setProperties(propsWithImages);
      } catch (err) {
        setError("Failed to load properties");
      } finally {
        setLoading(false);
      }
    };
    fetchProperties();
  }, [navigate]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-red-500">{error}</p>;

  return (
    <div className="p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
      {properties.map((p) => (
        <div key={p.id} className="border rounded-lg shadow p-4 flex flex-col">
          {p.imageUrls.length > 0 ? (
            <img
              src={p.imageUrls[0]}
              alt={p.propertyName}
              className="w-full h-48 object-cover mb-2 rounded"
            />
          ) : (
            <img
              src="/assets/fallback-image.jpg"
              alt="No image available"
              className="w-full h-48 object-cover mb-2 rounded"
            />
          )}

          <h2 className="text-xl font-bold">{p.propertyName}</h2>
          <p>{p.description}</p>
          <p className="font-semibold">₹{p.price} / night</p>

          <Link
            to={`/customer/book/${p.id}`}
            className="mt-auto bg-staynest-pink text-white px-4 py-2 mt-4 inline-block rounded text-center hover:bg-staynest-hover"
          >
            Book Now
          </Link>
        </div>
      ))}
    </div>
  );
}

export default CustomerProperties;
