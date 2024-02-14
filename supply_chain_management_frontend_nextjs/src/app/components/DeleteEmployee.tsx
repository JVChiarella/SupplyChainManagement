'use client'
import React, { useState, FormEvent } from 'react'

const DeleteEmployee = () => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handleDeleteEmployeeSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData?.get('id')
        const endpoint = `employees/delete/${id}`

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
                <h3>Delete an Employee:</h3>

                <form onSubmit={handleDeleteEmployeeSubmit}>
                    <input type="text" name="id" placeholder="Employee ID" required />
                    <button type="submit">Delete</button>
                </form>

                <h1>deleted from employee successfully!</h1>
            </div>
        )
    } else {
        return (
            <div>
                <div>------------------------------------------------------------------------------------------------------------------------------------</div>
                <h3>Delete an Employee:</h3>

                <form onSubmit={handleDeleteEmployeeSubmit}>
                <input type="text" name="id" placeholder="Employee ID" required />
                    <button type="submit">Delete</button>
                </form>
            </div>
        )
    }
}

export default DeleteEmployee