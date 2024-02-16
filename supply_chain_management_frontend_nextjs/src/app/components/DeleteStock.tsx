'use client'
import React, { useState, FormEvent } from 'react'

const DeleteStock = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleDeleteStockSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData?.get('id')
        const endpoint = `stock/delete/${id}`

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
            <div className="crud-items">
                <h3>Delete a Product from Stock:</h3>

                <form onSubmit={handleDeleteStockSubmit}>
                    <div className="fields">
                        <div className="fields">
                            <input type="text" name="id" placeholder="Stock ID" required />
                        </div>
                        <button className='submit-button' type="submit">Delete</button>
                    </div>
                </form>

                <h1>product removed from stock successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Delete a Product from Stock:</h3>

                <form onSubmit={handleDeleteStockSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Stock ID" required />
                    </div>
                    <button className='submit-button' type="submit">Delete</button>
                </form>
            </div>
        )
    }
}

export default DeleteStock