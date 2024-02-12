'use client'
import React, { useEffect, useState } from 'react'

const GetAllCustomers = () => {

    const defaultCustomers : Customer[] = [];
    const [gotUsers, setGotUsers] = useState(false);
    const [customers, setCustomers] = useState(defaultCustomers)
    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : Customer[] = await getCustomers();

            //update state and return
            setCustomers(data);
            setGotUsers(true);
            return
        };
        getData();
    }, []);
  
    if(gotUsers){
        return (
            <div>
                <h1>Customers</h1>
                <ul>
                {customers.map(user => <li key={user.id}>{user.active}{user.firstName}{user.lastName}{user.phoneNumber}{user.address}{user.email}</li>)}
                </ul>
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
    const res = await fetch('/api/customers/getAll', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    });
  
    const users = await res.json();
    return users;
}

export default GetAllCustomers
  