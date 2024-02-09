'use client'

import React, { useEffect, useState } from 'react'
import Navbar from "../navbar/navbar";

const UsersPage = () => {
  const defaultEmployees : Employee[] = [];
  const defaultCustomers : Customer[] = [];
  const [gotUsers, setGotUsers] = useState(false);
  const [employees, setEmployees] = useState(defaultEmployees)
  const [customers, setCustomers] = useState(defaultCustomers)
  useEffect(() => {
      const getData = async () => {
        //fetch data from spring api
        const data : Employee[] = await getEmployees();
        const data2 : Customer[] = await getCustomers();

        //update state and return
        setEmployees(data);
        setCustomers(data2);
        setGotUsers(true);
        return
      };
      getData();
  });

  if(!gotUsers){
      return (
        <p>
            Loading...
        </p>
  )} else { 
    return (
      <div>
        <Navbar></Navbar>
          <h1>Employees</h1>
          <ul>
            {employees.map(user => <li key={user.id}>{user.admin}{user.active}{user.firstName}{user.lastName}</li>)}
          </ul>

          <div>-------------------------</div>

          <h1>Customers</h1>
          <ul>
            {customers.map(user => <li key={user.id}>{user.active}{user.firstName}{user.lastName}{user.phoneNumber}{user.address}{user.email}</li>)}
          </ul>
      </div>
    )
  }
}

async function getEmployees(){
  const res = await fetch('/api/employees/getAll', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });

  const users = await res.json();
  return users;
}


async function getCustomers(){
  const res = await fetch('/api/customers/getAll', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });

  const users = await res.json();
  return users;
}

export default UsersPage