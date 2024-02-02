'use client';
import React from 'react'
import { FormEvent } from 'react'
import { useRouter } from 'next/navigation'
import UserService from '@/app/services/user.service';

function LoginPage() {
  const router = useRouter()

  const user : UserService = new UserService();
 
  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
 
    const formData = new FormData(event.currentTarget)
    const username = formData.get('username')
    const password = formData.get('password')
 
    const response = await fetch('http://localhost:8080/login/employee', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    }).then(async (response) => {
        if(response.ok){
          const result = await response.json();
          localStorage.setItem("user", JSON.stringify(result));
          //console.log(result);
          console.log(localStorage.getItem("user"));
          user.setEmployee(result, username, password);
          router.push('/home')
        } else {
          //handle errors
        }
      }
    )
  }
 
  return (
    <form onSubmit={handleSubmit}>
      <input type="username" name="username" placeholder="Username" required />
      <input type="password" name="password" placeholder="Password" required />
      <button type="submit">Login</button>
    </form>
  )
}
export default LoginPage