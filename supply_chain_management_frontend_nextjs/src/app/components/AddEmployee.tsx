'use client'
import React, { useState, FormEvent } from 'react'

const AddEmployee = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleNewEmployeeSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault
        const formData = new FormData(event.currentTarget)
        const name = formData?.get('username')
        const pass = formData?.get('password')
        const first = formData.get('firstName')
        const last = formData.get('lastName')
        const admin = formData.get('admin')

        const res = await fetch('/api/employees/add', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: name,
                                 password: pass,
                                 firstName: first,
                                 lastName: last,
                                 admin: admin,
          }),
        })

        if(res.statusText != "OK"){
            return "error"
        } else{
            setPostComplete(true);
            //const newUser = await res.json();
            //console.log("new user created: ", newUser)
        }
    }

    if(postComplete){
        return (
            <div>
                <div>-----------------------</div>
                <h3>Add a New Employee:</h3>

                <form onSubmit={handleNewEmployeeSubmit}>
                    <input type="username" name="username" placeholder="Username"/>
                    <input type="password" name="password" placeholder="Password"/>
                    <input type="text" name="firstName" placeholder="First Name" required />
                    <input type="text" name="lastName" placeholder="Last Name" required/>
                    <input type="text" name="admin" placeholder="Admin?" required/>
                    <button type="submit">Add</button>
                </form>

                <h1>employee added successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>-----------------------</div>
                <h3>Add a New Employee:</h3>

                <form onSubmit={handleNewEmployeeSubmit}>
                    <input type="username" name="username" placeholder="Username"/>
                    <input type="password" name="password" placeholder="Password"/>
                    <input type="text" name="firstName" placeholder="First Name" required />
                    <input type="text" name="lastName" placeholder="Last Name" required/>
                    <input type="text" name="admin" placeholder="Admin?" required/>
                    <button type="submit">Add</button>
                </form>
            </div>
        )
    }
}

export default AddEmployee