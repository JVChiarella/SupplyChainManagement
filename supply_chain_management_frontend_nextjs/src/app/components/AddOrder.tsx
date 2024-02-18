'use client'
import React, { useState, useEffect } from 'react'
import NewOrderItem from '../components/NewOrderItem'

//WIP -> need to be able to add stock to ordered items array of newOrder + set state to submit to API
const AddOrder = () => {

    const defaultOrder : Order = {customer_id : 0,
                                  invoice : {status : "", totalPrice : 0},
                                  ordered_items : [],
                                  date : new Date(0)}
    const defaultStock : StockItem[] = [];
    const defaultOrderItem : OrderedItem = { stock_id : -1,
                                             amount : -1}
    const defaultItems : OrderedItem[] = [];
    const [ selectedVal, setSelectedVal ] = useState("");

    const [ postComplete, setPostComplete ] = useState(false);
    const [ gotStock, setGotStock ] = useState(false);
    const [ stock, setStock ] = useState(defaultStock)
    const [ newOrder, setNewOrder ] = useState(defaultOrder);
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

    function incrementOrderItems(newItemName : String){
        let newItem : OrderedItem = {stock_id : getStockFromName(newItemName)?.id,
                                     amount : 0}
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

    const handleChange = (event : any) => {
        setSelectedVal(event.target.value)
    }

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
        //create dropdown menu
        let select = document.getElementById("selectId")
        for(let i = 0; i < stock.length; i++) {
            var el = document.createElement("option");
            el.textContent = stock[i].name;
            el.value = stock[i].name;
            if(!(select?.contains(el))){
                select?.appendChild(el);
            }
        }
        let curItemId : any;
        let stockItem : StockItem = {id : 0,    
                                    name: "",
                                    description: "",
                                    count : 0,
                                    price: 0};

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
                                    <td>{0}</td>
                                    <td><button className='remove-item-button' onClick={() => decrementOrderItems(idx)}>Remove</button></td>
                                </tr>
                            )}
                            <tr>
                                <td>
                                    <select id="selectId" value={selectedVal} onChange={handleChange} className = 'select-menu'></select>
                                </td>
                                <td>{getStockFromName(selectedVal)?.count}</td>
                                <td>{getStockFromName(selectedVal)?.price}</td>
                                <td>{0}</td>
                            </tr>
                            <button className='add-item-button' onClick={() => incrementOrderItems(selectedVal)}>Add</button>
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