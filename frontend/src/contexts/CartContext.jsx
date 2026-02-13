import { createContext, useContext, useReducer, useEffect } from 'react';

const CartContext = createContext();

const cartReducer = (state, action) => {
  switch (action.type) {
    case 'ADD_TO_CART':
      const existingItem = state.items.find(item => 
        item.productId === action.payload.productId && 
        item.size === action.payload.size
      );
      
      if (existingItem) {
        return {
          ...state,
          items: state.items.map(item =>
            item.productId === action.payload.productId && item.size === action.payload.size
              ? { ...item, quantity: item.quantity + action.payload.quantity }
              : item
          )
        };
      } else {
        return {
          ...state,
          items: [...state.items, action.payload]
        };
      }
    
    case 'REMOVE_FROM_CART':
      return {
        ...state,
        items: state.items.filter(item => 
          !(item.productId === action.payload.productId && item.size === action.payload.size)
        )
      };
    
    case 'UPDATE_QUANTITY':
      return {
        ...state,
        items: state.items.map(item =>
          item.productId === action.payload.productId && item.size === action.payload.size
            ? { ...item, quantity: action.payload.quantity }
            : item
        )
      };
    
    case 'CLEAR_CART':
      return {
        ...state,
        items: []
      };
    
    case 'SET_SELECTED_SIZE':
      return {
        ...state,
        selectedSize: action.payload
      };
    
    default:
      return state;
  }
};

export const CartProvider = ({ children }) => {
  const [state, dispatch] = useReducer(cartReducer, {
    items: [],
    selectedSize: null
  });

  // Load cart from localStorage on mount
  useEffect(() => {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      const parsedCart = JSON.parse(savedCart);
      dispatch({ type: 'CLEAR_CART' });
      parsedCart.items.forEach(item => {
        dispatch({ type: 'ADD_TO_CART', payload: item });
      });
    }
  }, []);

  // Save cart to localStorage whenever it changes
  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(state));
  }, [state]);

  const addToCart = (product, size, quantity = 1) => {
    dispatch({
      type: 'ADD_TO_CART',
      payload: {
        productId: product.id,
        productName: product.name,
        productPrice: product.price,
        productImage: product.image || '📱',
        size,
        quantity
      }
    });
  };

  const removeFromCart = (productId, size) => {
    dispatch({
      type: 'REMOVE_FROM_CART',
      payload: { productId, size }
    });
  };

  const updateQuantity = (productId, size, quantity) => {
    if (quantity <= 0) {
      removeFromCart(productId, size);
    } else {
      dispatch({
        type: 'UPDATE_QUANTITY',
        payload: { productId, size, quantity }
      });
    }
  };

  const clearCart = () => {
    dispatch({ type: 'CLEAR_CART' });
  };

  const setSelectedSize = (size) => {
    dispatch({ type: 'SET_SELECTED_SIZE', payload: size });
  };

  const getTotalItems = () => {
    return state.items.reduce((total, item) => total + item.quantity, 0);
  };

  const getTotalPrice = () => {
    return state.items.reduce((total, item) => total + (item.productPrice * item.quantity), 0);
  };

  const value = {
    ...state,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    setSelectedSize,
    getTotalItems,
    getTotalPrice
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

