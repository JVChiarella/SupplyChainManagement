'use client'
import React, { useState, FormEvent } from 'react'

const DeleteCustomer = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleDeleteCustomerSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData?.get('id')
        const endpoint = `customers/delete/${id}`

        const res = await fetch('/api/crud/patch', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint, })
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
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Delete a Customer:</h3>

                <form onSubmit={handleDeleteCustomerSubmit}>
                    <input type="text" name="id" placeholder="Customer ID" required />
                    <button type="submit">Delete</button>
                </form>

                <h1>deleted from Customer successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Delete a Customer:</h3>

                <form onSubmit={handleDeleteCustomerSubmit}>
                <input type="text" name="id" placeholder="Customer ID" required />
                    <button type="submit">Delete</button>
                </form>
            </div>
        )
    }
}

export default DeleteCustomer