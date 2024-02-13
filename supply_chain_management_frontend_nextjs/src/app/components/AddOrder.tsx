'use client'
import React, { useState, FormEvent } from 'react'

const NewOrder = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleNewOrderSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const items = formData?.get('items')

        const res = await fetch('/api/orders/customer/new', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ ordered_items: items,
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
                <h3>Place a New Order:</h3>

                <form onSubmit={handleNewOrderSubmit}>
                    <input type="text" name="items" placeholder="Items" required/>
                    <button type="submit">Add</button>
                </form>

                <h1>order placed successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>-----------------------</div>
                <h3>Place a New Order:</h3>

                <form onSubmit={handleNewOrderSubmit}>
                    <input type="text" name="items" placeholder="Items" required/>
                    <button type="submit">Add</button>
                </form>
            </div>
        )
    }
}

export default NewOrder