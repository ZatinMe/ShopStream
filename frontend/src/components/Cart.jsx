import { useState } from 'react';
import { useCart } from '../contexts/CartContext';
import { orderService } from '../services/orderService';
import { authService } from '../services/authService';

const Cart = ({ isOpen, onClose }) => {
  const { items, removeFromCart, updateQuantity, clearCart, getTotalItems, getTotalPrice } = useCart();
  const [isCheckingOut, setIsCheckingOut] = useState(false);
  const [checkoutError, setCheckoutError] = useState('');

  const handleCheckout = async () => {
    const user = authService.getCurrentUser();
    if (!user) {
      setCheckoutError('Please login to place an order');
      return;
    }

    if (items.length === 0) {
      setCheckoutError('Your cart is empty');
      return;
    }

    setIsCheckingOut(true);
    setCheckoutError('');

    try {
      const orderData = {
        userId: user.id,
        orderItems: items.map(item => ({
          productId: item.productId,
          quantity: item.quantity,
          price: item.productPrice
        })),
        totalAmount: getTotalPrice(),
        status: 'PENDING'
      };

      await orderService.createOrder(orderData);
      clearCart();
      onClose();
      alert('Order placed successfully!');
    } catch (error) {
      setCheckoutError('Failed to place order. Please try again.');
    } finally {
      setIsCheckingOut(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-black bg-opacity-50" onClick={onClose}></div>
      
      <div className="absolute right-0 top-0 h-full w-full max-w-md bg-white shadow-xl">
        <div className="flex h-full flex-col">
          {/* Header */}
          <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
            <h2 className="text-lg font-semibold text-gray-900">
              Shopping Cart ({getTotalItems()})
            </h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors"
            >
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {/* Cart Items */}
          <div className="flex-1 overflow-y-auto px-6 py-4">
            {items.length === 0 ? (
              <div className="text-center py-12">
                <div className="text-6xl text-gray-300 mb-4">🛒</div>
                <p className="text-gray-500 text-lg">Your cart is empty</p>
                <p className="text-gray-400 text-sm mt-2">Add some products to get started</p>
              </div>
            ) : (
              <div className="space-y-4">
                {items.map((item, index) => (
                  <div key={`${item.productId}-${item.size}-${index}`} className="flex items-center space-x-4 border-b border-gray-100 pb-4">
                    {/* Product Image */}
                    <div className="h-16 w-16 bg-gray-100 rounded-lg flex items-center justify-center">
                      <span className="text-2xl">{item.productImage}</span>
                    </div>
                    
                    {/* Product Details */}
                    <div className="flex-1">
                      <h3 className="text-sm font-medium text-gray-900 line-clamp-2">
                        {item.productName}
                      </h3>
                      <p className="text-xs text-gray-500">Size: {item.size}</p>
                      <p className="text-sm font-semibold text-gray-900">₹{item.productPrice}</p>
                    </div>
                    
                    {/* Quantity Controls */}
                    <div className="flex items-center space-x-2">
                      <button
                        onClick={() => updateQuantity(item.productId, item.size, item.quantity - 1)}
                        className="w-8 h-8 border border-gray-300 rounded-full flex items-center justify-center hover:bg-gray-50 transition-colors"
                      >
                        <span className="text-gray-600">-</span>
                      </button>
                      <span className="w-8 text-center text-sm font-medium">{item.quantity}</span>
                      <button
                        onClick={() => updateQuantity(item.productId, item.size, item.quantity + 1)}
                        className="w-8 h-8 border border-gray-300 rounded-full flex items-center justify-center hover:bg-gray-50 transition-colors"
                      >
                        <span className="text-gray-600">+</span>
                      </button>
                    </div>
                    
                    {/* Remove Button */}
                    <button
                      onClick={() => removeFromCart(item.productId, item.size)}
                      className="text-red-500 hover:text-red-700 transition-colors"
                    >
                      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Footer */}
          {items.length > 0 && (
            <div className="border-t border-gray-200 px-6 py-4">
              {checkoutError && (
                <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                  {checkoutError}
                </div>
              )}
              
              <div className="flex justify-between items-center mb-4">
                <span className="text-lg font-semibold text-gray-900">Total:</span>
                <span className="text-xl font-bold text-gray-900">₹{getTotalPrice().toFixed(2)}</span>
              </div>
              
              <button
                onClick={handleCheckout}
                disabled={isCheckingOut}
                className="w-full bg-red-500 hover:bg-red-600 disabled:bg-gray-400 text-white py-3 px-4 rounded-lg font-semibold transition-colors"
              >
                {isCheckingOut ? (
                  <div className="flex items-center justify-center">
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                    Processing...
                  </div>
                ) : (
                  'Checkout'
                )}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Cart;

