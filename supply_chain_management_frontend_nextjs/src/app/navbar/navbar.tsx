import React from 'react'
import NavButton from './navButton';
import SignOutButton from './SignOutButton';

const Navbar = () => {
  return (
    <div className="navbar-container">
        <NavButton name="home"></NavButton>
        <NavButton name="users"></NavButton>
        <SignOutButton></SignOutButton>
    </div>
  )
}

export default Navbar