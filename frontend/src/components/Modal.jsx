import React from 'react';

const modalStyles = {
  overlay: {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.7)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000,
  },
  content: {
    backgroundColor: '#282c34', 
    padding: '20px',
    borderRadius: '8px',
    border: '1px solid #fff',
    minWidth: '400px',
    maxWidth: '90%',
    position: 'relative',
    zIndex: 1001,
  },
  closeButton: {
    position: 'absolute',
    top: '10px',
    right: '10px',
    background: 'transparent',
    border: 'none',
    color: 'white',
    fontSize: '24px',
    cursor: 'pointer',
  }
};

const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) {
    return null;
  }

  return (
    <div style={modalStyles.overlay} onClick={onClose}>
      <div style={modalStyles.content} onClick={(e) => e.stopPropagation()}>
        <button style={modalStyles.closeButton} onClick={onClose}>&times;</button>
        <h2>{title}</h2>
        {children}
      </div>
    </div>
  );
};

export default Modal;