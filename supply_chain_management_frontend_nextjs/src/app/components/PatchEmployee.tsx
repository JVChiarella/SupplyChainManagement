'use client'
import React, { useState, FormEvent } from 'react'

const PatchEmployee = (props : any) => {

    const [ postComplete, setPostComplete ] = useState(false)

    async function handlePatchEmployeeSubmit(event: FormEvent<HTMLFormElement>){
        event?.preventDefault()
        const formData = new FormData(event.currentTarget)
        const id = formData.get('id')
        const first = formData.get('firstName')
        const last = formData.get('lastName')
        const phone = formData.get('phoneNumber')
        const admin = formData.get('admin')

        //create necessary body to send to api for request
        const endpoint = `employees/patch/${id}`
        const payload = { firstName: first,
                          lastName: last,
                          phoneNumber: phone,
                          admin: admin }

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
            props.setUpdateEmployeeFetch(true);
        }
    }

    if(postComplete){
        return (
            <div className="crud-items">
                <h3>Modify an Employee's Information:</h3>

                <form onSubmit={handlePatchEmployeeSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Employee ID" required/>
                        <input type="text" name="firstName" placeholder="First Name" />
                        <input type="text" name="lastName" placeholder="Last Name" />
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                        <input type="text" name="admin" placeholder="Admin?"/>
                    </div>
                    <button className='submit-button' type="submit">Update</button>
                </form>

                <h1>employee data modified successfully!</h1>
            </div>
        )
    } else {
        return (
            <div className="crud-items">
                <h3>Modify an Employee's Information:</h3>

                <form onSubmit={handlePatchEmployeeSubmit}>
                    <div className="fields">
                        <input type="text" name="id" placeholder="Employee ID" required/>
                        <input type="text" name="firstName" placeholder="First Name" />
                        <input type="text" name="lastName" placeholder="Last Name"/>
                        <input type="text" name="phoneNumber" placeholder="Phone Number"/>
                        <input type="text" name="admin" placeholder="Admin?"/>
                    </div>
                    <button className='submit-button' type="submit">Update</button>
                </form>
            </div>
        )
    }
}

export default PatchEmployee