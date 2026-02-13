import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { CartProvider } from './contexts/CartContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Products from './pages/Products';
import Orders from './pages/Orders';
import { authService } from './services/authService';

function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    setUser(currentUser);
    setLoading(false);
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-xl">Loading...</div>
      </div>
    );
  }

  return (
    <CartProvider>
      <Router>
        <div className="min-h-screen bg-white">
          <Navbar user={user} setUser={setUser} />
          <Routes>
            <Route path="/login" element={!user ? <Login setUser={setUser} /> : <Navigate to="/" />} />
            <Route path="/register" element={!user ? <Register setUser={setUser} /> : <Navigate to="/" />} />
            <Route path="/" element={<Products />} />
            <Route path="/orders" element={user ? <Orders /> : <Navigate to="/login" />} />
          </Routes>
        </div>
      </Router>
    </CartProvider>
  );
}

export default App;
