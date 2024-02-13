'use client'
import React, { useEffect, useState } from 'react'

const GetAllOrdersByCustomer = () => {

    const defaultOrders : Order[] = [];
    const [gotOrders, setGotOrders] = useState(false);
    const [orders, setOrders] = useState(defaultOrders)
    
    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : Order[] = await getOrders();

            //update state and return
            setOrders(data);
            setGotOrders(true);
            return
        };
        getData();
    }, []);
  
    if(gotOrders){
        return (
            <div>
                <h1>Orders</h1>
                <div>-----------------------</div>
                <ul>
                {orders.map(order => <li key={order.id}>{order.id}</li>)}
                </ul>
            </div>
        )
    } else {
        return (
            <div>
                Loading Orders...
            </div>
        )
    }
}

async function getOrders(){
    const res = await fetch('/api/orders/customer/getAll', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    });
  
    const users = await res.json();
    return users;
}

export default GetAllOrdersByCustomer
  