'use client';
import React from 'react'
import { FormEvent } from 'react'
import { useRouter } from 'next/navigation'
import { signIn } from 'next-auth/react';

function LoginPage() {
  const router = useRouter()
 
  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
 
    const formData = new FormData(event.currentTarget)
    const name = formData.get('username')
    const pass = formData.get('password')

    const result = await signIn("credentials",{ 
      username: name,
      password: pass,
      redirect: true,
      callbackUrl: "/home"
    })
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