import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

export default function PaymentPage() {
  const { bookingId } = useParams();
  const token = localStorage.getItem("token");

  const [booking, setBooking] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchBooking = async () => {
      try {
        console.log("Fetching booking with ID:", bookingId);
        const res = await axios.get(
          `http://localhost:9090/bookings/${bookingId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setBooking(res.data);
      } catch (err) {
        setError(err.response?.data?.message || "Failed to load booking details");
      } finally {
        setLoading(false);
      }
    };

    fetchBooking();
  }, [bookingId, token]);

  if (loading) {
    return <p className="text-center mt-10">Loading payment details...</p>;
  }

  if (error) {
    return <p className="text-center mt-10 text-red-500">{error}</p>;
  }

  if (!booking) {
    return <p className="text-center mt-10">No booking found.</p>;
  }

  return (
    <div className="p-6 max-w-lg mx-auto bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Payment Details</h2>
      <p><strong>Booking ID:</strong> {booking.bookingId}</p>
      <p><strong>Property:</strong> {booking.propertyName}</p>
      <p><strong>Total Price:</strong> ₹{booking.totalPrice}</p>
      <p><strong>Booking Status:</strong> {booking.bookingStatus}</p>
      <p>
        <strong>Payment Status:</strong>{" "}
        <span
          className={
            booking.paymentStatus === "SUCCESS"
              ? "text-green-600 font-semibold"
              : booking.paymentStatus === "PENDING"
              ? "text-yellow-600 font-semibold"
              : "text-red-600 font-semibold"
          }
        >
          {booking.paymentStatus}
        </span>
      </p>
    </div>
  );
}
