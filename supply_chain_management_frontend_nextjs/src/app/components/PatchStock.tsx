'use client'
import React, { useState, FormEvent } from 'react'

const PatchStock = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handlePatchStockSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData?.get('id')
        const name = formData?.get('name')
        const descrip = formData.get('description')
        const count = formData.get('count')
        const price = formData.get('price')

        const res = await fetch('/api/stock/patch', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: id,
                                 name: name,
                                 description: descrip,
                                 count: count,
                                 price: price,
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
                <h3>Modify a Product in Stock:</h3>

                <form onSubmit={handlePatchStockSubmit}>
                    <input type="text" name="id" placeholder="Stock ID" required />
                    <input type="text" name="name" placeholder="Product Name" />
                    <input type="text" name="description" placeholder="Description" />
                    <input type="text" name="count" placeholder="Count" />
                    <input type="text" name="price" placeholder="Price" />
                    <button type="submit">Update</button>
                </form>

                <h1>updated product in stock successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Modify a Product in Stock:</h3>

                <form onSubmit={handlePatchStockSubmit}>
                    <input type="text" name="id" placeholder="Stock ID" required />
                    <input type="text" name="name" placeholder="Product Name"/>
                    <input type="text" name="description" placeholder="Description"/>
                    <input type="text" name="count" placeholder="Count"/>
                    <input type="text" name="price" placeholder="Price"/>
                    <button type="submit">Update</button>
                </form>
            </div>
        )
    }
}

export default PatchStock