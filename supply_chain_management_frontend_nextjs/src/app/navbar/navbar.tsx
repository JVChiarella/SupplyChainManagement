import React from 'react'
import NavButton from './navButton/navButton';

const Navbar = () => {
  return (
    <div className="navbar-container">
        <NavButton name="home"></NavButton>
        <NavButton name="users"></NavButton>
        <NavButton name="logout"></NavButton>
    </div>
  )
}

export default Navbar