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

        //check if price + count were provided; if not, set to -1 so db value is not modified
        let tempCount = count;
        let tempPrice = price;
        if(!tempCount) {
            tempCount = "-1"
        }
        if(!tempPrice){
            tempPrice = "-1"
        }

        //create necessary body to send to api for request
        const endpoint = `stock/patch/${id}`
        const payload = { name: name,
                          description: descrip,
                          count: tempCount,
                          price: tempPrice }

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
                <h3>Modify a Product in Stock:</h3>

                <form onSubmit={handlePatchStockSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Stock ID" required />
                        <input type="text" name="name" placeholder="Product Name" />
                        <input type="text" name="description" placeholder="Description" />
                        <input type="text" name="count" placeholder="Count" />
                        <input type="text" name="price" placeholder="Price" />
                    </div>
                    <button className='submit-button' type="submit">Update</button>
                </form>

                <h1>updated product in stock successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Modify a Product in Stock:</h3>

                <form onSubmit={handlePatchStockSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Stock ID" required />
                        <input type="text" name="name" placeholder="Product Name"/>
                        <input type="text" name="description" placeholder="Description"/>
                        <input type="text" name="count" placeholder="Count"/>
                        <input type="text" name="price" placeholder="Price"/>
                    </div>
                    <button className='submit-button' type="submit">Update</button>
                </form>
            </div>
        )
    }
}

export default PatchStock