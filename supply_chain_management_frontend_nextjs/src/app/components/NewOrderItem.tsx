import React, { useState, useEffect } from 'react'

const NewOrderItem = (props : any) => {
  const defaultStock : StockItem[] = [];
  const [ gotStock, setGotStock ] = useState(false);
  const [ stock, setStock ] = useState(defaultStock)
  const [ error, setError] = useState(false)
  const id = props.stock_id;

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
    const stockItem = getStockFromId(id, stock)
    return(
      <div className=''>
        {stockItem?.name}
      </div>
    )
  } else {
    return(
      <div>Loading...</div>
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

function getStockFromId(id : any, stock : StockItem[]){
  return stock.find(obj => obj.id == id)
}


export default NewOrderItem