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
            <div>
                <h1>Customers</h1>
                <div>-----------------------</div>
                <ul>
                {customers.map(user => <li key={user.id}>{user.id}{user.active}{user.firstName}{user.lastName}{user.phoneNumber}{user.address}{user.email}</li>)}
                </ul>
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
    const res = await fetch('/api/customers/getAll', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    });
  
    const users = await res.json();
    return users;
}

export default GetAllCustomers
  