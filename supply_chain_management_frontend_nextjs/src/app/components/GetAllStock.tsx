'use client'
import React, { useEffect, useState } from 'react'

const GetAllStock = (props : any) => {

    const defaultStock : StockItem[] = [];
    const [gotStock, setGotStock] = useState(false);
    const [stock, setStock] = useState(defaultStock)
    const [error, setError] = useState(false)
    
    useEffect(() => {
        setGotStock(false);
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
        getData().then(() => {
            if(props.updateFetch){
                props.setUpdateFetch(false);
            }
        })
    }, [props.updateFetch]);
  
    if(gotStock){
        return (
            <div className="crud-items">
                <div className='subtitle'>Browse Stock</div>
                <div className='table-container'>
                    <table>
                        <tbody>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Count</th>
                                <th>Price</th>
                            </tr>
                            {stock.map(item => 
                                <tr key={item.id}>
                                    <td>{item.id}</td>
                                    <td>{item.name}</td>
                                    <td>{item.description}</td>
                                    <td>{item.count}</td>
                                    <td>{item.price}</td>
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
    const res = await fetch('/api/crud/get', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ endpoint: "stock"})
    });
  
    const users = await res.json();
    return users;
}

export default GetAllStock
  