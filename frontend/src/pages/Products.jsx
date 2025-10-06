import { useState, useEffect } from 'react';
import { productService } from '../services/productService';
import { authService } from '../services/authService';
import ProductCard from '../components/ProductCard';
import AdminPanel from '../components/AdminPanel';

const Products = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAdminPanel, setShowAdminPanel] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [sortBy, setSortBy] = useState('name');

  const user = authService.getCurrentUser();
  const isSeller = user?.roles?.includes('ROLE_SELLER') || user?.roles?.includes('ROLE_ADMIN');

  useEffect(() => {
    loadProducts();
  }, []);

  useEffect(() => {
    let filtered = products.filter(product => {
      const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           product.category.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           product.description.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesCategory = !selectedCategory || product.category === selectedCategory;
      return matchesSearch && matchesCategory;
    });

    // Sort products
    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'price-low':
          return a.price - b.price;
        case 'price-high':
          return b.price - a.price;
        case 'name':
          return a.name.localeCompare(b.name);
        case 'newest':
          return new Date(b.createdAt || 0) - new Date(a.createdAt || 0);
        default:
          return 0;
      }
    });

    setFilteredProducts(filtered);
  }, [products, searchTerm, selectedCategory, sortBy]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      console.log('Loading products...');
      const data = await productService.getAllProducts();
      console.log('Products loaded:', data);
      setProducts(data);
    } catch (err) {
      console.error('Error loading products:', err);
      setError('Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const handleAddProduct = async (productData) => {
    try {
      await productService.createProduct({
        ...productData,
        sellerId: user.id
      });
      setShowAdminPanel(false);
      loadProducts();
    } catch (err) {
      setError('Failed to create product');
    }
  };

  const handleEditProduct = async (productId, productData) => {
    try {
      await productService.updateProduct(productId, productData);
      setShowAdminPanel(false);
      setEditingProduct(null);
      loadProducts();
    } catch (err) {
      setError('Failed to update product');
    }
  };

  const handleEdit = (product) => {
    setEditingProduct(product);
    setShowAdminPanel(true);
  };

  const handleDelete = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await productService.deleteProduct(productId);
        loadProducts();
      } catch (err) {
        setError('Failed to delete product');
      }
    }
  };

  const handleCancel = () => {
    setShowAdminPanel(false);
    setEditingProduct(null);
  };

  const getCategories = () => {
    const categories = [...new Set(products.map(p => p.category))];
    return categories;
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-white">
        <div className="text-xl text-gray-600">Loading products...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-white">
      {/* Breadcrumbs */}
      <div className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-2">
          <div className="text-sm text-gray-500">
            <span>Home</span> / <span>Clothing</span> / <span className="font-semibold text-gray-900">Men T-Shirts</span>
          </div>
        </div>
      </div>

      {/* Page Title and Filters Bar */}
      <div className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="myntra-heading text-2xl">Men T-Shirts</h1>
              <p className="myntra-text-muted">{filteredProducts.length} items</p>
            </div>
            
            <div className="flex items-center space-x-4">
              <div className="flex items-center space-x-2">
                <span className="text-sm font-semibold text-gray-700">FILTERS</span>
                <div className="flex space-x-4">
                  <select className="text-sm border-none bg-transparent focus:outline-none">
                    <option>Bundles</option>
                  </select>
                  <select className="text-sm border-none bg-transparent focus:outline-none">
                    <option>Country of Origin</option>
                  </select>
                  <select className="text-sm border-none bg-transparent focus:outline-none">
                    <option>Size</option>
                  </select>
                </div>
              </div>
              <div className="flex items-center space-x-2">
                <span className="text-sm text-gray-600">Sort by:</span>
                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value)}
                  className="text-sm border-none bg-transparent focus:outline-none font-semibold"
                >
                  <option value="name">Recommended</option>
                  <option value="price-low">Price: Low to High</option>
                  <option value="price-high">Price: High to Low</option>
                  <option value="newest">Newest First</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="flex gap-6">
          {/* Filters Sidebar */}
          <div className="myntra-filter-sidebar w-64 myntra-mobile-hidden">
            {/* Categories */}
            <div className="myntra-filter-section">
              <h3 className="myntra-filter-title mb-3">CATEGORIES</h3>
              <div className="space-y-2">
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>Tshirts ({products.filter(p => p.category === 'Tshirts').length})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>Polo Tshirts ({products.filter(p => p.category === 'Polo').length})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>Oversized Tshirts ({products.filter(p => p.category === 'Oversized').length})</span>
                </label>
              </div>
            </div>

            {/* Brand */}
            <div className="myntra-filter-section">
              <div className="flex items-center justify-between mb-3">
                <h3 className="myntra-filter-title">BRAND</h3>
                <svg className="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
              <div className="space-y-2">
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>WOOSTRO ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>Genzy ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <span>Roadster ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <button className="text-blue-600 text-sm font-medium">+ 1368 more</button>
              </div>
            </div>

            {/* Price */}
            <div className="myntra-filter-section">
              <h3 className="myntra-filter-title mb-3">PRICE</h3>
              <div className="space-y-3">
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-gray-600">₹0</span>
                  <input
                    type="range"
                    min="0"
                    max="10000"
                    className="myntra-price-slider flex-1"
                  />
                  <span className="text-sm text-gray-600">₹10,000+</span>
                </div>
                <div className="flex space-x-2">
                  <input
                    type="number"
                    placeholder="Min"
                    className="w-20 px-2 py-1 text-sm border border-gray-300 rounded"
                  />
                  <input
                    type="number"
                    placeholder="Max"
                    className="w-20 px-2 py-1 text-sm border border-gray-300 rounded"
                  />
                </div>
              </div>
            </div>

            {/* Color */}
            <div className="myntra-filter-section">
              <div className="flex items-center justify-between mb-3">
                <h3 className="myntra-filter-title">COLOR</h3>
                <svg className="h-4 w-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
              <div className="space-y-2">
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <div className="myntra-color-swatch bg-black"></div>
                  <span>Black ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <div className="myntra-color-swatch bg-blue-500"></div>
                  <span>Blue ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <div className="myntra-color-swatch bg-red-500"></div>
                  <span>Red ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
                <label className="flex items-center space-x-2 myntra-filter-item">
                  <input type="checkbox" className="rounded" />
                  <div className="myntra-color-swatch bg-green-500"></div>
                  <span>Green ({Math.floor(Math.random() * 1000) + 100})</span>
                </label>
              </div>
            </div>
          </div>

          {/* Products Grid */}
          <div className="flex-1">
            {/* Error Message */}
            {error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                {error}
              </div>
            )}

            {/* Products Grid */}
            <div className="myntra-grid">
              {filteredProducts.map((product) => (
                <ProductCard
                  key={product.id}
                  product={product}
                  onEdit={handleEdit}
                  onDelete={handleDelete}
                  isSeller={isSeller}
                />
              ))}
            </div>

            {/* Empty State */}
            {filteredProducts.length === 0 && !loading && (
              <div className="text-center py-16">
                <div className="text-6xl text-gray-300 mb-4">🔍</div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">No products found</h3>
                <p className="text-gray-500 mb-6">Try adjusting your search or filter criteria</p>
                {isSeller && (
                  <button
                    onClick={() => setShowAdminPanel(true)}
                    className="myntra-btn-primary"
                  >
                    Add Your First Product
                  </button>
                )}
              </div>
            )}

            {/* Add Product Button for Sellers */}
            {isSeller && (
              <div className="mt-8 text-center">
                <button
                  onClick={() => setShowAdminPanel(true)}
                  className="myntra-btn-primary"
                >
                  Add Product
                </button>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Admin Panel */}
      <AdminPanel
        isOpen={showAdminPanel}
        onClose={() => setShowAdminPanel(false)}
        onAddProduct={handleAddProduct}
        onEditProduct={handleEditProduct}
        editingProduct={editingProduct}
        onCancel={handleCancel}
      />
    </div>
  );
};

export default Products;
