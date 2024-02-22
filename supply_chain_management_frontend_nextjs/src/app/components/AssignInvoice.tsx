'use client'
import React, { useState, FormEvent } from 'react'

const AssignInvoice = (props : any) => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleAssignInvoiceSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        setPostComplete(false);
        const formData = new FormData(event.currentTarget)
        const id = formData.get('id')

        //create necessary body to send to api for request
        const endpoint = `invoices/patch/${id}`

        const res = await fetch('/api/crud/patch', {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint })
        })

        if(res.statusText != "OK"){
            return "error"
        } else{
            setPostComplete(true);
            props.setUpdateFetch(true);
        }
    }

    if(postComplete){
        return (
            <div className="crud-items">
                <h3>Assign New Invoice</h3>

                <form onSubmit={handleAssignInvoiceSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Order ID" required/>
                    </div>
                    <button className='submit-button' type="submit">Assign</button>
                </form>

                <h1>invoice assigned successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Assign New Invoice</h3>

                <form onSubmit={handleAssignInvoiceSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Order ID" required/>
                    </div>
                    <button className='submit-button' type="submit">Assign</button>
                </form>
            </div>
        )
    }
}

export default AssignInvoice