import React from 'react'
import NavButton from './navButton';
import SignOutButton from './SignOutButton';

const Navbar = (props : any) => {
  if(props.type == "customer"){
    //customer navbar
    return(
      <div className="navbar-container">
        <NavButton name="home"></NavButton>
        <SignOutButton></SignOutButton>
      </div>
  )} else {
    //employee navbar
    return (
      <div className="navbar-container">
          <NavButton name="home"></NavButton>
          <NavButton name="users"></NavButton>
          <SignOutButton></SignOutButton>
      </div>
  )}
}

export default Navbar