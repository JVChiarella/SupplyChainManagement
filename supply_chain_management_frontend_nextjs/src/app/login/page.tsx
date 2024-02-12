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
      <div>
      <h1>
        Customer Login
      </h1>
      <form onSubmit={handleCustomerSubmit}>
        <input type="username" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </form>

      <h1>
        Employee Login
      </h1>
      <form onSubmit={handleEmployeeSubmit}>
        <input type="username" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </form>
      <h2>
        Incorrect Username or Password
      </h2>
    </div>
    )
  } else {
    return (
      <div>
        <h1>
          Customer Login
        </h1>
        <form onSubmit={handleCustomerSubmit}>
          <input type="username" name="username" placeholder="Username" required />
          <input type="password" name="password" placeholder="Password" required />
          <button type="submit">Login</button>
        </form>

        <h1>
          Employee Login
        </h1>
        <form onSubmit={handleEmployeeSubmit}>
          <input type="username" name="username" placeholder="Username" required />
          <input type="password" name="password" placeholder="Password" required />
          <button type="submit">Login</button>
        </form>
      </div>
    )
  }
}

export default LoginPage