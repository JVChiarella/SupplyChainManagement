'use client'
import React, { useState, FormEvent } from 'react'

const DeleteStock = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleDeleteStockSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData?.get('id')

        const res = await fetch('/api/stock/delete', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: id,
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
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Delete a Product from Stock:</h3>

                <form onSubmit={handleDeleteStockSubmit}>
                    <input type="text" name="id" placeholder="Stock ID" required />
                    <button type="submit">Delete</button>
                </form>

                <h1>product removed from stock successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Delete a Product from Stock:</h3>

                <form onSubmit={handleDeleteStockSubmit}>
                <input type="text" name="id" placeholder="Stock ID" required />
                    <button type="submit">Delete</button>
                </form>
            </div>
        )
    }
}

export default DeleteStock