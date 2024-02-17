'use client'
import React, { useState, useEffect } from 'react'

//WIP -> need to be able to add stock to ordered items array of newOrder + set state to submit to API
const AddOrder = () => {

    const defaultOrder : Order = {customer_id : 0,
                                  invoice : {status : "", totalPrice : 0},
                                  ordered_items : [],
                                  date : new Date(0)}
    const defaultStock : StockItem[] = [];

    const [ postComplete, setPostComplete ] = useState(false);
    const [ gotStock, setGotStock ] = useState(false);
    const [ stock, setStock ] = useState(defaultStock)
    const [ newOrder, setNewOrder ] = useState(defaultOrder);
    const [ error, setError] = useState(false)
        
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

    async function handleNewOrderSubmit(order : Order){
        //create necessary body to send to api for request
        const endpoint = `orders/new`
        const payload = order

        const res = await fetch('/api/crud/post', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ endpoint: endpoint,
                                 payload: payload}),
        })

        if(res.statusText != "OK"){
            return "error"
        } else{
            setPostComplete(true);
        }
    }

    if(postComplete){
        return (
            <div className="crud-items">
                <div className='subtitle'>Place a New Order</div>
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
                <button className='submit-button' onClick={()=> handleNewOrderSubmit(newOrder)}>Submit</button>
                <h1>order placed successfully!</h1>
            </div>
        )
    } else if(gotStock){
        return (
            <div className="crud-items">
                <div className='subtitle'>Place a New Order</div>
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
                <button className='submit-button' onClick={()=> handleNewOrderSubmit(newOrder)}>Submit</button>
            </div>
        )
    } else if(error){
        return (
            <div>
                --An Error Occured While Loading Stock--
            </div>
        )
    } else {
        <div>Loading...</div>
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

export default AddOrder