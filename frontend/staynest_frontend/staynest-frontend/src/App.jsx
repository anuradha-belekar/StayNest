import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './components/Home';
import OwnerSignup from './pages/ownersignup';
import OwnerLogin from './pages/ownerlogin';
import PropertyForm from './pages/PropertyForm';
import PropertyList from './pages/PropertyList';
import CustomerLogin from './pages/customersignin';
import CustomerSignup from './pages/customersignup';
import CustomerProperties from './pages/customer/CustomerProperties';
import BookingPage from './pages/customer/BookingPage';
import PaymentPage from './pages/customer/PaymentPage';
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";

// Load Stripe with your publishable key
const stripePromise = loadStripe("pk_test_51RsTAa9bzPkCEmVuvwLirR4gcBdAK5ZN3I6sAsEzFK8j1za3U0oc6epYe0u1ciw5juWxTUMBIsfW1E0lDv0nQuqL0002hHN2gs");


function App() {
  return (
    <div className="min-h-screen bg-staynest-light">
      <Navbar />
      <Elements stripe={stripePromise}></Elements>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/owner/signup" element={<OwnerSignup />} />
        <Route path="/owner/login" element={<OwnerLogin />} />
        <Route path="/customer/signup" element={<CustomerSignup/>}></Route>
        <Route path="/customer/login" element={<CustomerLogin />} />
        <Route path="/owner/properties" element={<PropertyList />} />
        <Route path="/owner/add-property" element={<PropertyForm />} />
        <Route path="/owner/edit-property/:propertyId" element={<PropertyForm />} />
        <Route path="/customer/properties" element={<CustomerProperties />} />
        <Route path="/customer/book/:propertyId" element={<Elements stripe={stripePromise}><BookingPage /></Elements>} />
        <Route path="/customer/payment/:bookingId" element={<Elements stripe={stripePromise}><PaymentPage /></Elements>}/>        
      </Routes>
    </div>
  );
}

export default App;

