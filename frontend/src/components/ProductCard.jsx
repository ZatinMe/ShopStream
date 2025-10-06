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
    <div className="myntra-product-card group cursor-pointer">
      {/* Product Image Container */}
      <div className="relative aspect-square bg-gray-50 overflow-hidden">
        {/* Product Image Placeholder */}
        <div className="absolute inset-0 flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-200">
          <div className="text-8xl text-gray-300">👕</div>
        </div>
        
        {/* Rating Badge */}
        <div className="absolute bottom-2 left-2">
          <div className="myntra-rating px-2 py-1 rounded-sm">
            <span className="flex items-center">
              <span>4.{Math.floor(Math.random() * 9) + 1}</span>
              <span className="ml-1">★</span>
              <span className="ml-1">|</span>
              <span className="ml-1">{Math.floor(Math.random() * 100) + 50}</span>
            </span>
          </div>
        </div>
        
        {/* Wishlist Button */}
        <button className="absolute top-2 right-2 w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:shadow-lg transition-all duration-200 opacity-0 group-hover:opacity-100">
          <span className="text-gray-600 text-lg">♡</span>
        </button>
        
        {/* Quick View Overlay */}
        <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-300 flex items-center justify-center">
          <button className="bg-white text-black px-4 py-2 rounded-full font-semibold text-sm opacity-0 group-hover:opacity-100 transform translate-y-4 group-hover:translate-y-0 transition-all duration-300">
            Quick View
          </button>
        </div>
      </div>
      
      {/* Product Details */}
      <div className="p-3">
        {/* Brand Name */}
        <div className="mb-1">
          <h3 className="text-sm font-bold text-gray-900">
            {product.name.split(' ')[0] || 'Brand'}
          </h3>
        </div>
        
        {/* Product Description */}
        <p className="text-xs text-gray-600 mb-2 line-clamp-2 leading-relaxed">
          {product.description}
        </p>
        
        {/* Price Section */}
        <div className="mb-2">
          <div className="flex items-center space-x-2">
            <span className="myntra-price-current text-sm">Rs. {product.price}</span>
            <span className="myntra-price-original text-xs">Rs. {Math.round(product.price * 1.2)}</span>
            <span className="myntra-discount text-xs">({Math.round((1.2 - 1) * 100)}% OFF)</span>
          </div>
        </div>
        
        {/* Size Options */}
        <div className="mb-3">
          <div className="flex space-x-1">
            {sizes.slice(0, 4).map((size) => (
              <button 
                key={size} 
                onClick={() => handleSizeSelect(size)}
                className={`w-6 h-6 border rounded text-xs font-medium transition-colors ${
                  selectedSize === size 
                    ? 'border-pink-500 bg-pink-50 text-pink-600' 
                    : 'border-gray-300 hover:border-gray-400 hover:bg-gray-50'
                }`}
              >
                {size}
              </button>
            ))}
          </div>
        </div>
        
        {/* Action Buttons */}
        <div className="space-y-1">
          <button 
            onClick={handleAddToCart}
            disabled={product.stock === 0 || isAddingToCart}
            data-product-id={product.id}
            className="w-full myntra-btn-primary text-xs py-2 disabled:bg-gray-400"
          >
            {isAddingToCart ? 'Adding...' : product.stock === 0 ? 'OUT OF STOCK' : 'ADD TO BAG'}
          </button>
        </div>
        
        {/* Seller Actions */}
        {isSeller && (
          <div className="mt-3 pt-2 border-t border-gray-100 flex space-x-2">
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
