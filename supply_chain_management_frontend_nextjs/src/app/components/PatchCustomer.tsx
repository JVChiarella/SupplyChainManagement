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

        const res = await fetch('/api/customers/patch', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: id,
                                 firstName: first,
                                 lastName: last,
                                 address: address,
                                 phoneNumber: phone,
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
            <div>
                <div>-----------------------</div>
                <h3>Modify a Customer's Information:</h3>

                <form onSubmit={handlePatchCustomerSubmit}>
                    <input type="text" name="id" placeholder="Customer ID" required/>
                    <input type="text" name="firstName" placeholder="First Name" />
                    <input type="text" name="lastName" placeholder="Last Name" />
                    <input type="text" name="address" placeholder="Address"/>
                    <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                    <button type="submit">Update</button>
                </form>

                <h1>customer data modified successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>-----------------------</div>
                <h3>Modify a Customer's Information:</h3>

                <form onSubmit={handlePatchCustomerSubmit}>
                    <input type="text" name="id" placeholder="Customer ID" required/>
                    <input type="text" name="firstName" placeholder="First Name" />
                    <input type="text" name="lastName" placeholder="Last Name"/>
                    <input type="text" name="address" placeholder="Address" />
                    <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                    <button type="submit">Update</button>
                </form>
            </div>
        )
    }
}

export default PatchCustomer