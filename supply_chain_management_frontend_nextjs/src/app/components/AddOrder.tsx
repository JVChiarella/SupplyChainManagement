'use client'
import React, { useState, useEffect } from 'react'

const AddOrder = (props : any) => {

    const defaultStock : StockItem[] = [];
    const defaultItems : OrderedItem[] = [];

    const [ selectedVal, setSelectedVal ] = useState("");
    const [ amount, setAmount ] = useState(0);
    const [ postComplete, setPostComplete ] = useState(false);
    const [ gotStock, setGotStock ] = useState(false);
    const [ stock, setStock ] = useState(defaultStock)
    const [ orderedItems, setOrderedItems] = useState(defaultItems);
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

    function incrementOrderItems(newItemName : String, curAmount : number){
        let newItem : OrderedItem = {stock_id : getStockFromName(newItemName)?.id,
                                     amount : curAmount}
        setOrderedItems([...orderedItems].concat(newItem))
    }

    function decrementOrderItems(removeItemIndex : number){
        if(removeItemIndex < orderedItems.length){
            setOrderedItems(orderedItems.splice(0, removeItemIndex).concat(orderedItems.splice(removeItemIndex+1,)))
        } else {
            setOrderedItems(orderedItems.splice(0, removeItemIndex))
        }
    }

    function getStockFromName(name : any){
        return  stock.find(obj => obj.name == name)
      }

      function getStockFromId(id : any){
        return stock.find(obj => obj.id == id)
      }

    const handleSelectionChange = (event : any) => {
        setSelectedVal(event.target.value)
    }

    const handleAmountChange = (event : any) => {
        setAmount(event.target.value)
    }

    async function handleNewOrderSubmit(){
        //set order
        setPostComplete(false);
        const id = await getUserId();
        const order : Order = {customer_id : id,
                               ordered_items : orderedItems}

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
            props.setUpdateFetch(true);
            setOrderedItems([]);
            setSelectedVal("");
            setAmount(0);
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
                                <th>Name</th>
                                <th>Available</th>
                                <th>Price</th>
                                <th>Amount</th>
                            </tr>
                            {orderedItems.map((item, idx) => 
                                <tr key={item.stock_id}>
                                    <td>{getStockFromId(item.stock_id)?.name}</td>
                                    <td>{getStockFromId(item.stock_id)?.count}</td>
                                    <td>{getStockFromId(item.stock_id)?.price}</td>
                                    <td>{item.amount}</td>
                                    <td><button className='remove-item-button' onClick={() => decrementOrderItems(idx)}>Remove</button></td>
                                </tr>
                            )}
                            <tr>
                                <td>
                                    <select value={selectedVal} onChange={handleSelectionChange} className = 'select-menu'>
                                        {stock.map( item => 
                                            <option key={item.name}>{item.name}</option>)}
                                    </select>
                                </td>
                                <td>{getStockFromName(selectedVal)?.count}</td>
                                <td>{getStockFromName(selectedVal)?.price}</td>
                                <td>
                                    <input type={'number'} value={amount} onChange={handleAmountChange} className="amount-box"></input>
                                </td>
                            </tr>
                            <button className='add-item-button' onClick={() => incrementOrderItems(selectedVal, amount)}>Add</button>
                        </tbody>
                    </table>
                </div>
                <div>order placed successfully!</div>
                <button className='submit-button' onClick={handleNewOrderSubmit}>Submit</button>
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
                                <th>Name</th>
                                <th>Available</th>
                                <th>Price</th>
                                <th>Amount</th>
                            </tr>
                            {orderedItems.map((item, idx) => 
                                <tr key={item.stock_id}>
                                    <td>{getStockFromId(item.stock_id)?.name}</td>
                                    <td>{getStockFromId(item.stock_id)?.count}</td>
                                    <td>{getStockFromId(item.stock_id)?.price}</td>
                                    <td>{item.amount}</td>
                                    <td><button className='remove-item-button' onClick={() => decrementOrderItems(idx)}>Remove</button></td>
                                </tr>
                            )}
                            <tr>
                                <td>
                                    <select value={selectedVal} onChange={handleSelectionChange} className = 'select-menu'>
                                        {stock.map( item => 
                                            <option key={item.name}>{item.name}</option>)}
                                    </select>
                                </td>
                                <td>{getStockFromName(selectedVal)?.count}</td>
                                <td>{getStockFromName(selectedVal)?.price}</td>
                                <td>
                                    <input type={'number'} value={amount} onChange={handleAmountChange} className="amount-box"></input>
                                </td>
                            </tr>
                            <button className='add-item-button' onClick={() => incrementOrderItems(selectedVal, amount)}>Add</button>
                        </tbody>
                    </table>
                </div>
                <button className='submit-button' onClick={handleNewOrderSubmit}>Submit</button>
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

async function getUserId(){
    //get customer id from token
    const res1 = await fetch('/api/getUserInfo', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    });

    const data = await res1.json();

    return data.id;
}

export default AddOrder