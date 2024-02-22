'use client'
import React, { useEffect, useState } from 'react'

const GetAllOrdersByCustomer = (props : any) => {

    const defaultOrders : Order[] = [];
    const [gotOrders, setGotOrders] = useState(false);
    const [orders, setOrders] = useState(defaultOrders);
    const [error, setError] = useState(false);
    
    useEffect(() => {
        setGotOrders(false);
        const getData = async () => {
            //fetch data from spring api
            const data : Order[] | any = await getOrders();

            if(data['message']){
                //handle error
                setError(true)
            } else {       
                //update state and return
                setOrders(data);
                setGotOrders(true);
                return
            }
        };
        getData().then(() => {
            if(props.updateFetch){
                props.setUpdateFetch(false);
            }
        })
    }, [props.updateFetch]);
  
    if(gotOrders){
        return (
            <div className="crud-items">
                <div className='subtitle'>Current Orders</div>
                <div className='table-container'>
                    <table>
                        <tbody>
                            <tr>
                                <th>Order ID</th>
                                <th>Price</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                            {orders.map(order => 
                                <tr key={order.id}>
                                    <td>{order.id}</td>
                                    <td>{order.invoice?.totalPrice}</td>
                                    <td>{order.date?.toString().substring(0, 10)}</td>
                                    <td>{order.invoice?.status}</td>
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
                --An Error Occured While Loading Orders--
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
    //get customer id from their token
    const res1 = await fetch('/api/getUserInfo', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    });

    const data = await res1.json();
    const id = data.id

    //get orders belonging to customer
    const res2 = await fetch('/api/crud/get', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ endpoint: `orders/customer/${id}`})
    });
  
    const users = await res2.json();
    return users;
}

export default GetAllOrdersByCustomer
  