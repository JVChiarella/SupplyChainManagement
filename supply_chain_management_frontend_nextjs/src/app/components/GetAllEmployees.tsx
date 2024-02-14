'use client'
import React, { useEffect, useState } from 'react'

const GetAllEmployees = () => {

    const defaultEmployees : Employee[] = [];
    const [gotUsers, setGotUsers] = useState(false);
    const [employees, setEmployees] = useState(defaultEmployees);
    const [error, setError] = useState(false);

    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : Employee[] | any = await getEmployees();

            if(data['message']){
                //handle error
                setError(true)
            } else {        
                //update state and return
                setEmployees(data);
                setGotUsers(true);
                return
            }
        };
        getData();
    }, []);
  
    if(gotUsers){
        return (
            <div>
                <h1>Employees</h1>
                <div>-----------------------</div>
                <ul>
                    {employees.map(user => <li key={user.id}>{user.id}{user.admin}{user.active}{user.firstName}{user.lastName}</li>)}
                </ul>
            </div>
        )
    } else if(error){
        return (
            <div>
                --An Error Occured While Loading Employees--
            </div>
        )
    } else {
        return (
            <div>
                Loading Employees...
            </div>
        )
    }
}

async function getEmployees(){
    const res = await fetch('/api/crud/get', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ endpoint: "employees" })
      });
    
    const employees = await res.json();
    return employees;
}
  
export default GetAllEmployees