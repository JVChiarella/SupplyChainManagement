'use client'
import React, { useState, FormEvent } from 'react'

const AddStock = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleNewStockSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const name = formData?.get('name')
        const descrip = formData.get('description')
        const count = formData.get('count')
        const price = formData.get('price')

        //create necessary body to send to api for request
        const endpoint = `stock/new`
        const payload = { name: name,
                          description: descrip,
                          count: count,
                          price: price }

        const res = await fetch('/api/crud/post', {
          method: 'POST',
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
                <h3>Add a New Product to Stock:</h3>

                <form onSubmit={handleNewStockSubmit}>
                    <div className="fields">
                        <input type="text" name="name" placeholder="Product Name" required />
                        <input type="text" name="description" placeholder="Description" required/>
                        <input type="text" name="count" placeholder="Count" required/>
                        <input type="text" name="price" placeholder="Price" required/>
                    </div>
                    <button className='submit-button' type="submit">Add</button>
                </form>

                <h1>product added to stock successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Add a New Product to Stock:</h3>

                <form onSubmit={handleNewStockSubmit}>
                    <div className="fields">
                        <input type="text" name="name" placeholder="Product Name" required />
                        <input type="text" name="description" placeholder="Description" required/>
                        <input type="text" name="count" placeholder="Count" required/>
                        <input type="text" name="price" placeholder="Price" required/>
                    </div>
                    <button className='submit-button' type="submit">Add</button>
                </form>
            </div>
        )
    }
}

export default AddStock