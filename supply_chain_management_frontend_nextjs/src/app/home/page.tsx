import React from 'react'
import Navbar from '../navbar/navbar'

const HomePage = () => {
  const user : any = localStorage.getItem("user");
  return (
    <div>
        <Navbar></Navbar>
        Home Page
        <p>
          Welcome {user["firstName"]}
        </p>
    </div>
  )
}

export default HomePage