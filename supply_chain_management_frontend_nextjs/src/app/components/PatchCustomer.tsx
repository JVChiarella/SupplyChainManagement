'use client'
import React, { useState, FormEvent } from 'react'

const PatchCustomer = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handlePatchCustomerSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData.get('id')
        const first = formData.get('firstName')
        const last = formData.get('lastName')
        const address = formData.get('address')
        const phone = formData.get('phoneNumber')

        //create necessary body to send to api for request
        const endpoint = `customers/patch/${id}`
        const payload = { firstName: first,
                          lastName: last,
                          address: address,
                          phoneNumber: phone }

        const res = await fetch('/api/crud/patch', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint,
                                 payload: payload}),
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
                <h3>Modify a Customer's Information:</h3>

                <form onSubmit={handlePatchCustomerSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Customer ID" required/>
                        <input type="text" name="firstName" placeholder="First Name" />
                        <input type="text" name="lastName" placeholder="Last Name" />
                        <input type="text" name="address" placeholder="Address"/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                    </div>
                    <div className='submit-button'>
                        <button type="submit">Update</button>
                    </div>
                </form>

                <h1>customer data modified successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Modify a Customer's Information:</h3>

                <form onSubmit={handlePatchCustomerSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Customer ID" required/>
                        <input type="text" name="firstName" placeholder="First Name" />
                        <input type="text" name="lastName" placeholder="Last Name"/>
                        <input type="text" name="address" placeholder="Address" />
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                    </div>
                    <div className='submit-button'>
                        <button type="submit">Update</button>
                    </div>
                </form>
            </div>
        )
    }
}

export default PatchCustomer