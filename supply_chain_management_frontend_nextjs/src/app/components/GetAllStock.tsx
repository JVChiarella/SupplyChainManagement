'use client'
import React, { useEffect, useState } from 'react'

const GetAllStock = () => {

    const defaultStock : StockItem[] = [];
    const [gotStock, setGotStock] = useState(false);
    const [stock, setStock] = useState(defaultStock)
    const [error, setError] = useState(false)
    
    useEffect(() => {
        const getData = async () => {
            //fetch data from spring api
            const data : StockItem[] | any = await getStock();

            if(data['message']){
                //handle error
                setError(true)
            } else {           
                //update state and return
                setStock(data);
                setGotStock(true);
                return
            }
        };
        getData();
    }, []);
  
    if(gotStock){
        return (
            <div>
                <div>---------------------------------------------------------------------</div>
                <ul>
                {stock.map(item=> <li key={item.id}>{item.name}{item.description}{item.count}{item.price}</li>)}
                </ul>
            </div>
        )
    } else if(error){
        return (
            <div>
                --An Error Occured While Loading Stock--
            </div>
        )
    } else {
        return (
            <div>
                Loading Stock...
            </div>
        )
    }
}

async function getStock(){
    const res = await fetch('/api/stock/getAll', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    });
  
    const users = await res.json();
    return users;
}

export default GetAllStock
  