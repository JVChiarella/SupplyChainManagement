'use client'
import React, { useEffect, useState } from 'react'

const GetAllCustomers = () => {

    const defaultCustomers : Customer[] = [];
    const [gotUsers, setGotUsers] = useState(false);
    const [customers, setCustomers] = useState(defaultCustomers);
    const [error, setError] = useState(false);
    
    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : Customer[] | any = await getCustomers();

            if(data['message']){
                //handle error
                setError(true)
            } else {        
                //update state and return
                setCustomers(data);
                setGotUsers(true);
                return
            }
        };
        getData();
    }, []);
  
    if(gotUsers){
        return (
            <div className="crud-items">
                <div className='subtitle'>Customers</div>
                <div className='table-container'>
                    <table>
                        <tbody>
                            <tr>
                                <th>ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Phone Number</th>
                                <th>Address</th>
                            </tr>
                            {customers.map(user => 
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>{user.address}</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
        </div>
        )
    } else if(error){
        return (
            <div>
                --An Error Occured While Loading Customers--
            </div>
        )
    } else {
        return (
            <div>
                Loading Customers...
            </div>
        )
    }
}

async function getCustomers(){
    const res = await fetch('/api/crud/get', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ endpoint: "customers"})
    });
  
    const users = await res.json();
    return users;
}

export default GetAllCustomers
  