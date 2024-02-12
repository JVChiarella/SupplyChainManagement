'use client'
import React, { useEffect, useState } from 'react'

const GetAllEmployees = () => {

    const defaultEmployees : Employee[] = [];
    const [gotUsers, setGotUsers] = useState(false);
    const [employees, setEmployees] = useState(defaultEmployees)
    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : Employee[] = await getEmployees();

            //update state and return
            setEmployees(data);
            setGotUsers(true);
            return
        };
        getData();
    }, []);
  
    if(gotUsers){
        return (
            <div>
                <h1>Employees</h1>
                <ul>
                    {employees.map(user => <li key={user.id}>{user.admin}{user.active}{user.firstName}{user.lastName}</li>)}
                </ul>
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
    const res = await fetch('/api/employees/getAll', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });
    
    const employees = await res.json();
    return employees;
}
  
export default GetAllEmployees