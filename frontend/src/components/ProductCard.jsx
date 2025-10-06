import { useState } from 'react';
import { useCart } from '../contexts/CartContext';
import { authService } from '../services/authService';

const ProductCard = ({ product, onEdit, onDelete, isSeller }) => {
  const { addToCart } = useCart();
  const [showSizeSelector, setShowSizeSelector] = useState(false);
  const [selectedSize, setSelectedSizeLocal] = useState(null);
  const [isAddingToCart, setIsAddingToCart] = useState(false);

  const sizes = ['S', 'M', 'L', 'XL'];
  const user = authService.getCurrentUser();

  const handleAddToCart = async () => {
    if (!user) {
      alert('Please login to add items to cart');
      return;
    }

    if (!selectedSize) {
      setShowSizeSelector(true);
      return;
    }

    setIsAddingToCart(true);
    try {
      addToCart(product, 1);
      setShowSizeSelector(false);
      setSelectedSizeLocal(null);
      // Show success message
      const button = document.querySelector(`[data-product-id="${product.id}"]`);
      if (button) {
        const originalText = button.textContent;
        button.textContent = 'Added!';
        button.classList.add('bg-green-500');
        setTimeout(() => {
          button.textContent = originalText;
          button.classList.remove('bg-green-500');
        }, 2000);
      }
    } catch (error) {
      console.error('Error adding to cart:', error);
    } finally {
      setIsAddingToCart(false);
    }
  };

  const handleSizeSelect = (size) => {
    setSelectedSizeLocal(size);
    setShowSizeSelector(false);
  };

  return (
    <div className="group bg-white hover:shadow-xl transition-all duration-300 cursor-pointer border border-gray-100 rounded-xl overflow-hidden">
      {/* Product Image Container */}
      <div className="relative aspect-square bg-gray-50 overflow-hidden">
        {/* Product Image Placeholder */}
        <div className="absolute inset-0 flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-200">
          <div className="text-8xl text-gray-300">📱</div>
        </div>
        
        {/* Wishlist Button */}
        <button className="absolute top-3 right-3 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:shadow-lg transition-all duration-200 opacity-0 group-hover:opacity-100">
          <span className="text-gray-600 text-lg">♡</span>
        </button>
        
        {/* Category Badge */}
        <div className="absolute top-3 left-3">
          <span className="bg-red-500 text-white px-2 py-1 rounded text-xs font-semibold">
            {product.category}
          </span>
        </div>
        
        {/* Stock Indicator */}
        <div className="absolute bottom-3 left-3">
          <span className={`px-2 py-1 rounded text-xs font-medium ${
            product.stock > 10 ? 'bg-green-500 text-white' : 
            product.stock > 0 ? 'bg-yellow-500 text-white' : 'bg-red-500 text-white'
          }`}>
            {product.stock > 0 ? `${product.stock} left` : 'Out of stock'}
          </span>
        </div>
        
        {/* Quick View Overlay */}
        <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-300 flex items-center justify-center">
          <button className="bg-white text-black px-4 py-2 rounded-full font-semibold text-sm opacity-0 group-hover:opacity-100 transform translate-y-4 group-hover:translate-y-0 transition-all duration-300">
            Quick View
          </button>
        </div>
      </div>
      
      {/* Product Details */}
      <div className="p-4">
        {/* Product Name */}
        <div className="mb-2">
          <h3 className="text-sm font-semibold text-gray-900 line-clamp-2 leading-tight">
            {product.name}
          </h3>
        </div>
        
        {/* Description */}
        <p className="text-xs text-gray-600 mb-3 line-clamp-2 leading-relaxed">
          {product.description}
        </p>
        
        {/* Price Section */}
        <div className="mb-3">
          <div className="flex items-center space-x-2">
            <span className="text-lg font-bold text-gray-900">₹{product.price}</span>
            <span className="text-sm text-gray-500 line-through">₹{Math.round(product.price * 1.2)}</span>
            <span className="text-xs text-green-600 font-semibold">({Math.round((1.2 - 1) * 100)}% OFF)</span>
          </div>
          <div className="text-xs text-gray-500 mt-1">
            MRP: ₹{Math.round(product.price * 1.2)} (incl. of all taxes)
          </div>
        </div>
        
        {/* Rating */}
        <div className="flex items-center space-x-2 mb-3">
          <div className="flex items-center">
            {[...Array(5)].map((_, i) => (
              <span key={i} className="text-yellow-400 text-xs">★</span>
            ))}
          </div>
          <span className="text-xs text-gray-500">(4.{Math.floor(Math.random() * 9) + 1})</span>
          <span className="text-xs text-gray-500">({Math.floor(Math.random() * 100) + 50} reviews)</span>
        </div>
        
        {/* Size Options */}
        <div className="mb-4">
          <div className="text-xs text-gray-600 mb-2">Size:</div>
          <div className="flex space-x-1">
            {sizes.map((size) => (
              <button 
                key={size} 
                onClick={() => handleSizeSelect(size)}
                className={`w-8 h-8 border rounded text-xs font-medium transition-colors ${
                  selectedSize === size 
                    ? 'border-red-500 bg-red-50 text-red-600' 
                    : 'border-gray-300 hover:border-gray-400 hover:bg-gray-50'
                }`}
              >
                {size}
              </button>
            ))}
          </div>
        </div>
        
        {/* Action Buttons */}
        <div className="space-y-2">
          <button 
            onClick={handleAddToCart}
            disabled={product.stock === 0 || isAddingToCart}
            data-product-id={product.id}
            className="w-full bg-red-500 hover:bg-red-600 disabled:bg-gray-400 text-white py-2 px-4 rounded font-semibold text-sm transition-colors"
          >
            {isAddingToCart ? 'Adding...' : product.stock === 0 ? 'OUT OF STOCK' : 'ADD TO BAG'}
          </button>
          <button className="w-full border border-gray-300 hover:border-gray-400 text-gray-700 py-2 px-4 rounded font-semibold text-sm transition-colors">
            WISHLIST
          </button>
        </div>
        
        {/* Seller Actions */}
        {isSeller && (
          <div className="mt-4 pt-3 border-t border-gray-100 flex space-x-2">
            <button
              onClick={() => onEdit(product)}
              className="flex-1 text-gray-600 hover:text-black py-1 px-2 rounded text-xs font-medium hover:bg-gray-50 transition-colors"
            >
              Edit
            </button>
            <button
              onClick={() => onDelete(product.id)}
              className="flex-1 text-red-600 hover:text-red-800 py-1 px-2 rounded text-xs font-medium hover:bg-red-50 transition-colors"
            >
              Delete
            </button>
          </div>
        )}
      </div>

      {/* Size Selector Modal */}
      {showSizeSelector && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white rounded-lg p-6 max-w-sm w-full mx-4">
            <h3 className="text-lg font-semibold mb-4">Select Size</h3>
            <div className="grid grid-cols-4 gap-2 mb-4">
              {sizes.map((size) => (
                <button
                  key={size}
                  onClick={() => handleSizeSelect(size)}
                  className="w-12 h-12 border border-gray-300 rounded text-sm font-medium hover:border-red-500 hover:bg-red-50 transition-colors"
                >
                  {size}
                </button>
              ))}
            </div>
            <div className="flex space-x-2">
              <button
                onClick={() => setShowSizeSelector(false)}
                className="flex-1 border border-gray-300 text-gray-700 py-2 px-4 rounded font-medium transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={() => setShowSizeSelector(false)}
                className="flex-1 bg-red-500 text-white py-2 px-4 rounded font-medium transition-colors"
              >
                Continue
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductCard;
