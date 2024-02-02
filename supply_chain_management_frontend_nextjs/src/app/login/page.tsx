'use client';
import React from 'react'
import { FormEvent } from 'react'
import { useRouter } from 'next/navigation'
import UserService from '@/app/services/user.service';
import { PostEmployee } from '../api/login/route';

function LoginPage() {
  const router = useRouter()

  const user : UserService = new UserService();
 
  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
 
    const formData = new FormData(event.currentTarget)
    const username = formData.get('username')
    const password = formData.get('password')
 
    const response = await PostEmployee(username, password).then((response) => {
      console.log(response);
      if(response != null){
        //login success
        console.log("im here");
        router.push("/home");
      } else {
        //login failed
        console.log("i shouldnt be here");
        router.push("/");
      }
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