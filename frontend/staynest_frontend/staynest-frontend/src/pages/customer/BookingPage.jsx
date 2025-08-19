// src/pages/customer/BookingPage.jsx
import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";

export default function BookingPage() {
  const { propertyId } = useParams();
  const navigate = useNavigate();
  const stripe = useStripe();
  const elements = useElements();
  const token = localStorage.getItem("token");

  const [form, setForm] = useState({
    checkInDate: "",
    checkOutDate: "",
    numberOfGuests: 1
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setLoading(true);

    try {
      // 1️⃣ Create PaymentMethod from card details
      const { paymentMethod, error } = await stripe.createPaymentMethod({
        type: "card",
        card: elements.getElement(CardElement)
      });

      if (error) {
        setMessage(error.message);
        setLoading(false);
        return;
      }

      // 2️⃣ Send booking + paymentMethodId to backend
      const res = await axios.post(
        `http://localhost:9090/properties/${propertyId}/bookings`,
        {
          checkInDate: form.checkInDate,
          checkOutDate: form.checkOutDate,
          numberOfGuests: form.numberOfGuests,
          paymentMethodId: paymentMethod.id
        },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      console.log("Booking creation response:", res.data);

      // 3️⃣ Extract bookingId from the message string
      const match = res.data?.message?.match(/id (\d+)/);
      if (match && match[1]) {
        const bookingId = match[1];
        console.log("Extracted bookingId:", bookingId);
        navigate(`/customer/payment/${bookingId}`);
      } else {
        setMessage("Booking created but booking ID was not found in response");
      }
    } catch (err) {
      setMessage(err.response?.data?.message || "Booking or payment failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-lg mx-auto bg-white shadow rounded">
      <h2 className="text-2xl font-bold mb-4">Book Property</h2>
      {message && <p className="mb-4 text-red-500">{message}</p>}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label>Check-in Date</label>
          <input
            type="date"
            value={form.checkInDate}
            onChange={(e) => setForm({ ...form, checkInDate: e.target.value })}
            required
            className="border p-2 w-full"
          />
        </div>

        <div>
          <label>Check-out Date</label>
          <input
            type="date"
            value={form.checkOutDate}
            onChange={(e) => setForm({ ...form, checkOutDate: e.target.value })}
            required
            className="border p-2 w-full"
          />
        </div>

        <div>
          <label>Number of Guests</label>
          <input
            type="number"
            min="1"
            value={form.numberOfGuests}
            onChange={(e) =>
              setForm({ ...form, numberOfGuests: Number(e.target.value) })
            }
            required
            className="border p-2 w-full"
          />
        </div>

        <div>
          <label>Card Details</label>
          <CardElement className="p-2 border rounded" />
        </div>

        <button
          type="submit"
          disabled={!stripe || loading}
          className="bg-staynest-pink text-white px-4 py-2 rounded"
        >
          {loading ? "Processing..." : "Book & Pay"}
        </button>
      </form>
    </div>
  );
}
