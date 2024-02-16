'use client';
import React, { useState, FormEvent } from 'react'
import { useRouter } from 'next/navigation'
 
function LoginPage() {
  const router = useRouter()
  const [loginFailState, setLoginFailState] = useState(false)
 
  async function handleCustomerSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
 
    const formData = new FormData(event.currentTarget)
    const name = formData.get('username')
    const pass = formData.get('password')

    const result = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: name,
                             password: pass,
                             userType: "customer",
      }),
    });

    if(result === null){
      setLoginFailState(true);
    } else {
      router.push("./home")
    }
  }

  async function handleEmployeeSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
 
    const formData = new FormData(event.currentTarget)
    const name = formData.get('username')
    const pass = formData.get('password')

    const result = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: name,
                             password: pass,
                             userType: "employee",
      }),
    });

    if(result === null){
      setLoginFailState(true);
    } else {
      router.push("./home")
    }
  }

  if(loginFailState){
    return (
      <div className = "background">
        <div className="title">Supply Chain Management App</div>
        <div className = "login-container">
          <div className="login-items">
          <div className="subtitle">Customer Login</div>
            <form onSubmit={handleCustomerSubmit}>
              <div className="fields">
                <input type="username" name="username" placeholder="Username" required />
                <input type="password" name="password" placeholder="Password" required />
              </div>
              <div className='login-button'>
                <button type="submit">Login</button>
              </div>
            </form>

            <div className="subtitle">Employee Login</div>
            <form onSubmit={handleEmployeeSubmit}>
              <div className="fields">
                <input type="username" name="username" placeholder="Username" required />
                <input type="password" name="password" placeholder="Password" required />
              </div>
              <div className='login-button'>
                <button type="submit">Login</button>
              </div>
            </form>
            <h2>
              Incorrect Username or Password
            </h2>
          </div>
        </div>
      </div>
    )
  } else {
    return (
      <div className = "background">
        <div className="title">Supply Chain Management App</div>
        <div className = "login-container">
          <div className="login-items">
            <div className="subtitle">Customer Login</div>
            <form onSubmit={handleCustomerSubmit}>
              <div className="fields">
                <input type="username" name="username" placeholder="Username" required />
                <input type="password" name="password" placeholder="Password" required />
              </div>
              <button className='login-button' type="submit">Login</button>
            </form>

            <div className="spacing"></div>

            <div className="subtitle">Employee Login</div>
            <form onSubmit={handleEmployeeSubmit}>
              <div className="fields">
                <input type="username" name="username" placeholder="Username" required />
                <input type="password" name="password" placeholder="Password" required />
              </div>
              <button className='login-button' type="submit">Login</button>
            </form>
          </div>
        </div>
      </div>
    )
  }
}

export default LoginPage