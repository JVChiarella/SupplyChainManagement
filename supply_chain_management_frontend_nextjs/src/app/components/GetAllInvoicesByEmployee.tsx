'use client'
import React, { useEffect, useState } from 'react'

const GetAllInvoicesByEmployee = (props : any) => {

    const defaultInvoices : Invoice[] = [];
    const [gotInvoices, setGotInvoices] = useState(false);
    const [invoices, setInvoices] = useState(defaultInvoices);
    const [error, setError] = useState(false);
    
    useEffect(() => {
        setGotInvoices(false);
        const getData = async () => {
            //fetch data from spring api
            const data : Invoice[] | any = await getInvoices();

            if(data['message']){
                //handle error
                setError(true)
            } else {       
                //update state and return
                setInvoices(data);
                setGotInvoices(true);
                return
            }
        };
        getData().then(() => {
            if(props.updateFetch){
                props.setUpdateFetch(false)
            }
        })
    }, [props.updateFetch]);
  
    if(gotInvoices){
        return (
            <div className="crud-items">
                <div className='subtitle'>Current Invoices</div>
                <div className='table-container'>
                    <table>
                        <tbody>
                            <tr>
                                <th>Invoice ID</th>
                                <th>Price</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                            {invoices.map(invoice => 
                                <tr key={invoice.id}>
                                    <td>{invoice.id}</td>
                                    <td>{invoice.totalPrice}</td>
                                    <td>{invoice.order?.date?.toString().substring(0, 10)}</td>
                                    <td>{invoice.status}</td>
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
                --An Error Occured While Loading Invoices--
            </div>
        )
    } else {
        return (
            <div>
                Loading Invoices...
            </div>
        )
    }
}

async function getInvoices(){
    //get employee id from their token
    const res1 = await fetch('/api/getUserInfo', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    });

    const data = await res1.json();
    const id = data.id;

    //get invoices belonging to employee
    const res2 = await fetch('/api/crud/get', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ endpoint: `invoices/employee/${id}`})
    });
  
    const users = await res2.json();
    return users;
}

export default GetAllInvoicesByEmployee
  