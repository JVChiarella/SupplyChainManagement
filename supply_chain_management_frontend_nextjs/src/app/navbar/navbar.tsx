import React from 'react'
import NavButton from './navButton/navButton';

const Navbar = () => {
  return (
    <div className="navbar-container">
        <NavButton name="home"></NavButton>
        <NavButton name="users"></NavButton>
    </div>
  )
}

export default Navbar