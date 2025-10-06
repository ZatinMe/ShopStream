import { Link } from 'react-router-dom';
import { useState } from 'react';
import { authService } from '../services/authService';
import { useCart } from '../contexts/CartContext';
import Cart from './Cart';

const Navbar = ({ user, setUser }) => {
  const { getTotalItems } = useCart();
  const [isCartOpen, setIsCartOpen] = useState(false);

  const handleLogout = () => {
    authService.logout();
    setUser(null);
  };

  return (
    <nav className="myntra-header">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center space-x-3">
              <div className="myntra-logo h-8 w-8 flex items-center justify-center">
                <span className="text-white font-bold text-lg">M</span>
              </div>
              <span className="text-2xl font-bold text-white">ShopStream</span>
            </Link>
          </div>
          
          {/* Navigation Links */}
          <div className="hidden md:flex items-center space-x-8">
            <Link to="/" className="myntra-nav-link">MEN</Link>
            <Link to="/" className="myntra-nav-link">WOMEN</Link>
            <Link to="/" className="myntra-nav-link">KIDS</Link>
            <Link to="/" className="myntra-nav-link">HOME</Link>
            <Link to="/" className="myntra-nav-link">BEAUTY</Link>
            <Link to="/" className="myntra-nav-link">GENZ</Link>
            <Link to="/" className="myntra-nav-link">
              STUDIO <span className="text-red-400 text-xs">NEW</span>
            </Link>
          </div>

          {/* Search Bar */}
          <div className="flex-1 max-w-lg mx-8">
            <div className="relative">
              <input
                type="text"
                placeholder="Search for products, brands and more"
                className="myntra-search w-full px-4 py-2 pl-10 pr-4 rounded-sm focus:outline-none focus:ring-2 focus:ring-pink-500"
              />
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <svg className="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
            </div>
          </div>
          
          {/* User Actions */}
          <div className="flex items-center space-x-6">
            {user ? (
              <>
                {/* Cart Button */}
                <button
                  onClick={() => setIsCartOpen(true)}
                  className="relative text-white hover:text-pink-300 px-3 py-2 text-sm font-medium transition-colors flex flex-col items-center"
                >
                  <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01" />
                  </svg>
                  <span className="text-xs mt-1">Cart</span>
                  {getTotalItems() > 0 && (
                    <span className="absolute -top-1 -right-1 bg-pink-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                      {getTotalItems()}
                    </span>
                  )}
                </button>

                {/* Wishlist Button */}
                <button className="text-white hover:text-pink-300 px-3 py-2 text-sm font-medium transition-colors flex flex-col items-center">
                  <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                  <span className="text-xs mt-1">Wishlist</span>
                </button>
                
                {/* Profile */}
                <div className="flex items-center space-x-2">
                  <div className="h-8 w-8 bg-gray-200 rounded-full flex items-center justify-center">
                    <span className="text-gray-700 text-sm font-semibold">
                      {user.username.charAt(0).toUpperCase()}
                    </span>
                  </div>
                  <span className="text-white text-sm">Hi, {user.username}</span>
                </div>
                
                <button
                  onClick={handleLogout}
                  className="text-white hover:text-pink-300 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Logout
                </button>
              </>
            ) : (
              <div className="flex items-center space-x-4">
                <Link
                  to="/login"
                  className="text-white hover:text-pink-300 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="myntra-btn-primary"
                >
                  Sign Up
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
      
      {/* Cart Component */}
      <Cart isOpen={isCartOpen} onClose={() => setIsCartOpen(false)} />
    </nav>
  );
};

export default Navbar;
