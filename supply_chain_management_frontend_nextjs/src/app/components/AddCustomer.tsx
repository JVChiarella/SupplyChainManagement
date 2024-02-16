'use client'
import React, { useState, FormEvent } from 'react'

const AddCustomer = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleNewCustomerSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const name = formData?.get('username')
        const pass = formData?.get('password')
        const first = formData.get('firstName')
        const last = formData.get('lastName')
        const address = formData.get('address')
        const phone = formData.get('phoneNumber')

        //create necessary body to send to api for request
        const endpoint = "customers/add"
        const payload = { credentials: { username: name, 
                                         password: pass },
                          firstName: first,
                          lastName: last,
                          active: true,
                          address: address,
                          phoneNumber: phone }

        const res = await fetch('/api/crud/post', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint,
                                 payload: payload
          }),
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
                <h3>Add a New Customer:</h3>

                <form onSubmit={handleNewCustomerSubmit}>
                    <div className="fields">
                        <input type="username" name="username" placeholder="Username"/>
                        <input type="password" name="password" placeholder="Password"/>
                        <input type="text" name="firstName" placeholder="First Name" required />
                        <input type="text" name="lastName" placeholder="Last Name" required/>
                        <input type="text" name="address" placeholder="Address" required/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number" required/>
                    </div>
                    <div className='submit-button'>
                        <button type="submit">Update</button>
                    </div>
                </form>

                <h1>customer added successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Add a New Customer:</h3>

                <form onSubmit={handleNewCustomerSubmit}>
                    <div className="fields">
                        <input type="username" name="username" placeholder="Username"/>
                        <input type="password" name="password" placeholder="Password"/>
                        <input type="text" name="firstName" placeholder="First Name" required />
                        <input type="text" name="lastName" placeholder="Last Name" required/>
                        <input type="text" name="address" placeholder="Address" required/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number" required/>
                    </div>
                    <div className='submit-button'>
                        <button type="submit">Update</button>
                    </div>
                </form>
            </div>
        )
    }
}

export default AddCustomer