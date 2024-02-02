import React from 'react'
import NavButton from './navButton/navButton';

const Navbar = () => {
  return (
    <main>
        <NavButton name="home"></NavButton>
        <NavButton name="users"></NavButton>
    </main>
  )
}

export default Navbar