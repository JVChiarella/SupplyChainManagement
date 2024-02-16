'use client'
import React, { useState, FormEvent } from 'react'

const AddEmployee = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleNewEmployeeSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const name = formData?.get('username')
        const pass = formData?.get('password')
        const first = formData.get('firstName')
        const last = formData.get('lastName')
        const phone = formData.get('phoneNumber')
        const admin = formData.get('admin')

        //create necessary body to send to api for request
        const endpoint = "employees/add"
        const payload = { credentials: { username: name, 
                                         password: pass },
                            firstName: first,
                            lastName: last,
                            phoneNumber: phone,
                            active: true,
                            admin: admin}

        const res = await fetch('/api/crud/post', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint,
                                 payload: payload }),
        })

        if(res.statusText != "OK"){
            return "error"
        } else{
            setPostComplete(true);
        }
    }

    if(postComplete){
        return (
            <div className="crud-items">
                <h3>Add a New Employee:</h3>

                <form onSubmit={handleNewEmployeeSubmit}>
                    <div className="fields">
                        <input type="username" name="username" placeholder="Username"/>
                        <input type="password" name="password" placeholder="Password"/>
                        <input type="text" name="firstName" placeholder="First Name" required />
                        <input type="text" name="lastName" placeholder="Last Name" required/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                        <input type="text" name="admin" placeholder="Admin?" required/>
                    </div>
                    <button className='submit-button' type="submit">Add</button>
                </form>

                <h1>employee added successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Add a New Employee:</h3>

                <form onSubmit={handleNewEmployeeSubmit}>
                    <div className="fields">
                        <input type="username" name="username" placeholder="Username"/>
                        <input type="password" name="password" placeholder="Password"/>
                        <input type="text" name="firstName" placeholder="First Name" required />
                        <input type="text" name="lastName" placeholder="Last Name" required/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                        <input type="text" name="admin" placeholder="Admin?" required/>
                    </div>
                    <button className='submit-button' type="submit">Add</button>
                </form>
            </div>
        )
    }
}

export default AddEmployee